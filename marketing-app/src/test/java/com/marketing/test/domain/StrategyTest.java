package com.marketing.test.domain;

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
        log.info("测试结果：{} - 奖品ID值", strategyService.getRandomAwardId(100001L));
        log.info("测试结果：{} - 奖品ID值", strategyService.getRandomAwardId(100001L));
        log.info("测试结果：{} - 奖品ID值", strategyService.getRandomAwardId(100001L));
        log.info("测试结果：{} - 奖品ID值", strategyService.getRandomAwardId(100001L));
        log.info("测试结果：{} - 奖品ID值", strategyService.getRandomAwardId(100001L));
    }


}
