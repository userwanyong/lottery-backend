package com.lottery.domain.strategy.service.rule.tree.impl;

import com.lottery.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.lottery.domain.strategy.service.rule.tree.LogicTree;
import com.lottery.domain.strategy.service.rule.tree.factory.DefaultLogicTreeFactory;
import com.lottery.types.common.Constants;
import org.springframework.stereotype.Component;

/**
 * @author 永
 * 规则树-次数锁节点
 */
@Component(Constants.RuleModel.RULE_LOCK)
public class RuleLockTreeNode implements LogicTree {
    @Override
    public DefaultLogicTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Long awardId) {
        return DefaultLogicTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.ALLOW)
                .build();
    }
}
