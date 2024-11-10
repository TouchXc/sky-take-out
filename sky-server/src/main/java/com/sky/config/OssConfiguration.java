package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 阿里云Oss配置类
 */
@Configuration
@Slf4j
public class OssConfiguration {
    @Bean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        log.info("配置类初始化执行：{}", aliOssProperties);
        return new AliOssUtil(aliOssProperties.getEndpoint(),aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),aliOssProperties.getBucketName());
    }
}
