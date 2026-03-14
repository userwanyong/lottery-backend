package com.lottery.domain.strategy.repository;

import com.lottery.domain.strategy.event.SendLotteryMessageEvent;
import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.model.entity.StrategyAwardEntity;
import com.lottery.domain.strategy.model.entity.StrategyEntity;
import com.lottery.domain.strategy.model.valobj.RuleTreeVO;
import com.lottery.domain.strategy.model.valobj.RuleWeightVO;
import com.lottery.types.event.BaseEvent;

import java.util.List;
import java.util.Map;

/**
 * @author 永
 * 策略领域仓储接口
 */
public interface StrategyRepository {

    List<StrategyAwardEntity> queryActivityAwardList(Long activityId);

    <K, V> void storeStrategyAwardSearchRateTable(String key, Integer rateRange, Map<K, V> strategyAwardSearchRateTable);

    Long getStrategyAwardAssemble(String strategyId, Integer rateKey);

    int getRateRange(String strategyId);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    RuleEntity queryStrategyRule(String ruleWeight);

    String queryStrategyRuleValue(Long strategyId, Long awardId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, String ruleModel);

    Long queryRuleModelVO(Long activityId, Long awardId);

    RuleTreeVO queryRuleTreeVO(Long treeId);

    Boolean reduceAwardStock(String key, Long activityId);

    void awardStockConsumeSendQueue(LotteryReqEntity lotteryReqEntity);

    LotteryReqEntity takeQueueValue(String activityAward);

    void updateActivityAwardStock(Long activityId, Long awardId);

    void cacheStrategyAwardCount(String key, Integer awardCount);

    StrategyAwardEntity queryActivityAwardEntity(Long activityId, Long awardId);

    Long queryStrategyIdByActivityId(Long activityId);

    Integer queryTodayUserLotteryCount(String userId, Long strategyId, Long activityId);

    Map<Long, Integer> queryAwardRuleLockCount(Long[] treeIds);

    List<String> getActivityAwardList();

    void clearAwardStock(String activityAward);

    void clearQueueValue(String activityAward);

    List<RuleWeightVO> queryStrategyRuleWeight(Long activityId);

    void cacheStrategyArmoryAlgorithm(String key, String name);

    String queryStrategyArmoryAlgorithmFromCache(String key);

    <K, V> Map<K, V> getMap(String key);

    void sendLotteryMessageToMq(String topic, BaseEvent.EventMessage<SendLotteryMessageEvent.LotteryMessage> eventMessage);

    String queryRuleValue(Long awardId);

    void deleteCacheKeyByStrategyId(Long strategyId);

    void deleteCacheKeyByActivityId(Long activityId);

    void deleteCacheKeyByTreeId(Long treeId);

    List<Long> queryActivityIdsByStrategyId(Long strategyId);
}
