package com.github.ayongw.thymeleaf.dynamicurl.service;

/**
 * 资源动态地址替换服务类
 *
 * @author jiangguangtao 2018/4/5
 */
public interface DynamicResourceLocationService {

    /**
     * 根据本地地址url查询匹配的远程url地址
     * <p>
     * 如果有多个随机返回一个。
     *
     * @param localUrl 本地url地址
     * @return 远程url地址
     */
    String findRemoteUrl(String localUrl);
}
