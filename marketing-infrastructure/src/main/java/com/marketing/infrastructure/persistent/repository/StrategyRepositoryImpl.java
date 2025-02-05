package com.marketing.infrastructure.persistent.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.marketing.domain.strategy.model.entity.StrategyAwardEntity;
import com.marketing.domain.strategy.repository.StrategyRepository;
import com.marketing.infrastructure.persistent.dao.StrategyAwardMapper;
import com.marketing.infrastructure.persistent.po.StrategyAward;
import com.marketing.infrastructure.persistent.redis.RedisService;
import com.marketing.types.common.Constants;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 永
 * 策略服务仓储实现
 */
@Repository
public class StrategyRepositoryImpl implements StrategyRepository {
    @Resource
    private StrategyAwardMapper strategyAwardMapper;
    @Resource
    private RedisService redisService;

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        List<StrategyAwardEntity> strategyAwardEntities;

        // 优先从redis缓存中获取
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId;
        strategyAwardEntities = redisService.getValue(cacheKey);
        if (strategyAwardEntities !=null && !strategyAwardEntities.isEmpty()) {
            return strategyAwardEntities;
        }
        // 否则查询数据库
        LambdaQueryWrapper<StrategyAward> queryWrapper = new QueryWrapper<StrategyAward>()
                .lambda()
                .eq(StrategyAward::getStrategyId, strategyId);
        List<StrategyAward> strategyAwards = strategyAwardMapper.selectList(queryWrapper);
        strategyAwardEntities = new ArrayList<>(strategyAwards.size());

        //StrategyAward->StrategyAwardEntity
        for (StrategyAward strategyAward : strategyAwards) {
            StrategyAwardEntity strategyAwardEntity = new StrategyAwardEntity();
            BeanUtils.copyProperties(strategyAward, strategyAwardEntity);
            strategyAwardEntities.add(strategyAwardEntity);
        }
        //保存到redis中
        redisService.setValue(cacheKey, strategyAwardEntities);
        return strategyAwardEntities;
    }

    @Override
    public void storeStrategyAwardSearchRateTable(Long strategyId, Integer rateRange, Map<Integer, Long> strategyAwardSearchRateTable) {
        // 1. 存储 抽奖策略范围值，如1000，用于生成1000以内的随机数
        redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId, rateRange);
        // 2. 存储 概率查找表
        Map<Integer, Long> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId);
        cacheRateTable.putAll(strategyAwardSearchRateTable);

    }

    @Override
    public Long getStrategyAwardAssemble(Long strategyId, Integer rateKey) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId, rateKey);
    }

    @Override
    public int getRateRange(Long strategyId) {
        return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId);
    }

}
