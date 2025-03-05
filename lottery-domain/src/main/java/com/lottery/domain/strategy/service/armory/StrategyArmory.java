package com.lottery.domain.strategy.service.armory;

/**
 * @author 永
 * 策略装配接口
 */
public interface StrategyArmory {

    /**
     * 装配抽奖策略「触发的时机可以为活动审核通过后进行调用」
     *
     * @param strategyId 策略ID
     * @return 装配结果
     */
    boolean assembleLotteryStrategy(Long strategyId);

}
