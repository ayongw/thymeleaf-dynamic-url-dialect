package com.github.ayongw.test.dymamicurl;

import com.github.ayongw.thymeleaf.dynamicurl.utils.InnerUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jiangguangtao
 */
public class InnerUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(InnerUtilsTest.class);

    @Test
    public void testGetPureUrl() {

        String url = "/admin/article/updateArticle.htm?id=5&username=2349890";
        logger.info("原始地址：{}", url);
        String purePath = InnerUtils.getPurePath(url);
        logger.info("转换后的地址：{}", purePath);

        Assert.assertEquals("/admin/article/updateArticle.htm", purePath);


        url = "/admin/article/updateArticle.htm#/sdfh/234?id=5&username=2349890";
        purePath = InnerUtils.getPurePath(url);
        Assert.assertEquals("/admin/article/updateArticle.htm", purePath);
    }

    @Test
    public void testGetFileExt() {
        String fileName = "updateArticle.htm";
        Assert.assertEquals("htm", InnerUtils.getFileExt(fileName));

        fileName = "updateArticletm";
        Assert.assertEquals("", InnerUtils.getFileExt(fileName));

        fileName = null;
        Assert.assertEquals("", InnerUtils.getFileExt(fileName));
    }

    @Test
    public void testSuffixFileName() {
        String fileName = "updateArticle.htm";

        String result = InnerUtils.suffixFileName(fileName, ".min");
        Assert.assertEquals("updateArticle.min.htm", result);
    }
}
