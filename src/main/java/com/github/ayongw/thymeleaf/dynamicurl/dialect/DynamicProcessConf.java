package com.github.ayongw.thymeleaf.dynamicurl.dialect;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 静态资源处理配置
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
