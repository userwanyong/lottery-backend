package com.lottery.test.domain;

import com.alibaba.fastjson.JSON;
import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.LotteryResEntity;
import com.lottery.domain.strategy.service.lottery.Lottery;
import com.lottery.domain.strategy.service.rule.chain.impl.RuleWeightLogicChain;
import com.lottery.domain.strategy.service.rule.tree.impl.RuleLockTreeNode;
import com.lottery.domain.strategy.service.strategy.StrategyArmory;
import com.lottery.infrastructure.persistent.redis.RedisServiceImpl;
import com.lottery.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class LotteryTest {
    @Resource
    private Lottery lottery;
    @Resource
    private RuleWeightLogicChain ruleWeightLogicChain;
    @Resource
    private RuleLockTreeNode ruleLockTreeNode;
    @Resource
    private RedisServiceImpl redisService;
    @Resource
    private StrategyArmory strategyArmory;

    @Before
    public void setUp() {
        // 通过反射 mock 规则中的值
        ReflectionTestUtils.setField(ruleWeightLogicChain, "userScore", 5050L);
        ReflectionTestUtils.setField(ruleLockTreeNode, "userLotteryCount", 10L);
    }

    /**
     * 抽奖策略装配
     */
    @Test
    public void test_strategyArmory() {
        // 因为实际操作是更新数据库时，更新redis。这里装配抽奖策略时有部分参数是从原先redis缓存中获取的，测试先手动清空redis便于调试
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_KEY + 100006;
        String cacheKey2 = Constants.RedisKey.STRATEGY_KEY + 100006;
        String cacheKey3 = Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + 100006;
        redisService.remove(cacheKey);
        redisService.remove(cacheKey2);
        redisService.remove(cacheKey3);
        boolean success4 = strategyArmory.assembleLotteryStrategy(100006L);
        log.info("测试结果4：{}", success4);
    }

    /**
     * 执行抽奖
     */
    @Test
    public void test_lottery() throws InterruptedException {
        for (int i = 0; i < 1; i++) {
            LotteryReqEntity lotteryReqEntity = LotteryReqEntity.builder()
                    .userId("user010")
                    .strategyId(100006L)
                    .build();
            LotteryResEntity lotteryResEntity = lottery.performLottery(lotteryReqEntity);
            log.info("请求参数：{}", JSON.toJSONString(lotteryReqEntity));
            log.info("测试结果：{}", JSON.toJSONString(lotteryResEntity));
        }
        // 等待 UpdateAwardStockJob 消费队列
        new CountDownLatch(1).await();
    }
}
