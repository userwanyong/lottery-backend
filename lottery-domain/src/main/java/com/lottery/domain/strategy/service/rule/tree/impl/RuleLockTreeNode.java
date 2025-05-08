package com.lottery.domain.strategy.service.rule.tree.impl;

import com.lottery.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.lottery.domain.strategy.repository.StrategyRepository;
import com.lottery.domain.strategy.service.rule.tree.LogicTree;
import com.lottery.domain.strategy.service.rule.tree.factory.DefaultLogicTreeFactory;
import com.lottery.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 永
 * 规则树-次数锁节点
 */
@Component(Constants.RuleModel.RULE_LOCK)
@Slf4j
public class RuleLockTreeNode implements LogicTree {

    @Resource
    private StrategyRepository repository;
    @Override
    public DefaultLogicTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Long awardId, String ruleValue) {
        log.debug("【规则树 RuleLockTreeNode-次数锁节点开始执行】");
        // 判断用户抽奖次数
        // 查询用户抽奖次数 - 当天的；策略ID:活动ID 1:1 的配置，可以直接用 strategyId 查询。
        Integer userLotteryCount = repository.queryTodayUserLotteryCount(userId, strategyId);
        // 小于等于该值，拦截
        if (userLotteryCount < Long.parseLong(ruleValue)) {
            log.debug("【规则树 RuleLockTreeNode】-次数锁拦截 userId:{} strategyId:{} ruleModel:{} awardId: {}", userId, strategyId, Constants.RuleModel.RULE_LOCK,101);
            return DefaultLogicTreeFactory.TreeActionEntity.builder()
                    .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                    .build();
        }
        // 大于等于该值，放行
        log.debug("【规则树 RuleLockTreeNode】-次数锁放行 userId:{} strategyId:{} ruleModel:{}", userId, strategyId, Constants.RuleModel.RULE_LOCK);
        return DefaultLogicTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.ALLOW)
                .build();
    }
}
