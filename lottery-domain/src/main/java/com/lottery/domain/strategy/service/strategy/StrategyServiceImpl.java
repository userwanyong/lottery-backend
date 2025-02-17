package com.lottery.domain.strategy.service.strategy;

import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.model.entity.StrategyAwardEntity;
import com.lottery.domain.strategy.model.entity.StrategyEntity;
import com.lottery.domain.strategy.repository.LotteryRepository;
import com.lottery.types.common.Constants;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

/**
 * @author 永
 * 策略装配实现类，负责初始化策略计算
 */
@Slf4j
@Service
public class StrategyServiceImpl implements StrategyArmory, StrategyService {
    @Resource
    private LotteryRepository repository;

    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        log.info("============抽奖策略装配开始，策略ID：{}============",strategyId);
        // 1. 查询策略配置（该策略对应的奖品）
        List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);
        // 缓存奖品库存
        for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
            Long awardId = strategyAwardEntity.getAwardId();
            Integer awardCount = strategyAwardEntity.getAwardCount();
            cacheStrategyAwardCount(strategyId, awardId, awardCount);
        }
        // 2. 生成并保存概率查找表
        assembleLotteryStrategy(String.valueOf(strategyId), strategyAwardEntities);

        //生成并保存概率查找表+权重的
        // 3. 根据策略id查询策略表，获得策略实体，判断是否存在权重规则
        StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyId(strategyId);
        if (strategyEntity.getRuleModels() == null) {// 未配置任何规则
            log.info("============抽奖策略装配完成，策略ID：{}============",strategyId);
            return true;
        }
        String ruleWeight = strategyEntity.getRuleWeight();
        if (ruleWeight == null) {
            log.info("============抽奖策略装配完成，策略ID：{}============",strategyId);
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
            assembleLotteryStrategy(String.valueOf(strategyId).concat("_").concat(key), strategyAwardEntitiesClone);
        }
        log.info("============抽奖策略装配完成，策略ID：{}============",strategyId);
        return true;
    }

    private void cacheStrategyAwardCount(Long strategyId, Long awardId, Integer awardCount) {
        String key = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY+strategyId+Constants.UNDERLINE+awardId;
        repository.cacheStrategyAwardCount(key, awardCount);
    }


    /**
     * 按需计算数据，生成并保存保存概率查找表
     *
     * @param key                   键
     * @param strategyAwardEntities 策略对应的奖品
     */
    private void assembleLotteryStrategy(String key, List<StrategyAwardEntity> strategyAwardEntities) {
        // 1. 获取最小概率值
        BigDecimal minAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        // 2. 找概率范围值
        BigDecimal rateRange = BigDecimal.valueOf(convert(minAwardRate.doubleValue()));

        // 3. 生成策略奖品概率查找表（在list集合中，存放上对应的奖品占位即可，占位越多等于概率越高）
        List<Long> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());//不用频繁扩容，提高性能
        for (StrategyAwardEntity strategyAward : strategyAwardEntities) {
            Long awardId = strategyAward.getAwardId();
            BigDecimal awardRate = strategyAward.getAwardRate();
            // 计算出每个概率值需要存放到查找表的数量，循环填充
            for (int i = 0; i < rateRange.multiply(awardRate).intValue(); i++) {
                strategyAwardSearchRateTables.add(awardId);
            }
        }

        // 4. 进行乱序操作
        Collections.shuffle(strategyAwardSearchRateTables);

        // 5. 构造Map集合（比如抽到的数是31，就查找31对应的奖品）
        Map<Integer, Long> shuffleStrategyAwardSearchRateTable = new LinkedHashMap<>();
        for (int i = 0; i < strategyAwardSearchRateTables.size(); i++) {
            shuffleStrategyAwardSearchRateTable.put(i, strategyAwardSearchRateTables.get(i));
        }

        // 6. 存放到 Redis
        repository.storeStrategyAwardSearchRateTable(key, shuffleStrategyAwardSearchRateTable.size(), shuffleStrategyAwardSearchRateTable);

    }

    /**
     * 转换计算，只根据小数位来计算。如【0.01返回100】、【0.009返回1000】、【0.0018返回10000】
     */
    private double convert(double min) {
        double current = min;
        double max = 1;
        while (current < 1) {
            current = current * 10;
            max = max * 10;
        }
        return max;
    }

    @Override
    public Long getRandomAwardId(Long strategyId) {
        // 分布式部署下，需要从 Redis 中获取
        // 1、获得数量范围
        int rateRange = repository.getRateRange(String.valueOf(strategyId));
        // 2、生成随机值，获取 概率值奖品查找表 的结果
        return repository.getStrategyAwardAssemble(String.valueOf(strategyId), new SecureRandom().nextInt(rateRange));
    }

    @Override
    public Long getRandomAwardId(Long strategyId, String ruleWeightValue) {
        String key = String.valueOf(strategyId).concat(Constants.UNDERLINE).concat(ruleWeightValue);
        // 1、获得数量范围
        int rateRange = repository.getRateRange(key);
        // 2、生成随机值，获取 概率值奖品查找表 的结果
        return repository.getStrategyAwardAssemble(key, new SecureRandom().nextInt(rateRange));
    }

    @Override
    public Long getRandomAwardId(String key) {
        int rateRange = repository.getRateRange(key);
        return repository.getStrategyAwardAssemble(key, new SecureRandom().nextInt(rateRange));
    }

    @Override
    public Boolean reduceAwardStock(Long strategyId, Long awardId) {
        String key = Constants.RedisKey.STRATEGY_AWARD_COUNT_KEY + strategyId + Constants.UNDERLINE + awardId;
        return repository.reduceAwardStock(key);
    }

}
