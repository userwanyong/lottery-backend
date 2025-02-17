package com.lottery.domain.strategy.service.rule.tree;

import com.lottery.domain.strategy.service.rule.tree.factory.DefaultLogicTreeFactory;

/**
 * @author 永
 * 规则树接口
 */
public interface LogicTree {
    DefaultLogicTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Long awardId, String ruleValue);
}
