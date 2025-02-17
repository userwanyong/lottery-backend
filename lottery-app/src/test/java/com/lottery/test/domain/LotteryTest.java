package com.lottery.test.domain;

import com.alibaba.fastjson.JSON;
import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.LotteryResEntity;
import com.lottery.domain.strategy.service.LotteryStrategy;
import com.lottery.domain.strategy.service.rule.filter.impl.RuleLockLogicFilter;
import com.lottery.domain.strategy.service.rule.filter.impl.RuleWeightLogicFilter;
import com.lottery.domain.strategy.service.rule.tree.impl.RuleLockTreeNode;
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
    private LotteryStrategy lotteryStrategy;
    @Resource
    private RuleWeightLogicFilter ruleWeightLogicFilter;
    @Resource
    private RuleLockTreeNode ruleLockTreeNode;

    @Before
    public void setUp() {
        // 通过反射 mock 规则中的值
        ReflectionTestUtils.setField(ruleWeightLogicFilter, "userScore", 5050L);
        ReflectionTestUtils.setField(ruleLockTreeNode, "userLotteryCount", 10L);
    }


    @Test
    public void test_performLottery_weight() {
        for (int i = 0; i < 10; i++) {
            LotteryReqEntity lotteryReqEntity = LotteryReqEntity.builder()
                    .userId("user000")
                    .strategyId(100001L)
                    .build();
            LotteryResEntity lotteryResEntity = lotteryStrategy.performLottery(lotteryReqEntity);
            log.info("请求参数：{}", JSON.toJSONString(lotteryReqEntity));
            log.info("测试结果：{}", JSON.toJSONString(lotteryResEntity));
        }
    }

    @Test
    public void test_performLottery_blacklist() {
        LotteryReqEntity lotteryReqEntity = LotteryReqEntity.builder()
                .userId("user003")  // 黑名单用户 user001,user002,user003
                .strategyId(100001L)
                .build();
        LotteryResEntity lotteryResEntity = lotteryStrategy.performLottery(lotteryReqEntity);
        log.info("请求参数：{}", JSON.toJSONString(lotteryReqEntity));
        log.info("测试结果：{}", JSON.toJSONString(lotteryResEntity));
    }


    @Test
    public void test_raffle_center_rule_lock(){
        LotteryReqEntity lotteryReqEntity = LotteryReqEntity.builder()
                .userId("user010")
                .strategyId(100006L)
                .build();
        LotteryResEntity raffleAwardEntity = lotteryStrategy.performLottery(lotteryReqEntity);
        log.info("请求参数：{}", JSON.toJSONString(lotteryReqEntity));
        log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
    }

    @Test
    public void test_performLottery() throws InterruptedException {
        for (int i = 0; i < 1; i++) {
            LotteryReqEntity raffleFactorEntity = LotteryReqEntity.builder()
                    .userId("user010")
                    .strategyId(100006L)
                    .build();
            LotteryResEntity raffleAwardEntity = lotteryStrategy.performLottery(raffleFactorEntity);
            log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
            log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
        }
        // 等待 UpdateAwardStockJob 消费队列
        new CountDownLatch(1).await();
    }
}
