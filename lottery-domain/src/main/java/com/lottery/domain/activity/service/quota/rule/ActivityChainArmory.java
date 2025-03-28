package com.lottery.domain.activity.service.quota.rule;

/**
 * @author 永
 * 活动责任链组装
 */
public interface ActivityChainArmory {
    ActivityChain appendNext(ActivityChain next);
    ActivityChain next();
}
