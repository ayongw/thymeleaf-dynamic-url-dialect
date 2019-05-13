package com.github.ayongw.thymeleaf.dynamicurl.processor;


import com.github.ayongw.thymeleaf.dynamicurl.dialect.DynamicProcessConf;

/**
 * 替换url路径用的
 * @author jiangguangtao 2018/4/5
 */
public class DynamicHrefAttributeTagProcessor extends AbstractResourceLocationAttributeTagProcessor {
    public static final String ATTR_NAME = "href";
    public static final String OUTPUT_ATTR_NAME = "href";

    public DynamicHrefAttributeTagProcessor(String dialectPrefix, DynamicProcessConf dynamicProcessConf) {
        super(dialectPrefix, ATTR_NAME, OUTPUT_ATTR_NAME, dynamicProcessConf);
    }
}
