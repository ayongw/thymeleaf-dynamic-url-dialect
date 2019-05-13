package com.github.ayongw.thymeleaf.dynamicurl.processor;


import com.github.ayongw.thymeleaf.dynamicurl.dialect.DynamicProcessConf;

/**
 * 替换url路径用的
 *
 * @author jiangguangtao 2018/4/5
 */
public class DynamicUrlAttributeTagProcessor extends AbstractResourceLocationAttributeTagProcessor {
    public static final String ATTR_NAME = "url";
    public static final String OUTPUT_ATTR_NAME = "url";

    public DynamicUrlAttributeTagProcessor(String dialectPrefix, DynamicProcessConf dynamicProcessConf) {
        super(dialectPrefix, ATTR_NAME, OUTPUT_ATTR_NAME, dynamicProcessConf);
    }
}
