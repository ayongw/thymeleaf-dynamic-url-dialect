package com.github.ayongw.thymeleaf.dynamicurl.dialect;

import com.github.ayongw.thymeleaf.dynamicurl.processor.DynamicHrefAttributeTagProcessor;
import com.github.ayongw.thymeleaf.dynamicurl.processor.DynamicSrcAttributeTagProcessor;
import com.github.ayongw.thymeleaf.dynamicurl.processor.DynamicUrlAttributeTagProcessor;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 动态资源位置方言
 *
 * 默认前缀：awdl(ayongw-dynamicLocation)
 *
 * @author jiangguangtao 2018/4/5
 */
public class DynamicResourceLocationDialect extends AbstractProcessorDialect {
    public static final String NAME = "DynamicResourceLocation";
    public static final String PREFIX = "awdl";
    public static final int PROCESSOR_PRECEDENCE = 10000;

    private DynamicProcessConf dynamicProcessConf;

    public DynamicResourceLocationDialect() {
        super(NAME, PREFIX, PROCESSOR_PRECEDENCE);
        DynamicProcessConf dynamicProcessConf = new DynamicProcessConf();
        dynamicProcessConf.setEnableProcess(false);
        setDynamicProcessConf(dynamicProcessConf);
    }

    public DynamicResourceLocationDialect(DynamicProcessConf dynamicProcessConf) {
        super(NAME, PREFIX, PROCESSOR_PRECEDENCE);
        setDynamicProcessConf(dynamicProcessConf);
    }

    protected DynamicResourceLocationDialect(String prefix, DynamicProcessConf dynamicProcessConf) {
        super(NAME, prefix, PROCESSOR_PRECEDENCE);
        setDynamicProcessConf(dynamicProcessConf);
    }

    public void setDynamicProcessConf(DynamicProcessConf conf) {
        this.dynamicProcessConf = conf;

        if (conf.isEnableProcess()) {
            if (conf.isEnableRemoteReplace()
                    && isEmpty(conf.getRemoteReplacePrefixes())) {
                throw new IllegalArgumentException("启用远程资源替换时未指定远程资源前缀列表");
            }
        }

    }

    @Override
    public Set<IProcessor> getProcessors(String dialectPrefix) {
        return createProcessorsSet(dialectPrefix);
    }

    private boolean isEmpty(String[] cols) {
        return null == cols || cols.length == 0;
    }


    private Set<IProcessor> createProcessorsSet(String dialectPrefix) {
        final Set<IProcessor> processors = new LinkedHashSet<IProcessor>();

        processors.add(new DynamicUrlAttributeTagProcessor(dialectPrefix, dynamicProcessConf));
        processors.add(new DynamicHrefAttributeTagProcessor(dialectPrefix, dynamicProcessConf));
        processors.add(new DynamicSrcAttributeTagProcessor(dialectPrefix, dynamicProcessConf));

        return processors;
    }
}
