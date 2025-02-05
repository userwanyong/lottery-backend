package com.marketing.domain.strategy.repository;

import com.marketing.domain.strategy.model.entity.StrategyAwardEntity;
import java.util.List;
import java.util.Map;

/**
 * @author 永
 * 策略服务仓储接口
 */
public interface StrategyRepository {

    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchRateTable(Long strategyId, Integer rateRange, Map<Integer, Long> strategyAwardSearchRateTable);

    Long getStrategyAwardAssemble(Long strategyId, Integer rateKey);

    int getRateRange(Long strategyId);

}
