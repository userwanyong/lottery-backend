package com.lottery.domain.strategy.service.armory.algorithm;

import com.lottery.domain.strategy.model.entity.StrategyAwardEntity;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 永
 * 装配/抽奖算法接口
 */
public interface Algorithm {

    void armoryAlgorithm(String key, List<StrategyAwardEntity> strategyAwardEntities, BigDecimal rateRange);

    Long dispatchAlgorithm(String key);
}
