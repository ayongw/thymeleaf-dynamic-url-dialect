package com.github.ayongw.thymeleaf.dynamicurl.service;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 根据配置的属性列表进行替换，一个默认的实现
 *
 * @author jiangguangtao
 */
public class PropertyDynamicResourceLocationService implements DynamicResourceLocationService {

    private Map<String, String> resourceMap;

    public PropertyDynamicResourceLocationService(Map<String, String> resourceMap) {
        this.resourceMap = resourceMap;
    }

    @Override
    public String findRemoteUrl(String localUrl) {
        localUrl = StringUtils.trim(localUrl);
        if (StringUtils.isBlank(localUrl)) {
            return null;
        }

        if (null == resourceMap || resourceMap.size() == 0) {
            return localUrl;
        }

        if (!resourceMap.containsKey(localUrl)) {
            return localUrl;
        }

        return resourceMap.get(localUrl);
    }
}
