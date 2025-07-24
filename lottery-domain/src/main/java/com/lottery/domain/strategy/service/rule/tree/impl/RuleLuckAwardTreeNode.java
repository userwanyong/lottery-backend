package com.lottery.domain.strategy.service.rule.tree.impl;

import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.lottery.domain.strategy.service.rule.tree.LogicTree;
import com.lottery.domain.strategy.service.rule.tree.factory.DefaultLogicTreeFactory;
import com.lottery.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author 永
 * 规则树-幸运（兜底）奖节点
 */
@Component(Constants.RuleModel.RULE_LUCK_AWARD)
@Slf4j
public class RuleLuckAwardTreeNode implements LogicTree {
    @Override
    public DefaultLogicTreeFactory.TreeActionEntity logic(String userId, Long strategyId,Long activityId, Long awardId, String ruleValue) {
        log.info("【规则树 RuleLuckAwardTreeNode-兜底奖节点开始执行】");
        // 返回兜底奖品
        log.info("【规则树 RuleLuckAwardTreeNode】-兜底奖品'谢谢参与' userId:{} strategyId:{}", userId, strategyId);
        return DefaultLogicTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .ruleEntity(RuleEntity.builder()
                        .awardId(0L)
                        .ruleValue("谢谢参与")
                        .build())
                .build();
    }
}
