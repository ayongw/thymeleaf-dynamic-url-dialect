package com.github.ayongw.thymeleaf.dynamicurl.service.impl;

import com.github.ayongw.thymeleaf.dynamicurl.dialect.DynamicProcessConf;
import com.github.ayongw.thymeleaf.dynamicurl.service.DynamicResourceLocationService;

/**
 * @author jiangguangtao
 */
public class SimpleResourceTranslatorServiceImpl extends BaseResourceTranslatorServiceImpl {
    public SimpleResourceTranslatorServiceImpl(DynamicProcessConf dynamicProcessConf,
                                               DynamicResourceLocationService dynamicResourceLocationService) {
        super(dynamicProcessConf, dynamicResourceLocationService);
    }

    @Override
    public String translatePath(String contextPath, String requestUrl) {
        return doTranslate(contextPath, requestUrl);
    }
}
