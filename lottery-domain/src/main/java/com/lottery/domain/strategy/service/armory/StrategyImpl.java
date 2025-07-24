package com.lottery.domain.strategy.service.armory;

import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.model.entity.StrategyAwardEntity;
import com.lottery.domain.strategy.model.entity.StrategyEntity;
import com.lottery.domain.strategy.service.armory.algorithm.Algorithm;
import com.lottery.types.common.Constants;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 永
 * 策略-装配领域
 */
@Slf4j
@Service
public class StrategyImpl extends AbstractStrategy {

    // 抽奖策略算法
    private final Map<String, Algorithm> algorithmMap;

    // 抽奖算法阈值，在多少范围内开始选择不同选择
    private final Integer ALGORITHM_THRESHOLD_VALUE = 5000;

    public StrategyImpl(Map<String, Algorithm> algorithmMap) {
        this.algorithmMap = algorithmMap;
    }


    /**
     * 按需计算数据，生成并保存保存概率查找表
     *
     * @param key                   键
     * @param strategyAwardEntities 策略对应的奖品
     */
    @Override
    protected void armoryAlgorithm(String key, List<StrategyAwardEntity> strategyAwardEntities) {
        // 1. 获取最小概率值，如果最小概率为0，则排除继续查找下一个最小概率值
        BigDecimal minAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .filter(rate -> rate.compareTo(BigDecimal.ZERO) > 0)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);
        if (minAwardRate.compareTo(BigDecimal.ZERO) <= 0){
            return;
        }
        // 2. 找概率范围值
        Integer rateRange = convert(minAwardRate.doubleValue());

        // 3. 根据概率值范围选择算法
        if (rateRange <= ALGORITHM_THRESHOLD_VALUE) {
            Algorithm o1Algorithm = algorithmMap.get(Constants.Algorithm.O1);
            o1Algorithm.armoryAlgorithm(key, strategyAwardEntities, new BigDecimal(rateRange));
            repository.cacheStrategyArmoryAlgorithm(key, Constants.Algorithm.O1);
        } else {
            Algorithm oLogNAlgorithm = algorithmMap.get(Constants.Algorithm.OLogN);
            oLogNAlgorithm.armoryAlgorithm(key, strategyAwardEntities, new BigDecimal(rateRange));
            repository.cacheStrategyArmoryAlgorithm(key, Constants.Algorithm.OLogN);
        }

    }

    @Override
    protected Long dispatchAlgorithm(String key) {
        String name = repository.queryStrategyArmoryAlgorithmFromCache(key);
        if (name == null) {
            throw new RuntimeException("抽奖算法未配置");
        }
        Algorithm algorithm = algorithmMap.get(name);
        return algorithm.dispatchAlgorithm(key);
    }

}
