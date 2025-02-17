package com.lottery.domain.strategy.service.rule.tree.impl;

import com.lottery.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.lottery.domain.strategy.service.rule.tree.LogicTree;
import com.lottery.domain.strategy.service.rule.tree.factory.DefaultLogicTreeFactory;
import com.lottery.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author 永
 * 规则树-次数锁节点
 */
@Component(Constants.RuleModel.RULE_LOCK)
@Slf4j
public class RuleLockTreeNode implements LogicTree {

    // TODO 用户抽奖次数，后期从数据库查询
    private Long userLotteryCount = 0L;
    @Override
    public DefaultLogicTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Long awardId, String ruleValue) {
        log.info("【规则树-次数锁节点开始执行】");
        // 判断用户抽奖次数
        // 小于等于该值，拦截
        if (userLotteryCount < Long.parseLong(ruleValue)) {
            log.info("规则树-次数锁拦截 userId:{} strategyId:{} ruleModel:{} awardId: {}", userId, strategyId, Constants.RuleModel.RULE_LOCK,101);
            return DefaultLogicTreeFactory.TreeActionEntity.builder()
                    .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                    .build();
        }
        // 大于等于该值，放行
        log.info("规则树-次数锁放行 userId:{} strategyId:{} ruleModel:{}", userId, strategyId, Constants.RuleModel.RULE_LOCK);
        return DefaultLogicTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.ALLOW)
                .build();
    }
}
