package com.github.ayongw.thymeleaf.dynamicurl.service;

/**
 * 资源转换服务类
 *
 * @author jiangguangtao
 */
public interface ResourceTranslatorService {
    /**
     * 转换资源地址到最终需要的地址
     *
     * @param contextPath servlet基地址
     * @param requestUrl  原始资源请求地址
     * @return 实际需要使用的地址
     */
    String translatePath(String contextPath, String requestUrl);
}
