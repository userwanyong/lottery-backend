package com.lottery.domain.activity.service.quota.rule;

/**
 * @author 永
 */
public interface ActivityChainArmory {
    ActivityChain appendNext(ActivityChain next);
    ActivityChain next();
}
