package com.lottery.types.annotation;


import java.lang.annotation.*;

/**
 * @author 永
 * 注解，动态配置中心
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface DCCValue {
    String value() default "";
}
