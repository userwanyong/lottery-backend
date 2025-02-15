package com.lottery.domain.strategy.service.rule.tree.factory.engine;

import com.lottery.domain.strategy.model.entity.RuleEntity;

/**
 * @author 永
 * 组合规则树接口（引擎）
 */
public interface DecisionTreeEngine {
    RuleEntity process(String userId, Long strategyId, Integer awardId);
}
