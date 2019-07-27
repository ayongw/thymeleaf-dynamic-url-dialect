package com.github.ayongw.thymeleaf.dynamicurl.service.impl;

import com.github.ayongw.thymeleaf.dynamicurl.dialect.DynamicProcessConf;
import com.github.ayongw.thymeleaf.dynamicurl.service.DynamicResourceLocationService;
import com.github.ayongw.thymeleaf.dynamicurl.service.ResourceTranslatorService;
import com.github.ayongw.thymeleaf.dynamicurl.utils.InnerUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 简单实现的资源地址转换服务
 *
 * @author jiangguangtao
 */
public abstract class BaseResourceTranslatorServiceImpl implements ResourceTranslatorService {
    /**
     * 能适用压缩文件名后缀的文件类型
     */
    private final String minSuffixTypes = "css,js,";

    private DynamicProcessConf dynamicProcessConf;
    private DynamicResourceLocationService resourceLocationService;

    public BaseResourceTranslatorServiceImpl(DynamicProcessConf dynamicProcessConf,
                                             DynamicResourceLocationService resourceLocationService) {
        this.dynamicProcessConf = dynamicProcessConf;
        this.resourceLocationService = resourceLocationService;
    }

    protected DynamicResourceLocationService getResourceLocationService() {
        return resourceLocationService;
    }

    /**
     * 判断指定的路径是否是要排除的路径
     *
     * @param url 请求地址
     * @return true是，不转换
     */
    protected boolean isExcludedPath(String url) {
        return dynamicProcessConf.isExcludedPath(url);
    }


    /**
     * 是否是可替换为远程资源的本地资源url
     * <p>
     * 判断资源地址是否为可转换远程地址的
     *
     * @param url 资源地址
     * @return false：未配置本地与远程地址映射转换
     */
    protected boolean isReplaceRemoteLocation(String url) {
        String[] localToRemotePrefixes = dynamicProcessConf.getRemoteReplacePrefixes();
        if (null == localToRemotePrefixes || localToRemotePrefixes.length == 0) {
            return false;
        }

        for (String prefix : localToRemotePrefixes) {
            if (StringUtils.startsWithIgnoreCase(url, prefix)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取本地资源地址的压缩文件地址
     *
     * @param localUrl 本地资源文件地址
     * @return 本地压缩文件地址
     */
    protected String doGetLocalSuffixUrl(String localUrl) {
        String ext = InnerUtils.getFileExt(localUrl);
        if (StringUtils.isBlank(ext)) {
            return localUrl;
        }
        if (!minSuffixTypes.contains(ext + ",")) {
            return localUrl;
        }

        String path = localUrl.substring(0, localUrl.lastIndexOf("/") + 1);
        String fileName = FilenameUtils.getName(localUrl);
        if (!StringUtils.endsWithIgnoreCase(fileName, dynamicProcessConf.getLocalReplaceSuffix() + "." + ext)) {
            fileName = InnerUtils.suffixFileName(localUrl, dynamicProcessConf.getLocalReplaceSuffix());
            return path + fileName;
        }
        return localUrl;
    }

    /**
     * 获取本地资源文件的压缩文件地址
     *
     * @param localUrl 本地资源文件地址
     * @return 本地压缩文件地址
     */
    public String getLocalSuffixUrl(String localUrl) {
        return doGetLocalSuffixUrl(localUrl);
    }

    /**
     * 执行直接的转换操作
     *
     * @param contextPath servlet前缀
     * @param requestPath 请求地址
     * @return 转换后的请求地址
     */
    protected String doTranslate(String contextPath, String requestPath) {
        // 去掉Servlet前缀、去掉后置参数后的用于判断的参数
        String judgePath = StringUtils.removeStartIgnoreCase(InnerUtils.getPurePath(requestPath), contextPath);

        // 如果地址在排除列表中，直接返回
        if (isExcludedPath(judgePath)) {
            return requestPath;
        }

        if (dynamicProcessConf.isEnableRemoteReplace() && isReplaceRemoteLocation(judgePath)) {
            String remoteUrl = resourceLocationService.findRemoteUrl(judgePath);
            if (StringUtils.isNotBlank(remoteUrl)) {
                requestPath = remoteUrl;
            }
        } else if (dynamicProcessConf.isEnableLocalReplace()
                && StringUtils.isNotBlank(dynamicProcessConf.getLocalReplaceSuffix())
                && !isReplaceRemoteLocation(judgePath)) {
            requestPath = getLocalSuffixUrl(InnerUtils.getPurePath(requestPath));
        }

        return requestPath;
    }

}
