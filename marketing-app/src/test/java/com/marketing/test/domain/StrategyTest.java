package com.marketing.test.domain;

import com.marketing.domain.strategy.service.StrategyDispatch;
import com.marketing.domain.strategy.service.StrategyService;
import com.marketing.infrastructure.persistent.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RMap;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyTest {

    @Resource
    private StrategyService strategyService;

    @Resource
    private StrategyDispatch strategyDispatch;

    /**
     * 装配策略
     */
    @Test
    public void test_strategyArmory() {
        boolean success = strategyService.assembleLotteryStrategy(100001L);
        log.info("测试结果：{}", success);
    }

    /**
     * 从装配的策略中随机获取奖品ID值
     */
    @Test
    public void test_getAssembleRandomVal() {
        log.info("测试结果：{} - 奖品ID值", strategyDispatch.getRandomAwardId(100001L));
        log.info("测试结果：{} - 奖品ID值", strategyDispatch.getRandomAwardId(100001L));
        log.info("测试结果：{} - 奖品ID值", strategyDispatch.getRandomAwardId(100001L));
        log.info("测试结果：{} - 奖品ID值", strategyDispatch.getRandomAwardId(100001L));
        log.info("测试结果：{} - 奖品ID值", strategyDispatch.getRandomAwardId(100001L));
    }

    /**
     * 根据策略ID+权重值，从装配的策略中随机获取奖品ID值
     */
    @Test
    public void test_getRandomAwardId_ruleWeightValue() {
        log.info("测试结果：{} - 4000 策略配置", strategyDispatch.getRandomAwardId(100001L, "4000:102,103,104,105"));
        log.info("测试结果：{} - 5000 策略配置", strategyDispatch.getRandomAwardId(100001L, "5000:102,103,104,105,106,107"));
        log.info("测试结果：{} - 6000 策略配置", strategyDispatch.getRandomAwardId(100001L, "6000:102,103,104,105,106,107,108,109"));
    }


}
