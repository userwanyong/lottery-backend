package com.lottery.domain.strategy.service.rule.chain.impl;

import com.lottery.domain.activity.service.ActivityQuotaService;
import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.repository.StrategyRepository;
import com.lottery.domain.strategy.service.armory.StrategyService;
import com.lottery.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.lottery.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 永
 * 抽奖前-权重责任链
 */
@Slf4j
@Component(Constants.RuleModel.RULE_WIGHT)
public class RuleWeightLogicChain extends AbstractLogicChain {

    @Resource
    private StrategyRepository repository;

    @Resource
    private StrategyService strategyService;

    @Resource
    private ActivityQuotaService activityQuotaService;

    @Override
    public RuleEntity logic(String userId, Long strategyId, Long activityId) {
        log.info("【抽奖责任链-RuleWeightLogicChain】-权重开始 userId:{} strategyId:{} activityId:{}", userId, strategyId, activityId);

        String ruleValue = repository.queryStrategyRuleValue(strategyId, Constants.RuleModel.RULE_WIGHT);

        Integer userScore = activityQuotaService.queryTotalUserLotteryCount(userId, activityId);

        // 1. 处理规则模型的值，如果规则模型没有值，直接放行
        Map<Long, String> analyticalValueGroup = getAnalyticalValue(ruleValue);
        if (analyticalValueGroup == null || analyticalValueGroup.isEmpty()) {
            return next().logic(userId, strategyId,activityId);
        }

        // 2. 转换Keys值，并默认排序
        List<Long> analyticalSortedKeys = new ArrayList<>(analyticalValueGroup.keySet());
        Collections.sort(analyticalSortedKeys);

        Long nextValue = null;
        for (Long analyticalSortedKey : analyticalSortedKeys) {
            if (Long.valueOf(userScore).equals(analyticalSortedKey)) {
                nextValue = analyticalSortedKey;
                break;
            }
        }

        if (nextValue != null) {
            Long awardId = strategyService.getRandomAwardId(activityId, analyticalValueGroup.get(nextValue));
            log.info("【抽奖责任链-RuleWeightLogicChain】-权重接管 userId:{} strategyId:{} activityId:{} awardId:{}", userId, strategyId, activityId, awardId);
            return RuleEntity.builder()
                    .awardId(awardId)
                    .ruleModel(Constants.RuleModel.RULE_WIGHT)
                    .build();
        }

        // 否则过滤其他责任链
        log.info("【抽奖责任链-RuleWeightLogicChain】-权重放行 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, Constants.RuleModel.RULE_WIGHT);
        return next().logic(userId, strategyId,activityId);
    }

    private Map<Long, String> getAnalyticalValue(String ruleValue) {
        String[] ruleValueGroups = ruleValue.split(Constants.SPACE);
        Map<Long, String> ruleValueMap = new HashMap<>();
        for (String ruleValueKey : ruleValueGroups) {
            // 检查输入是否为空
            if (ruleValueKey == null || ruleValueKey.isEmpty()) {
                return ruleValueMap;
            }
            // 分割字符串以获取键和值
            String[] parts = ruleValueKey.split(Constants.COLON);
            if (parts.length != 2) {
                log.error("[RuleWeightLogicChain] rule_weight invalid input format {}", ruleValueKey);
                throw new IllegalArgumentException("rule_weight invalid input format " + ruleValueKey);
            }
            ruleValueMap.put(Long.parseLong(parts[0]), ruleValueKey);
        }
        return ruleValueMap;
    }
}
