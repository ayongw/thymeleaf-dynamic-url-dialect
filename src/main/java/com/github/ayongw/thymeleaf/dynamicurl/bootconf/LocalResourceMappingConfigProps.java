package com.github.ayongw.thymeleaf.dynamicurl.bootconf;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * 本地资源映射配置
 * 目前应用于Thymeleaf插件使用的。
 *
 * @author jiangguangtao
 */
@ConfigurationProperties(prefix = LocalResourceMappingConfigProps.CONFIG_PREFIX)
public class LocalResourceMappingConfigProps {
    public static final String CONFIG_PREFIX = "spring.thymeleaf.dynamic-url";
    /**
     * the whole switcher for enable replace or not
     */
    private boolean enable = true;

    /**
     * enable the local replace process?  replace local *.js file to *.min.js
     * 是否替换地址静态资源文件为压缩资源文件
     */
    private boolean enableLocal = true;
    /**
     * enable the remote replace process?
     */
    private boolean enableRemote = true;

    /**
     * local resource minified file suffix
     * <p>
     * 本地压缩的静态资源文件名称后缀
     */
    private String localSuffix = ".min";

    /**
     * local resource replace with remote resource location path prefix.
     * <p>
     * 本地资源文件替换CDN文件的前缀格式，如果有多个可以用逗号分隔开来
     */
    private String remotePrefix = "/static/lib/";

    /**
     * remote url replace map. localUrl-->remoteUrl
     * this is the default implement service config.
     * <p>
     * 本地url转换为远程cdn地址的映射配置。这是默认实现的服务类需要的配置属性
     */
    private Map<String, String> remoteUrlMap;


    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isEnableLocal() {
        return enableLocal;
    }

    public void setEnableLocal(boolean enableLocal) {
        this.enableLocal = enableLocal;
    }

    public boolean isEnableRemote() {
        return enableRemote;
    }

    public void setEnableRemote(boolean enableRemote) {
        this.enableRemote = enableRemote;
    }

    public String getLocalSuffix() {
        return localSuffix;
    }

    public void setLocalSuffix(String localSuffix) {
        this.localSuffix = localSuffix;
    }

    public String getRemotePrefix() {
        return remotePrefix;
    }

    public void setRemotePrefix(String remotePrefix) {
        this.remotePrefix = remotePrefix;
    }

    public Map<String, String> getRemoteUrlMap() {
        return remoteUrlMap;
    }

    public void setRemoteUrlMap(Map<String, String> remoteUrlMap) {
        this.remoteUrlMap = remoteUrlMap;
    }
}