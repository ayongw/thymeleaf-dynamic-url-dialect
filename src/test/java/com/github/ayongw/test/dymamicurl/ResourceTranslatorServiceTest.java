package com.github.ayongw.test.dymamicurl;

import com.github.ayongw.thymeleaf.dynamicurl.dialect.DynamicProcessConf;
import com.github.ayongw.thymeleaf.dynamicurl.service.DynamicResourceLocationService;
import com.github.ayongw.thymeleaf.dynamicurl.service.ResourceTranslatorService;
import com.github.ayongw.thymeleaf.dynamicurl.service.impl.PropertyDynamicResourceLocationServiceImpl;
import com.github.ayongw.thymeleaf.dynamicurl.service.impl.SimpleResourceTranslatorServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author jiangguangtao
 */
public class ResourceTranslatorServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(ResourceTranslatorServiceTest.class);
    private static ResourceTranslatorService resourceTranslatorService ;

    @Before
    public void initService() {
        DynamicProcessConf processConf = new DynamicProcessConf();

        Map<String, String> resourceMap = new LinkedHashMap<>();

        // CDN资源替换类
        DynamicResourceLocationService resourceLocationService = new PropertyDynamicResourceLocationServiceImpl(resourceMap);
        resourceTranslatorService = new SimpleResourceTranslatorServiceImpl(processConf, resourceLocationService);
    }


    @Test
    public void testTranslateLocalMinFile() {
        String contextPath = "";

        String url = "/demo/user/home.min.js";
        String result = resourceTranslatorService.translatePath(contextPath, url);
        logger.info("待测试的url {}，  转换后的url：{}", url, result);
        Assert.assertEquals(url, result);


        url = "/demo/user/home.js";
        result = resourceTranslatorService.translatePath(contextPath, url);
        logger.info("待测试的url {}，  转换后的url：{}", url, result);
        Assert.assertEquals("/demo/user/home.min.js", result);
    }
}
