package com.lottery.domain.strategy.service.armory;

/**
 * @author 永
 * 策略-装配领域-调度接口
 */
public interface StrategyService {

    Long getRandomAwardId(Long activityId);

    Long getRandomAwardId(Long activityId, String ruleWeightValue);

    Boolean reduceAwardStock(Long activityId, Long awardId);
}
