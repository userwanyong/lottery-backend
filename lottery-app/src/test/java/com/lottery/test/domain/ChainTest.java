package com.lottery.test.domain;


import com.alibaba.fastjson.JSON;
import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.service.rule.chain.LogicChain;
import com.lottery.domain.strategy.service.rule.chain.factory.DefaultLogicChainFactory;
import com.lottery.domain.strategy.service.rule.chain.impl.RuleWeightLogicChain;
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
public class ChainTest {

    @Resource
    private RuleWeightLogicChain ruleWeightLogicChain;
    @Resource
    private DefaultLogicChainFactory defaultLogicChainFactory;

    @Before
    public void setUp() {
        // 通过反射 mock 规则中的值
        ReflectionTestUtils.setField(ruleWeightLogicChain, "userScore", 100L);
    }

    @Test
    public void test_LogicChain_rule_blacklist() {
        LogicChain logicChain = defaultLogicChainFactory.openLogicChain(100001L);
        RuleEntity ruleEntity = logicChain.logic("user001", 100001L);
        log.info("测试结果：{}", JSON.toJSONString(ruleEntity));
    }
    @Test
    public void test_LogicChain_rule_weight() {
        LogicChain logicChain = defaultLogicChainFactory.openLogicChain(100001L);
        RuleEntity ruleEntity = logicChain.logic("user000", 100001L);
        log.info("测试结果：{}", JSON.toJSONString(ruleEntity));
    }
    @Test
    public void test_LogicChain_rule_default() {
        LogicChain logicChain = defaultLogicChainFactory.openLogicChain(100001L);
        RuleEntity ruleEntity = logicChain.logic("user000", 100001L);
        log.info("测试结果：{}", JSON.toJSONString(ruleEntity));
    }
}
