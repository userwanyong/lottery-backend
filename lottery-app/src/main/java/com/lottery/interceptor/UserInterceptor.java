package com.lottery.interceptor;


import cn.hutool.core.util.StrUtil;
import com.lottery.types.util.JWTUtils;
import com.lottery.types.util.ThreadUtils;
import com.lottery.types.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 永
 * 拦截器，将用户信息从token中取出，放入到线程上下文中
 */
@Slf4j
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截器处理请求: {} {}", request.getMethod(), request.getRequestURI());
        // 获取token
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isEmpty(token)) {
            log.warn("token为空");
            return false;
        }
        // 校验token
        if (!JWTUtils.verify(token)) {
            log.warn("token验证失败");
            return false;
        }
        // 存入ThreadLocal
        UserUtils userUtils = JWTUtils.getUser(token);
        ThreadUtils.setUser(userUtils);
        log.info("已将用户信息存入线程上下文：{}", userUtils);
        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // TODO 避免内存泄漏（试用）
        ThreadUtils.removeUser();
//        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
