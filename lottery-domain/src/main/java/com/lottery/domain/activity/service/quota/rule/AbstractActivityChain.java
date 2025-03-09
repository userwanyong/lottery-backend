package com.lottery.domain.activity.service.quota.rule;

/**
 * @author 永
 * 下单责任链
 */
public abstract class AbstractActivityChain implements ActivityChain {

    private ActivityChain next;
    @Override
    public ActivityChain appendNext(ActivityChain next) {
        this.next = next;
        return next;
    }

    @Override
    public ActivityChain next() {
        return next;
    }
}
