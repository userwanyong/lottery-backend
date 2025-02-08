package com.marketing.domain.strategy.service.strategy;

/**
 * @author 永
 * 策略调度接口
 */
public interface StrategyService {

    /**
     * 获取抽奖策略装配的随机结果
     * @param strategyId 策略ID
     * @return 抽奖结果
     */
    Long getRandomAwardId(Long strategyId);

    /**
     * 获取抽奖策略装配的随机结果
     * @param strategyId 策略ID+权重值
     * @return 抽奖结果
     */
    Long getRandomAwardId(Long strategyId, String ruleWeightValue);

}
