package com.lottery.domain.strategy.service.rule.chain;

import com.lottery.domain.strategy.model.entity.RuleEntity;

/**
 * @author 永
 * 抽奖规则责任链接口
 */
public interface LogicChain {
    RuleEntity logic(String userId, Long strategyId,Long activityId);

    LogicChain appendNext(LogicChain nextChain);

    LogicChain next();

}
