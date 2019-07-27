package com.github.ayongw.thymeleaf.dynamicurl.service.impl;

import com.github.ayongw.thymeleaf.dynamicurl.dialect.DynamicProcessConf;
import com.github.ayongw.thymeleaf.dynamicurl.service.DynamicResourceLocationService;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 使用缓存方式获取资源文件地址
 *
 * @author jiangguangtao
 */
public class CacheResourceTranslatorServiceImpl extends BaseResourceTranslatorServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(CacheResourceTranslatorServiceImpl.class);

    private Cache<String, String> translatorCaches = CacheBuilder.newBuilder()
            .maximumSize(500L)
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .expireAfterWrite(6, TimeUnit.HOURS)
            .weakKeys()
            .build();


    public CacheResourceTranslatorServiceImpl(DynamicProcessConf dynamicProcessConf,
                                              DynamicResourceLocationService dynamicResourceLocationService) {
        super(dynamicProcessConf, dynamicResourceLocationService);
    }

    @Override
    public String getLocalSuffixUrl(String localUrl) {
        return doGetLocalSuffixUrl(localUrl);
    }

    @Override
    public String translatePath(String contextPath, String requestUrl) {
        try {
            return translatorCaches.get(requestUrl, () -> doTranslate(contextPath, requestUrl));
        } catch (ExecutionException e) {
            logger.error("使用缓存方式转换资源异常", e);
        }
        return doTranslate(contextPath, requestUrl);
    }
}
