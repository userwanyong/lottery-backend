package com.lottery.interceptor;


import cn.hutool.core.util.StrUtil;
import cn.wanyj.auth.api.protobuf.StringValue;
import cn.wanyj.auth.api.protobuf.TokenRpcServiceProtobuf;
import cn.wanyj.auth.api.protobuf.TokenValidationResult;
import com.alibaba.fastjson.JSON;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.model.BaseResponse;
import com.lottery.types.util.ThreadUtils;
import com.lottery.types.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * @author 永
 * 拦截器，将用户信息从token中取出，放入到线程上下文中
 */
@Slf4j
@Component
public class UserInterceptor implements HandlerInterceptor {

    @DubboReference(version = "1.0.0", check = false)
    private TokenRpcServiceProtobuf tokenRpcService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截器处理请求: {} {}", request.getMethod(), request.getRequestURI());
        // 获取token
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StrUtil.isEmpty(authHeader)) {
            log.warn("token为空");
            writeUnauthorized(response, "未提供认证令牌");
            return false;
        }
        // 去除 Bearer 前缀
        String token = authHeader;
        if (authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        // 通过RPC校验token
        try {
            TokenValidationResult result = tokenRpcService.parseToken(
                    StringValue.newBuilder().setValue(token).build()
            );
            if (!result.getValid()) {
                log.warn("token验证失败");
                writeUnauthorized(response, "认证令牌无效或已过期");
                return false;
            }
            // 存入ThreadLocal
            UserUtils userUtils = new UserUtils();
            userUtils.setId(result.getUserId());
            userUtils.setUsername(result.getUsername());
            userUtils.setRoles(new ArrayList<>(result.getRolesList()));
            userUtils.setPermissions(new ArrayList<>(result.getPermissionsList()));
            userUtils.setTenantId(result.getTenantId());
            ThreadUtils.setUser(userUtils);
            log.info("已将用户信息存入线程上下文：{}", userUtils);
            return true;
        } catch (Exception e) {
            log.error("token验证异常", e);
            writeUnauthorized(response, "认证服务异常");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadUtils.removeUser();
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        BaseResponse<Void> body = new BaseResponse<>(ResponseCode.NO_LOGIN.getCode(), message);
        response.getWriter().write(JSON.toJSONString(body));
    }
}
