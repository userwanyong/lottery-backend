package com.marketing.domain.strategy.service;

import com.marketing.domain.strategy.model.entity.StrategyAwardEntity;
import com.marketing.domain.strategy.repository.StrategyRepository;
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
public class StrategyServiceImpl implements StrategyService {
    @Resource
    private StrategyRepository repository;

    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        // 1. 查询策略配置（该策略对应的奖品）
        List<StrategyAwardEntity> strategyAwardEntities = repository.queryStrategyAwardList(strategyId);

        // 2. 获取最小概率值
        BigDecimal minAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        // 3. 获取概率值总和
        BigDecimal totalAwardRate = strategyAwardEntities.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. 概率值总和/最小概率值 获得数量范围（0->保留的小数位数，RoundingMode.CEILING->向上取整）
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);

        // 5. 生成策略奖品概率查找表（在list集合中，存放上对应的奖品占位即可，占位越多等于概率越高）
        List<Long> strategyAwardSearchRateTables = new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAward : strategyAwardEntities) {
            Long awardId = strategyAward.getAwardId();
            BigDecimal awardRate = strategyAward.getAwardRate();
            // 计算出每个概率值需要存放到查找表的数量，循环填充
            for (int i = 0; i < rateRange.multiply(awardRate).divide(BigDecimal.valueOf(100),4,RoundingMode.HALF_UP).setScale(0, RoundingMode.CEILING).intValue(); i++) {
                strategyAwardSearchRateTables.add(awardId);
            }
        }

        // 6. 进行乱序操作
        Collections.shuffle(strategyAwardSearchRateTables);

        // 7. 构造Map集合（比如抽到的数是31，就查找31对应的奖品）
        Map<Integer, Long> shuffleStrategyAwardSearchRateTable = new LinkedHashMap<>();
        for (int i = 0; i < strategyAwardSearchRateTables.size(); i++) {
            shuffleStrategyAwardSearchRateTable.put(i, strategyAwardSearchRateTables.get(i));
        }

        // 8. 存放到 Redis
        repository.storeStrategyAwardSearchRateTable(strategyId, shuffleStrategyAwardSearchRateTable.size(), shuffleStrategyAwardSearchRateTable);

        return true;
    }

    @Override
    public Long getRandomAwardId(Long strategyId) {
        // 分布式部署下，需要从 Redis 中获取
        // 1、获得数量范围
        int rateRange = repository.getRateRange(strategyId);
        // 2、生成随机值，获取 概率值奖品查找表 的结果
        return repository.getStrategyAwardAssemble(strategyId, new SecureRandom().nextInt(rateRange));
    }

}
