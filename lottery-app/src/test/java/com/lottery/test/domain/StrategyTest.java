package com.lottery.test.domain;

import com.lottery.domain.strategy.service.strategy.StrategyArmory;
import com.lottery.domain.strategy.service.strategy.StrategyService;
import com.lottery.infrastructure.persistent.redis.RedisServiceImpl;
import com.lottery.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyTest {

    @Resource
    private StrategyArmory strategyArmory;

    @Resource
    private StrategyService strategyService;

    @Resource
    private RedisServiceImpl redisService;

    /**
     * 装配策略
     */
    @Test
    public void test_strategyArmory() {
        // 因为实际操作是更新数据库时，更新redis。这里装配抽奖策略时有部分参数是从原先redis缓存中获取的，测试先手动清空redis便于调试
        boolean success1 = strategyArmory.assembleLotteryStrategy(100001L);
        boolean success2 = strategyArmory.assembleLotteryStrategy(100002L);
        boolean success3 = strategyArmory.assembleLotteryStrategy(100003L);
        boolean success4 = strategyArmory.assembleLotteryStrategy(100006L);
        log.info("测试结果1：{}", success1);
        log.info("测试结果2：{}", success2);
        log.info("测试结果3：{}", success3);
        log.info("测试结果4：{}", success4);
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

    /**
     * 根据策略ID+权重值，从装配的策略中随机获取奖品ID值
     */
    @Test
    public void test_getRandomAwardId_ruleWeightValue() {
        log.info("测试结果：{} - 4000 策略配置", strategyService.getRandomAwardId(100001L, "4000:102,103,104,105"));
        log.info("测试结果：{} - 5000 策略配置", strategyService.getRandomAwardId(100001L, "5000:102,103,104,105,106,107"));
        log.info("测试结果：{} - 6000 策略配置", strategyService.getRandomAwardId(100001L, "6000:102,103,104,105,106,107,108,109"));
    }


}
