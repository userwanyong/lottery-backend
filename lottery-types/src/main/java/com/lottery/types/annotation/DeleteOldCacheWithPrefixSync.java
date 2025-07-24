package com.lottery.types.annotation;

import java.lang.annotation.*;

/**
 * @author 永
 * 删除以特定前缀开头的旧缓存注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface DeleteOldCacheWithPrefixSync {
    /**
     * 要删除的缓存key
     */
    String[] key();

    /**
     * 可传递的参数
     */
    String keyParam() default "";

}
