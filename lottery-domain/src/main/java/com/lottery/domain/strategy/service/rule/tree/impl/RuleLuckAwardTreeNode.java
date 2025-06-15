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
        log.debug("【规则树 RuleLuckAwardTreeNode-兜底奖节点开始执行】");
        String[] split = ruleValue.split(Constants.COLON);
        if (split.length == 0) {
            log.error("【规则树 RuleLuckAwardTreeNode】-兜底奖品，兜底奖品未配置 userId:{} strategyId:{} awardId:{}", userId, strategyId, awardId);
            throw new RuntimeException("【规则树 RuleLuckAwardTreeNode】-兜底奖品，兜底奖品未配置 " + ruleValue);
        }
        // 获取兜底奖励
        Long luckAwardId = Long.valueOf(split[0]);
        String awardRuleValue = split.length > 1 ? split[1] : "";
        // 返回兜底奖品
        log.debug("【规则树 RuleLuckAwardTreeNode】-兜底奖品 userId:{} strategyId:{} awardId:{} awardRuleValue:{}", userId, strategyId, luckAwardId, awardRuleValue);
        return DefaultLogicTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .ruleEntity(RuleEntity.builder()
                        .awardId(luckAwardId)
                        .ruleValue(awardRuleValue)
                        .build())
                .build();
    }
}
