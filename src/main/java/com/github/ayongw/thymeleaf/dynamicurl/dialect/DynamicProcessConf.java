package com.github.ayongw.thymeleaf.dynamicurl.dialect;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 静态资源处理配置
 *
 * @author jiangguangtao 2018/4/6
 */
public class DynamicProcessConf implements Serializable {
    /**
     * 是否启用处理 总的开关项
     */
    private boolean enableProcess = true;
    /**
     * 启用本地静态替换？ 本地资源资源后加min处理 加速
     */
    private boolean enableLocalReplace = true;
    /**
     * 启用远程静态资源替换？ 将静态资源引用转换为CDN引用 加速
     */
    private boolean enableRemoteReplace = true;
    /**
     * 本地资源静态资源替换时追加的后缀
     */
    private String localReplaceSuffix = ".min";

    /**
     * 远程资源替换时处理的前缀列表
     * <p>
     * 本地资源文件替换为远程资源文件位置的前缀
     */
    private String[] remoteReplacePrefixes;

    /**
     * enable the resource translator cache
     * <p>
     * 是否启用转换服务缓存
     */
    private boolean enableCache = true;

    /**
     * 保存排除路径地址列表，用map存储用于加快查询
     */
    private Map<String, Boolean> excludePathMap;

    public DynamicProcessConf() {
    }

    public DynamicProcessConf(boolean enableProcess, boolean enableLocalReplace, boolean enableRemoteReplace,
                              String localReplaceSuffix, String[] remoteReplacePrefixes) {
        this.enableProcess = enableProcess;
        this.enableLocalReplace = enableLocalReplace;
        this.enableRemoteReplace = enableRemoteReplace;
        this.localReplaceSuffix = localReplaceSuffix;
        this.remoteReplacePrefixes = remoteReplacePrefixes;
    }

    public boolean isEnableProcess() {
        return enableProcess;
    }

    public void setEnableProcess(boolean enableProcess) {
        this.enableProcess = enableProcess;
    }

    public boolean isEnableLocalReplace() {
        return enableLocalReplace;
    }

    public void setEnableLocalReplace(boolean enableLocalReplace) {
        this.enableLocalReplace = enableLocalReplace;
    }

    public boolean isEnableRemoteReplace() {
        return enableRemoteReplace;
    }

    public void setEnableRemoteReplace(boolean enableRemoteReplace) {
        this.enableRemoteReplace = enableRemoteReplace;
    }

    public String getLocalReplaceSuffix() {
        return localReplaceSuffix;
    }

    public void setLocalReplaceSuffix(String localReplaceSuffix) {
        this.localReplaceSuffix = localReplaceSuffix;
    }

    public String[] getRemoteReplacePrefixes() {
        return remoteReplacePrefixes;
    }

    public void setRemoteReplacePrefixes(String[] remoteReplacePrefixes) {
        this.remoteReplacePrefixes = remoteReplacePrefixes;
    }

    public boolean isEnableCache() {
        return enableCache;
    }

    public void setEnableCache(boolean enableCache) {
        this.enableCache = enableCache;
    }

    public void setExcludePaths(List<String> excludePaths) {
        if (CollectionUtils.isEmpty(excludePaths)) {
            this.excludePathMap = null;
            return;
        }

        int size = excludePaths.size() > 32 ? 32 : excludePaths.size();
        excludePathMap = new HashMap<>(size);
        excludePaths.forEach(path -> excludePathMap.put(StringUtils.lowerCase(path), Boolean.TRUE));
    }

    /**
     * 判断指定的路径是否在排除列表中
     *
     * @param path 路径参数
     * @return true在，false不在
     */
    public boolean isExcludedPath(String path) {
        if (StringUtils.isBlank(path)) {
            return false;
        }
        return null != excludePathMap && excludePathMap.containsKey(StringUtils.lowerCase(path));
    }


    @Override
    public String toString() {
        return "DynamicProcessConf{" +
                "enableProcess=" + enableProcess +
                ", enableLocalReplace=" + enableLocalReplace +
                ", enableRemoteReplace=" + enableRemoteReplace +
                ", localReplaceSuffix='" + localReplaceSuffix + '\'' +
                ", remoteReplacePrefixes=" + Arrays.toString(remoteReplacePrefixes) +
                '}';
    }
}
