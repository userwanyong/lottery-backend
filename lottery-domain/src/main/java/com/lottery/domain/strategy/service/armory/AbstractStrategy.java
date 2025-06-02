package com.lottery.domain.strategy.service.armory;


import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.model.entity.StrategyAwardEntity;
import com.lottery.domain.strategy.model.entity.StrategyEntity;
import com.lottery.domain.strategy.repository.StrategyRepository;
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

    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        // 1. 查询策略配置（该策略对应的奖品）
        List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);
        // 缓存奖品库存
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Long awardId = strategyAwardEntity.getAwardId();
            Integer awardCount = strategyAwardEntity.getAwardCountSurplus();
            cacheStrategyAwardCount(strategyId, awardId, awardCount);
        }
        // 2. 生成并保存概率查找表
        armoryAlgorithm(String.valueOf(strategyId), strategyAwardEntities);

        //生成并保存概率查找表+权重的
        // 3. 根据策略id查询策略表，获得策略实体，判断是否存在权重规则
        StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyId(strategyId);
        if (strategyEntity.getRuleModels() == null) {
            // 未配置任何规则
            return true;
        }
        String ruleWeight = strategyEntity.getRuleWeight();
        if (ruleWeight == null) {
            return true;
        }
        // 4. 根据策略id和规则模型查询规则表，获得相应实体数据
        RuleEntity ruleEntity = repository.queryStrategyRule(strategyId, ruleWeight);
        if (ruleEntity == null) {
            throw new AppException(ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getCode(), ResponseCode.STRATEGY_RULE_WEIGHT_IS_NULL.getMessage());
        }
        // 5. 通过实体数据的权重rule_weight 查询对应的值，封装为一个map集合
        Map<String, List<Long>> ruleWeightValuesMap = ruleEntity.getRuleWeightValues();
        Set<String> keys = ruleWeightValuesMap.keySet();
        for (String key : keys) {
            // 5.1. 获取每一个键对应的值集合
            List<Long> ruleWeightValues = ruleWeightValuesMap.get(key);
            // 5.2. 从概率表中移除不存在的奖品id，记得要保留原集合供后续遍历使用
            ArrayList<StrategyAwardEntity> strategyAwardEntitiesClone = new ArrayList<>(strategyAwardEntities);
            strategyAwardEntitiesClone.removeIf(entity -> !ruleWeightValues.contains(entity.getAwardId()));
            // 5.3. 生成并保存概率查找表+权重的
            armoryAlgorithm(String.valueOf(strategyId).concat("_").concat(key), strategyAwardEntitiesClone);
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


    @Override
    public boolean assembleLotteryStrategyByActivityId(Long activityId) {
        Long strategyId = repository.queryStrategyIdByActivityId(activityId);
        return assembleLotteryStrategy(strategyId);
    }

    private void cacheStrategyAwardCount(Long strategyId, Long awardId, Integer awardCount) {
        String key = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Constants.UNDERLINE + awardId;
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
    public Long getRandomAwardId(Long strategyId) {
        return dispatchAlgorithm(String.valueOf(strategyId));
    }

    @Override
    public Long getRandomAwardId(Long strategyId, String ruleWeightValue) {
        String key = String.valueOf(strategyId).concat(Constants.UNDERLINE).concat(ruleWeightValue);
        return dispatchAlgorithm(key);
    }

    @Override
    public Long getRandomAwardId(String key) {
        return dispatchAlgorithm(key);
    }

    @Override
    public Boolean reduceAwardStock(Long strategyId, Long awardId) {
        String key = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Constants.UNDERLINE + awardId;
        return repository.reduceAwardStock(key, strategyId);
    }
}
