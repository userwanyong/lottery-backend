package com.lottery.test.domain;

import com.alibaba.fastjson.JSON;
import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.LotteryResEntity;
import com.lottery.domain.strategy.service.LotteryStrategy;
import com.lottery.domain.strategy.service.rule.impl.RuleWeightLogicFilter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class LotteryTest {
    @Resource
    private LotteryStrategy lotteryStrategy;
    @Resource
    private RuleWeightLogicFilter ruleWeightLogicFilter;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(ruleWeightLogicFilter, "userScore", 5050L);
    }

    @Test
    public void test_performLottery_weight() {
        for (int i = 0; i < 10; i++) {
            LotteryReqEntity lotteryReqEntity = LotteryReqEntity.builder()
                    .userId("user000")
                    .strategyId(100001L)
                    .build();
            LotteryResEntity lotteryResEntity = lotteryStrategy.performRaffle(lotteryReqEntity);
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
        LotteryResEntity lotteryResEntity = lotteryStrategy.performRaffle(lotteryReqEntity);
        log.info("请求参数：{}", JSON.toJSONString(lotteryReqEntity));
        log.info("测试结果：{}", JSON.toJSONString(lotteryResEntity));
    }
}
