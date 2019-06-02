package com.github.ayongw.thymeleaf.dynamicurl.processor;


import com.github.ayongw.thymeleaf.dynamicurl.dialect.DynamicProcessConf;
import com.github.ayongw.thymeleaf.dynamicurl.service.DynamicResourceLocationService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
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
     * 能适用压缩文件名后缀的文件类型
     */
    private String minSuffixTypes = "css,js,";

    private Cache<String, String> localUrlSuffixCaches = CacheBuilder.newBuilder()
            .maximumSize(500L)
            .expireAfterAccess(5, TimeUnit.MINUTES)
//            .softValues()
            .build();

    /**
     * 处理后输出到的属性名
     */
    private String outputAttrName;
    private DynamicProcessConf dynamicProcessConf;

    private DynamicResourceLocationService dynamicResourceLocationService;

    /**
     * 资源定义服务类获取标识
     */
    private boolean dynamicResourceLocationServiceLoaded = false;

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
        if (dynamicProcessConf.isEnableProcess()) {
            // 远程未启用，则本地替换也停用
            if (!dynamicProcessConf.isEnableRemoteReplace()) {
                dynamicProcessConf.setEnableLocalReplace(false);
            }
        } else {
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

        final RequestContext requestContext =
                (RequestContext) context.getVariable(SpringContextVariableNames.SPRING_REQUEST_CONTEXT);
        if (requestContext == null) {
            LOGGER.warn("不在Spring的web环境下，不能处理！");
            return;
        }
        String targetUrl = HtmlEscape.escapeHtml4Xml(expressionResult == null ? "" : expressionResult.toString());

        if (!dynamicProcessConf.isEnableProcess()) {
            structureHandler.setAttribute(outputAttrName, targetUrl);
            return;
        }

        Matcher matcher = HTTP_URL_PATTERN.matcher(targetUrl);
        if (matcher.matches()) {
            structureHandler.setAttribute(outputAttrName, targetUrl);
            return;
        }
        String contextPath = requestContext.getContextPath();
        if (dynamicProcessConf.isEnableRemoteReplace() && isReplaceRemoteLocation(contextPath, targetUrl)) {
            ApplicationContext applicationContext = SpringContextUtils.getApplicationContext(context);
            DynamicResourceLocationService resourceLocationService = getDynamicResourceLocationService(applicationContext);
            String queryUrl = targetUrl;
            if (StringUtils.isNotBlank(contextPath) && !"/".equals(contextPath)
                    && StringUtils.startsWithIgnoreCase(queryUrl, contextPath)) {
                queryUrl = queryUrl.substring(contextPath.length());
            }
            String remoteUrl = resourceLocationService.findRemoteUrl(queryUrl);
            if(StringUtils.isNotBlank(remoteUrl)) {
                targetUrl = remoteUrl;
            }
        } else if (dynamicProcessConf.isEnableLocalReplace() && StringUtils.isNotBlank(dynamicProcessConf.getLocalReplaceSuffix())) {
            targetUrl = getLocalSuffixUrl(targetUrl);
        }

        LOGGER.debug("解析值 {}==>{}", expressionResult, targetUrl);
        structureHandler.setAttribute(outputAttrName, targetUrl);
    }

    /**
     * 获取本地资源地址的转换地址
     *
     * @param localUrl 原始本地资源地址
     * @return 转换后的地址
     */
    private String getLocalSuffixUrl(final String localUrl) {
        try {
            return localUrlSuffixCaches.get(localUrl, () -> {
                String ext = getFileExt(localUrl);
                boolean flag = true;
                if (StringUtils.isBlank(ext)
                        || (StringUtils.isNotBlank(minSuffixTypes) && !minSuffixTypes.contains(ext + ","))) {
                    flag = false;
                }
                if (flag) {
                    String path = localUrl.substring(0, localUrl.lastIndexOf("/") + 1);
                    String fileName = FilenameUtils.getName(localUrl);
                    if (!StringUtils.endsWithIgnoreCase(fileName, dynamicProcessConf.getLocalReplaceSuffix() + "." + ext)) {
                        fileName = suffixFileName(localUrl, dynamicProcessConf.getLocalReplaceSuffix());
                        return path + fileName;
                    }
                }


                return localUrl;
            });
        } catch (ExecutionException e) {
            LOGGER.warn("从本地缓存中获取资源url的本地压缩地址失败！", e);
        }
        return localUrl;
    }

    public static String suffixFileName(String fileName, String suffix) {
        String ext = FilenameUtils.getExtension(fileName);
        String result = FilenameUtils.getBaseName(fileName) + suffix;
        if (StringUtils.isBlank(ext)) {
            return result;
        }
        return result + "." + ext;
    }

    /**
     * 是否是可替换为远程资源的本地资源url
     *
     * @param contextPath 当前环境下下文地址
     * @param url         资源地址
     * @return false：未配置本地与远程地址映射转换
     */
    private boolean isReplaceRemoteLocation(String contextPath, String url) {
        String[] localToRemotePrefixes = dynamicProcessConf.getRemoteReplacePrefixes();
        if (null == localToRemotePrefixes || localToRemotePrefixes.length == 0) {
            return false;
        }

        for (String prefix : localToRemotePrefixes) {
            if (StringUtils.startsWithIgnoreCase(url, prefix)
                    || StringUtils.startsWithIgnoreCase(url, contextPath + prefix)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取本地资源地址动态替换服务类
     *
     * @param applicationContext 当前Spring容器对象
     * @return 服务实例对象
     */
    private DynamicResourceLocationService getDynamicResourceLocationService(ApplicationContext applicationContext) {
        if (dynamicResourceLocationServiceLoaded) {
            return dynamicResourceLocationService;
        }

        dynamicResourceLocationService = applicationContext.getBean(DynamicResourceLocationService.class);
        dynamicResourceLocationServiceLoaded = true;
        return dynamicResourceLocationService;
    }


    /**
     * 获取文件扩展名
     *
     * @param filePath 文件路径
     * @return 文件的扩展名
     */
    private String getFileExt(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return "";
        }
        int idx = filePath.lastIndexOf(".");
        if (idx == -1) {
            return "";
        }

        StringBuilder sb = new StringBuilder(filePath.substring(idx + 1));
        idx = sb.lastIndexOf("#");
        if (idx > 0) {
            sb = sb.delete(idx, sb.length());
        }
        idx = sb.lastIndexOf("?");
        if (idx > 0) {
            sb = sb.delete(idx, sb.length());
        }
        return sb.toString();
    }

}
