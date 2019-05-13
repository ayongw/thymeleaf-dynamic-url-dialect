package com.github.ayongw.thymeleaf.dynamicurl.bootconf;

import com.github.ayongw.thymeleaf.dynamicurl.dialect.DynamicProcessConf;
import com.github.ayongw.thymeleaf.dynamicurl.dialect.DynamicResourceLocationDialect;
import com.github.ayongw.thymeleaf.dynamicurl.service.DynamicResourceLocationService;
import com.github.ayongw.thymeleaf.dynamicurl.service.PropertyDynamicResourceLocationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.Map;

/**
 * 本地静态资源动态替换工具服务
 *
 * @author jiangguangtao 2018/4/30
 */
@Configuration
@ConditionalOnClass(SpringTemplateEngine.class)
@EnableConfigurationProperties({LocalResourceMappingConfigProps.class})
public class DynamicResourceLocationDialectAutoConfiguration {

    private LocalResourceMappingConfigProps localResourceMapping;

    public DynamicResourceLocationDialectAutoConfiguration(LocalResourceMappingConfigProps localResourceMapping) {
        this.localResourceMapping = localResourceMapping;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = LocalResourceMappingConfigProps.CONFIG_PREFIX, name = "remote-url-map", matchIfMissing = false)
    public DynamicResourceLocationService dynamicResourceLocationService() {
        Map<String, String> remoteUrlMap = localResourceMapping.getRemoteUrlMap();

        return new PropertyDynamicResourceLocationService(remoteUrlMap);
    }


    @Bean
    public DynamicResourceLocationDialect dynamicResourceLocationDialect() {
        DynamicProcessConf conf = new DynamicProcessConf();
        conf.setEnableProcess(localResourceMapping.isEnable());
        conf.setEnableLocalReplace(localResourceMapping.isEnableLocal());
        conf.setEnableRemoteReplace(localResourceMapping.isEnableRemote());
        conf.setLocalReplaceSuffix(localResourceMapping.getLocalSuffix());

        if (StringUtils.isNotBlank(localResourceMapping.getRemotePrefix())) {
            conf.setRemoteReplacePrefixes(StringUtils.split(localResourceMapping.getRemotePrefix()));
        }
        return new DynamicResourceLocationDialect(conf);
    }

}
