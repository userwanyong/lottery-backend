package com.lottery.domain.strategy.service.armory;

/**
 * @author 永
 * 策略-装配领域-调度接口
 */
public interface StrategyService {

    /**
     * 获取抽奖策略装配的随机结果
     *
     * @param strategyId 策略ID
     * @return 抽奖结果
     */
    Long getRandomAwardId(Long strategyId);

    /**
     * 获取抽奖策略装配的随机结果
     *
     * @param strategyId 策略ID+权重值
     * @return 抽奖结果
     */
    Long getRandomAwardId(Long strategyId, String ruleWeightValue);

    /**
     * 获取抽奖策略装配的随机结果
     *
     * @param key = strategyId + _ + ruleWeightValue；
     * @return 抽奖结果
     */
//    Long getRandomAwardId(String key);

    /**
     * 根据策略ID和奖品ID，扣减奖品缓存库存
     *
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     * @return 扣减结果
     */
    Boolean reduceAwardStock(Long strategyId, Long activityId,Long awardId);

}
