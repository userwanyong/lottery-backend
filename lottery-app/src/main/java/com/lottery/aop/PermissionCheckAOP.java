package com.lottery.aop;

import com.lottery.types.annotation.PermissionCheck;
import com.lottery.types.exception.PermissionException;
import com.lottery.types.util.ThreadUtils;
import com.lottery.types.util.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 永
 */
@Slf4j
@Aspect
@Component
public class PermissionCheckAOP {

    public PermissionCheckAOP() {
        log.info("PermissionCheckAOP component initialized");
    }

    @Before("@annotation(permissionCheck)")
    public void checkPermission(JoinPoint joinPoint, PermissionCheck permissionCheck) {
        UserUtils userUtils = ThreadUtils.getUser();
        if (userUtils == null) {
            log.warn("用户未登录");
            throw new PermissionException("用户未登录");
        }
        List<String> userRoles = userUtils.getRoles();
        if (userRoles == null || userRoles.isEmpty()) {
            throw new PermissionException("用户权限不足");
        }
        for (String requiredRole : permissionCheck.roles()) {
            if (userRoles.contains(requiredRole)) {
                log.info("用户权限验证通过");
                return;
            }
        }
        throw new PermissionException("用户权限不足");
    }
}
