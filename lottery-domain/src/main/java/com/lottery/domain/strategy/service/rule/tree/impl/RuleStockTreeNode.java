package com.lottery.domain.strategy.service.rule.tree.impl;

import com.lottery.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.lottery.domain.strategy.service.rule.tree.LogicTree;
import com.lottery.domain.strategy.service.rule.tree.factory.DefaultLogicTreeFactory;
import com.lottery.types.common.Constants;
import org.springframework.stereotype.Component;

/**
 * @author 永
 * 规则树-库存节点
 */
@Component(Constants.RuleModel.RULE_STOCK)
public class RuleStockTreeNode implements LogicTree {
    @Override
    public DefaultLogicTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId) {
        return DefaultLogicTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .build();
    }
}
