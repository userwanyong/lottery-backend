package com.marketing.domain.strategy.repository;

import com.marketing.domain.strategy.model.entity.StrategyAwardEntity;
import com.marketing.domain.strategy.model.entity.StrategyEntity;
import com.marketing.domain.strategy.model.entity.StrategyRuleEntity;

import java.util.List;
import java.util.Map;

/**
 * @author 永
 * 策略服务仓储接口
 */
public interface StrategyRepository {

    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchRateTable(String key, Integer rateRange, Map<Integer, Long> strategyAwardSearchRateTable);

    Long getStrategyAwardAssemble(String strategyId, Integer rateKey);

    int getRateRange(String strategyId);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleWeight);

    String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel);
}
