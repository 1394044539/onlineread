package com.wpy.interceptor;

import com.wpy.constant.CharConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 自定义拦截器配置文件
 * @author pywang
 * @date
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Value("${upload.img.rootpath}")
    private String rootPath;

    @Bean
    public HandlerInterceptor getMyInterceptor(){
        return new Loginlnterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(getMyInterceptor());
        interceptorRegistration.excludePathPatterns("/img/**");
        interceptorRegistration.excludePathPatterns("/rest/**");
        interceptorRegistration.excludePathPatterns("/error");

        interceptorRegistration.excludePathPatterns("/sysNotice/list");
        interceptorRegistration.excludePathPatterns("/sysNotice/openOrClose");
        interceptorRegistration.excludePathPatterns("/novel/novelInfo");
        interceptorRegistration.excludePathPatterns("/readHistory/history");
        interceptorRegistration.excludePathPatterns("/readHistory/list");
        interceptorRegistration.excludePathPatterns("/novelChapter/content");
        interceptorRegistration.excludePathPatterns("/novelChapter/list");
        interceptorRegistration.excludePathPatterns("/userCollection/collection");

        interceptorRegistration.excludePathPatterns("/novel/download");
        interceptorRegistration.excludePathPatterns("/sysUser/login");
        interceptorRegistration.excludePathPatterns("/sysUser/reg");
        interceptorRegistration.excludePathPatterns("/sysUser/getVerifyCode");
        interceptorRegistration.excludePathPatterns("/sysUser/checkAccount");
        interceptorRegistration.excludePathPatterns("/sysUser/checkPhone");
        interceptorRegistration.excludePathPatterns("/sysUser/getVerifyCode");
        interceptorRegistration.excludePathPatterns("/novel/list");


    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**").addResourceLocations("file:"+rootPath+ CharConstant.File_SEPARATOR);
    }
}
