package com.lottery.domain.strategy.service.rule.chain;

/**
 * @author 永
 * 抽奖规则责任链接口
 */
public interface LogicChain {
    Long chain(String userId, Long strategyId);

    LogicChain appendNext(LogicChain nextChain);

    LogicChain next();

}
