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
                // 登录方式发现 / 各类登录入口 / 令牌刷新 / OAuth 授权与回调：公开访问
                .excludePathPatterns("/user/login-methods")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/user/send-code")
                .excludePathPatterns("/user/login-by-code")
                .excludePathPatterns("/user/refresh")
                .excludePathPatterns("/user/oauth/**")
                .excludePathPatterns("/health/check");
    }
}
