package com.github.ayongw.thymeleaf.dynamicurl.processor;


import com.github.ayongw.thymeleaf.dynamicurl.dialect.DynamicProcessConf;
import com.github.ayongw.thymeleaf.dynamicurl.service.DynamicResourceLocationService;
import com.github.ayongw.thymeleaf.dynamicurl.service.ResourceTranslatorService;
import com.github.ayongw.thymeleaf.dynamicurl.service.impl.CacheResourceTranslatorServiceImpl;
import com.github.ayongw.thymeleaf.dynamicurl.service.impl.SimpleResourceTranslatorServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.support.RequestContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.spring5.context.SpringContextUtils;
import org.thymeleaf.spring5.naming.SpringContextVariableNames;
import org.thymeleaf.standard.processor.AbstractStandardExpressionAttributeTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;
import org.unbescape.html.HtmlEscape;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * URL标签动态替换标签处理类
 * <p>
 * 此类可以处理属性 url, href, src
 *
 * @author jiangguangtao 2018/4/5
 */
public class AbstractResourceLocationAttributeTagProcessor extends AbstractStandardExpressionAttributeTagProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractResourceLocationAttributeTagProcessor.class);

    private static final Pattern HTTP_URL_PATTERN = Pattern.compile("^(https?:)?//((.+?)+)(/.+?)?", Pattern.CASE_INSENSITIVE);

    private static final int PRECEDENCE = 10000;

    /**
     * 处理后输出到的属性名
     */
    private String outputAttrName;
    private DynamicProcessConf dynamicProcessConf;

    private ResourceTranslatorService resourceTranslatorService;

    /**
     * 资源定义服务类获取标识
     */
    private boolean serviceInited = false;

    /**
     * @param dialectPrefix      前缀
     * @param attrName           匹配属性名
     * @param dynamicProcessConf 配置参数vo
     */
    public AbstractResourceLocationAttributeTagProcessor(String dialectPrefix, String attrName, DynamicProcessConf dynamicProcessConf) {
        this(dialectPrefix, attrName, attrName, dynamicProcessConf);
    }

    /**
     * @param dialectPrefix      前缀
     * @param attrName           匹配属性名
     * @param outputAttrName     输出属性名
     * @param dynamicProcessConf 配置参数vo
     */
    public AbstractResourceLocationAttributeTagProcessor(String dialectPrefix, String attrName, String outputAttrName, DynamicProcessConf dynamicProcessConf) {
        super(TemplateMode.HTML, dialectPrefix, attrName, PRECEDENCE, attrName.equalsIgnoreCase(outputAttrName), true);
        this.dynamicProcessConf = dynamicProcessConf;
        this.outputAttrName = outputAttrName;

        // 总开关启用，其它的可以启用
        if (!dynamicProcessConf.isEnableProcess()) {
            // 未启用总的，其它全部领用
            dynamicProcessConf.setEnableLocalReplace(false);
            dynamicProcessConf.setEnableRemoteReplace(false);
        }
    }

    @Override
    protected void doProcess(ITemplateContext context, IProcessableElementTag tag, AttributeName attributeName,
                             String attributeValue, Object expressionResult,
                             IElementTagStructureHandler structureHandler) {
        /*
        处理过程说
        1.判断是否启用，如果未启用直接返回。
        2.判断是否是绝对url，如果是直接返回。
        3.
         */
        initTranslatorService(context);

        final RequestContext requestContext =
                (RequestContext) context.getVariable(SpringContextVariableNames.SPRING_REQUEST_CONTEXT);
        if (requestContext == null) {
            LOGGER.warn("不在Spring的web环境下，不能处理！");
            return;
        }
        String targetUrl = HtmlEscape.escapeHtml4Xml(expressionResult == null ? "" : expressionResult.toString());

        // 全局未开启时不处理
        if (!dynamicProcessConf.isEnableProcess()) {
            structureHandler.setAttribute(outputAttrName, targetUrl);
            return;
        }

        // 如果已经是http前缀的地址了，不用处理
        Matcher matcher = HTTP_URL_PATTERN.matcher(targetUrl);
        if (matcher.matches()) {
            structureHandler.setAttribute(outputAttrName, targetUrl);
            return;
        }

        targetUrl = resourceTranslatorService.translatePath(requestContext.getContextPath(), targetUrl);
        LOGGER.debug("解析值 {}==>{}", expressionResult, targetUrl);
        structureHandler.setAttribute(outputAttrName, targetUrl);
    }

    /**
     * 初始化转换服务类
     *
     * @param context
     */
    private void initTranslatorService(ITemplateContext context) {
        if (serviceInited) {
            return;
        }

        ApplicationContext applicationContext = SpringContextUtils.getApplicationContext(context);
        DynamicResourceLocationService dynamicResourceLocationService = applicationContext.getBean(DynamicResourceLocationService.class);

        if (dynamicProcessConf.isEnableCache()) {
            resourceTranslatorService = new CacheResourceTranslatorServiceImpl(dynamicProcessConf, dynamicResourceLocationService);
        } else {
            resourceTranslatorService = new SimpleResourceTranslatorServiceImpl(dynamicProcessConf, dynamicResourceLocationService);
        }
    }
}
