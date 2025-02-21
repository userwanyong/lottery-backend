package com.lottery.domain.strategy.repository;

import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.StrategyAwardEntity;
import com.lottery.domain.strategy.model.entity.StrategyEntity;
import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.model.valobj.RuleTreeVO;
import com.lottery.domain.strategy.model.valobj.StrategyRuleModelVO;

import java.util.List;
import java.util.Map;

/**
 * @author 永
 * 策略服务仓储接口
 */
public interface LotteryRepository {

    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchRateTable(String key, Integer rateRange, Map<Integer, Long> strategyAwardSearchRateTable);

    Long getStrategyAwardAssemble(String strategyId, Integer rateKey);

    int getRateRange(String strategyId);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    RuleEntity queryStrategyRule(Long strategyId, String ruleWeight);

    String queryStrategyRuleValue(Long strategyId, Long awardId, String ruleModel);
    String queryStrategyRuleValue(Long strategyId, String ruleModel);

    StrategyRuleModelVO queryRuleModelVO(Long strategyId, Long awardId);

    /**
     * 构建规则树
     * @param treeId 规则树ID
     * @return 规则树VO
     */
    RuleTreeVO queryRuleTreeVO(String treeId);

    Boolean reduceAwardStock(String key);

    void awardStockConsumeSendQueue(LotteryReqEntity lotteryReqEntity);

    LotteryReqEntity takeQueueValue();

    void updateStrategyAwardStock(Long strategyId, Long awardId);

    void cacheStrategyAwardCount(String key, Integer awardCount);

    StrategyAwardEntity queryStrategyAwardEntity(Long strategyId, Long awardId);
}
