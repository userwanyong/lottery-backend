package com.lottery.domain.strategy.repository;

import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.model.entity.StrategyAwardEntity;
import com.lottery.domain.strategy.model.entity.StrategyEntity;
import com.lottery.domain.strategy.model.valobj.RuleTreeVO;
import com.lottery.domain.strategy.model.valobj.RuleWeightVO;
import com.lottery.domain.strategy.model.valobj.StrategyRuleModelVO;

import java.util.List;
import java.util.Map;

/**
 * @author 永
 * 策略领域仓储接口
 */
public interface StrategyRepository {

    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    <K, V> void storeStrategyAwardSearchRateTable(String key, Integer rateRange, Map<K, V> strategyAwardSearchRateTable);

    Long getStrategyAwardAssemble(String strategyId, Integer rateKey);

    int getRateRange(String strategyId);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    RuleEntity queryStrategyRule(String ruleWeight);

    String queryStrategyRuleValue(Long strategyId, Long awardId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, String ruleModel);

    StrategyRuleModelVO queryRuleModelVO(Long strategyId, Long awardId);

    /**
     * 构建规则树树根
     *
     * @param treeId 规则树ID
     * @return 规则树树根VO
     */
    RuleTreeVO queryRuleTreeVO(Long treeId);

    Boolean reduceAwardStock(String key,Long strategyId,Long activityId);

    void awardStockConsumeSendQueue(LotteryReqEntity lotteryReqEntity);

    LotteryReqEntity takeQueueValue(String strategyAward);

    void updateStrategyAwardStock(Long strategyId, Long awardId);

    void cacheStrategyAwardCount(String key, Integer awardCount);

    StrategyAwardEntity queryStrategyAwardEntity(Long strategyId, Long awardId);

    Long queryStrategyIdByActivityId(Long activityId);

    Integer queryTodayUserLotteryCount(String userId, Long strategyId,Long activityId);

    Map<Long, Integer> queryAwardRuleLockCount(Long[] treeIds);

    List<String> getStrategyAwardList();

    void clearAwardStock(String strategyAward);

    void clearQueueValue(String strategyAward);

    List<RuleWeightVO> queryStrategyRuleWeight(String userId, Long activityId);

    void cacheStrategyArmoryAlgorithm(String key, String name);

    String queryStrategyArmoryAlgorithmFromCache(String key);

    <K, V> Map<K, V> getMap(String key);
}
