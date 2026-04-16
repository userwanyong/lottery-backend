package com.lottery.domain.strategy.service.armory;

import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.model.entity.StrategyAwardEntity;
import com.lottery.domain.strategy.model.entity.StrategyEntity;
import com.lottery.domain.strategy.repository.StrategyRepository;
import com.lottery.domain.strategy.service.rule.chain.factory.DefaultLogicChainFactory;
import com.lottery.domain.strategy.service.rule.tree.factory.DefaultLogicTreeFactory;
import com.lottery.types.common.Constants;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 永
 * 装配算法抽象类
 */
public abstract class AbstractStrategy implements StrategyArmory, StrategyService {
    @Resource
    protected StrategyRepository repository;

    @Resource
    private DefaultLogicChainFactory defaultLogicChainFactory;

    @Resource
    private DefaultLogicTreeFactory defaultLogicTreeFactory;

    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        List<Long> activityIds = repository.queryActivityIdsByStrategyId(strategyId);
        if (activityIds == null || activityIds.isEmpty()) {
            return true;
        }
        for (Long activityId : activityIds) {
            assembleLotteryStrategyByActivityId(activityId);
        }
        return true;
    }

    @Override
    public boolean assembleLotteryStrategyByActivityId(Long activityId) {
        repository.deleteCacheKeyByActivityId(activityId);
        Long strategyId = repository.queryStrategyIdByActivityId(activityId);
        if (strategyId == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "activity strategy is not configured");
        }

        // 清理责任链JVM缓存
        defaultLogicChainFactory.clearChainCache(strategyId);

        List<StrategyAwardEntity> strategyAwardEntities = repository.queryActivityAwardList(activityId);
        if (strategyAwardEntities == null || strategyAwardEntities.isEmpty()) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), "activity award is not configured");
        }

        List<Long> treeIds = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getRuleTreeId)
                .filter(treeId -> treeId != null && treeId > 0)
                .distinct()
                .toList();
        for (Long treeId : treeIds) {
            repository.deleteCacheKeyByTreeId(treeId);
            defaultLogicTreeFactory.clearEngineCache(treeId);
            repository.queryRuleTreeVO(treeId);
        }

        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Long awardId = strategyAwardEntity.getAwardId();
            Integer awardCount = strategyAwardEntity.getAwardCountSurplus();
            cacheActivityAwardCount(activityId, awardId, awardCount);
            repository.queryActivityAwardEntity(activityId, awardId);
        }

        armoryAlgorithm(String.valueOf(activityId), strategyAwardEntities);

        StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyId(strategyId);
        if (strategyEntity.getRuleModels() == null || strategyEntity.getRuleWeight() == null) {
            return true;
        }

        RuleEntity ruleEntity = repository.queryStrategyRule(strategyEntity.getRuleWeight());
        if (ruleEntity == null) {
            throw new AppException(
                    ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getCode(),
                    ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getMessage()
            );
        }

        Map<String, List<Long>> ruleWeightValuesMap = ruleEntity.getRuleWeightValues();
        Set<String> keys = ruleWeightValuesMap.keySet();
        for (String key : keys) {
            List<Long> ruleWeightValues = ruleWeightValuesMap.get(key);
            ArrayList<StrategyAwardEntity> strategyAwardEntitiesClone = new ArrayList<>(strategyAwardEntities);
            strategyAwardEntitiesClone.removeIf(entity -> !ruleWeightValues.contains(entity.getAwardId()));
            String newKey = key.split(Constants.COLON)[0];
            armoryAlgorithm(activityId + Constants.UNDERLINE + newKey, strategyAwardEntitiesClone);
        }
        return true;
    }

    /**
     * 装配算法
     */
    protected abstract void armoryAlgorithm(String key, List<StrategyAwardEntity> strategyAwardEntities);

    /**
     * 抽奖算法
     */
    protected abstract Long dispatchAlgorithm(String key);

    protected void cacheActivityAwardCount(Long activityId, Long awardId, Integer awardCount) {
        String key = Constants.RedisKey.ACTIVITY_AWARD_COUNT_KEY + activityId + Constants.UNDERLINE + awardId;
        repository.cacheStrategyAwardCount(key, awardCount);
    }

    /**
     * 转换计算，只根据小数位来计算。如【0.01返回100】、【0.009返回1000】、【0.0018返回10000】
     */
    protected Integer convert(double min) {
        double current = min;
        int max = 1;
        while (current < 1) {
            current = current * 10;
            max = max * 10;
        }
        return max;
    }

    @Override
    public Long getRandomAwardId(Long activityId) {
        return dispatchAlgorithm(String.valueOf(activityId));
    }

    @Override
    public Long getRandomAwardId(Long activityId, String ruleWeightValue) {
        String newRuleWeightValue = ruleWeightValue.split(Constants.COLON)[0];
        String key = activityId + Constants.UNDERLINE + newRuleWeightValue;
        return dispatchAlgorithm(key);
    }

    @Override
    public Boolean reduceAwardStock(Long activityId, Long awardId) {
        String key = Constants.RedisKey.ACTIVITY_AWARD_COUNT_KEY + activityId + Constants.UNDERLINE + awardId;
        return repository.reduceAwardStock(key, activityId);
    }
}
