package com.lottery.config;


import com.lottery.interceptor.UserInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;

/**
 * @author 永
 * 注册拦截器
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Resource
    private UserInterceptor userInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/refresh")
                .excludePathPatterns("/user/logout")
                .excludePathPatterns("/health/check");
    }
}
