package com.lottery.domain.strategy.service.rule.chain;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 永
 * 抽奖策略责任链，判断走那种抽奖策略。如；黑名单抽奖、权重抽奖、默认抽象
 */
@Slf4j
public abstract class AbstractLogicChain implements LogicChain {
    private LogicChain next;

    @Override
    public LogicChain next() {
        return next;
    }

    @Override
    public LogicChain appendNext(LogicChain next) {
        this.next = next;
        return next;
    }
}
