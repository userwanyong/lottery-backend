package com.marketing.domain.strategy.service.rule.impl;

import com.marketing.domain.strategy.model.entity.RuleFilterResEntity;
import com.marketing.domain.strategy.model.entity.RuleFilterReqEntity;
import com.marketing.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.marketing.domain.strategy.repository.StrategyRepository;
import com.marketing.domain.strategy.service.annotation.LogicStrategy;
import com.marketing.domain.strategy.service.rule.LogicFilter;
import com.marketing.domain.strategy.service.rule.factory.DefaultLogicFactory;
import com.marketing.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.*;

/**
 * @author 永
 * 抽奖前-根据抽奖权重返回可抽奖范围KEY
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_WIGHT) // 如果存在权重规则，则使用这个过滤器
public class RuleWeightLogicFilter implements LogicFilter<RuleFilterResEntity.LotteryBeforeEntity> {

    @Resource
    private StrategyRepository repository;

    // TODO 后期从数据库查询
    public Long userScore = 4500L;

    /**
     * 权重规则过滤；
     * 1. 权重规则格式；4000:102,103,104,105 5000:102,103,104,105,106,107 6000:102,103,104,105,106,107,108,109
     * 2. 解析数据格式；判断哪个范围符合用户的特定抽奖范围
     *
     * @param ruleFilterReqEntity 规则过滤参数
     * @return 规则过滤结果
     */
    @Override
    public RuleFilterResEntity<RuleFilterResEntity.LotteryBeforeEntity> filter(RuleFilterReqEntity ruleFilterReqEntity) {
        log.info("规则过滤-权重范围 userId:{} strategyId:{} ruleModel:{}", ruleFilterReqEntity.getUserId(), ruleFilterReqEntity.getStrategyId(), ruleFilterReqEntity.getRuleModel());

        String userId = ruleFilterReqEntity.getUserId();
        Long strategyId = ruleFilterReqEntity.getStrategyId();
        String ruleValue = repository.queryStrategyRuleValue(ruleFilterReqEntity.getStrategyId(), ruleFilterReqEntity.getAwardId(), ruleFilterReqEntity.getRuleModel());

        // 1. 处理规则模型的值，如果规则模型没有值，直接放行
        Map<Long, String> analyticalValueGroup = getAnalyticalValue(ruleValue);
        if (analyticalValueGroup == null || analyticalValueGroup.isEmpty()) {
            return RuleFilterResEntity.<RuleFilterResEntity.LotteryBeforeEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .message(RuleLogicCheckTypeVO.ALLOW.getMessage())
                    .build();
        }

        // 2. 转换Keys值，并默认排序
        List<Long> analyticalSortedKeys = new ArrayList<>(analyticalValueGroup.keySet());
        Collections.sort(analyticalSortedKeys);

        // 3. 找出最小符合的值，也就是【4500 积分，能找到 4000:102,103,104,105】、【5000 积分，能找到 5000:102,103,104,105,106,107】
        Long nextValue = analyticalSortedKeys.stream()
                .filter(key -> key <= userScore)
                .reduce((first, second) -> second)// 获取最后一个匹配的键
                .orElse(null);

        // 如果找到，进行接管
        if (nextValue != null) {
            return RuleFilterResEntity.<RuleFilterResEntity.LotteryBeforeEntity>builder()
                    .data(RuleFilterResEntity.LotteryBeforeEntity.builder()
                            .strategyId(strategyId)
                            .ruleWeightValueKey(analyticalValueGroup.get(nextValue))
                            .build())
                    .ruleModel(DefaultLogicFactory.LogicModel.RULE_WIGHT.getCode())
                    .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                    .message(RuleLogicCheckTypeVO.TAKE_OVER.getMessage())
                    .build();
        }

        // 若没找到，放行
        return RuleFilterResEntity.<RuleFilterResEntity.LotteryBeforeEntity>builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .message(RuleLogicCheckTypeVO.ALLOW.getMessage())
                .build();
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
                throw new IllegalArgumentException("rule_weight rule_rule invalid input format" + ruleValueKey);
            }
            ruleValueMap.put(Long.parseLong(parts[0]), ruleValueKey);
        }
        return ruleValueMap;
    }

}

