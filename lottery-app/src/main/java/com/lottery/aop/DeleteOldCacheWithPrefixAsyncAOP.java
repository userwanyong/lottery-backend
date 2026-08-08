package com.lottery.aop;

import com.lottery.infrastructure.redis.RedisService;
import com.lottery.types.annotation.DeleteOldCacheWithPrefixAsync;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 永
 * 操作结束后删除以前缀开头的旧缓存（轻量版：去 MQ，同步直接删除）
 */
@Aspect
@Slf4j
@Component
public class DeleteOldCacheWithPrefixAsyncAOP {
    @Resource
    private RedisService redisService;

    /**
     * 使用后置通知，在方法执行结束后且无异常时执行
     */
    @AfterReturning("@annotation(com.lottery.types.annotation.DeleteOldCacheWithPrefixAsync)&&@annotation(deleteOldCacheWithPrefixAsync)")
    public void after(JoinPoint joinPoint, DeleteOldCacheWithPrefixAsync deleteOldCacheWithPrefixAsync) {
        String[] key = deleteOldCacheWithPrefixAsync.key();
        for (String s : key) {
            try {
                redisService.deleteKeysWithPrefix(s);
                log.debug("[DeleteOldCacheWithPrefixAOP]删除缓存key成功 key_prefix: {}", s);
            } catch (Exception e) {
                log.error("[DeleteOldCacheWithPrefixAOP]删除缓存key失败 key_prefix: {}", s, e);
            }
        }

    }
}
