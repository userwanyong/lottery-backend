package com.lottery.types.annotation;

import java.lang.annotation.*;

/**
 * @author 永
 * 权限校验
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface PermissionCheck {
    String[] roles();
}
