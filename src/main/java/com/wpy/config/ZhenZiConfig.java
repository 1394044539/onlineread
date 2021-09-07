package com.wpy.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 榛子云短信配置
 * @author pywang
 * @date 2020/12/6
 */
@Data
@Configuration
public class ZhenZiConfig {

    @Value("${zhen-zi-message.url}")
    private String url;

    @Value("${zhen-zi-message.app-id}")
    private String appId;

    @Value("${zhen-zi-message.app-secret}")
    private String appSecret;

}
