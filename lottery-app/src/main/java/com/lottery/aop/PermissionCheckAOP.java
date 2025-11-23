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
        Integer currentRole = userUtils.getRole();
        for (int role : permissionCheck.roles()) {
            if (role == currentRole) {
                log.info("用户权限验证通过");
                return;
            }
        }
        throw new PermissionException("用户权限不足");
    }
}
