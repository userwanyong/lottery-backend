package com.lottery.domain.strategy.service.rule.tree.impl;

import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.lottery.domain.strategy.service.rule.tree.LogicTree;
import com.lottery.domain.strategy.service.rule.tree.factory.DefaultLogicTreeFactory;
import com.lottery.types.common.Constants;
import org.springframework.stereotype.Component;

/**
 * @author 永
 * 规则树-幸运（兜底）奖节点
 */
@Component(Constants.RuleModel.RULE_LUCK_AWARD)
public class RuleLuckAwardTreeNode implements LogicTree {
    @Override
    public DefaultLogicTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Long awardId) {
        return DefaultLogicTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .ruleEntity(RuleEntity.builder()
                        .awardId(101L)
                        .ruleValue("1,100")
                        .build())
                .build();
    }
}
