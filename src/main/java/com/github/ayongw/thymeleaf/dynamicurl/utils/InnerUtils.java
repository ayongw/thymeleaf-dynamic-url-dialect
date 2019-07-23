package com.github.ayongw.thymeleaf.dynamicurl.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author jiangguangtao
 */
public class InnerUtils {
    /**
     * 获取不带参数的路径参数
     *
     * @param url
     * @return
     */
    public static String getPurePath(String url) {
        if (url.indexOf('?') > 0) {
            url = url.substring(0, url.lastIndexOf('?'));
        }

        if (url.indexOf('#') > 0) {
            url = url.substring(0, url.lastIndexOf('#'));
        }

        return url;
    }


    /**
     * 获取文件扩展名
     *
     * @param filePath 文件路径
     * @return 文件的扩展名
     */
    public static String getFileExt(String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return "";
        }
        int idx = filePath.lastIndexOf(".");
        if (idx == -1) {
            return "";
        }

        return filePath.substring(idx + 1);
    }

    /**
     * 在文件名后添加后缀
     *
     * @param fileName 原文件名
     * @param suffix   后缀
     * @return
     */
    public static String suffixFileName(String fileName, String suffix) {
        String ext = FilenameUtils.getExtension(fileName);
        String result = FilenameUtils.getBaseName(fileName) + suffix;
        if (StringUtils.isBlank(ext)) {
            return result;
        }
        return result + "." + ext;
    }
}
