package com.wpy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 全局跨域问题解决
 * @author pywang
 * @date 2020/11/30
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET","HEAD","POST","DELETE","PUT","OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }
}
