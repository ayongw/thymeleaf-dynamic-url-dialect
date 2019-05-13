package com.github.ayongw.thymeleaf.dynamicurl.bootconf;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用注解的方式启用本地静态资源地址动态替换功能。
 *
 * @author jiangguangtao 2018/4/30
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({DynamicResourceLocationDialectAutoConfiguration.class})
public @interface EnableDynamicReourceLocationReplace {
}
