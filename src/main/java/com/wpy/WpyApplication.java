package com.wpy;

import com.wpy.utils.SpringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author 13940
 */
@SpringBootApplication
@EnableConfigurationProperties
public class WpyApplication {

    public static void main(String[] args) {
        SpringApplication.run(WpyApplication.class, args);
    }

    /**
     * 注入springutil，用来特殊情况下获取bean
     * @return
     */
    @Bean
    public SpringUtils springUtil(){
        return new SpringUtils();
    }


}
