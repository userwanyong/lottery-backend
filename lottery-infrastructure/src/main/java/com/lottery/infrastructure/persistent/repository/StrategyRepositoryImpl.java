package com.lottery.infrastructure.persistent.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lottery.domain.strategy.model.entity.StrategyAwardEntity;
import com.lottery.domain.strategy.model.entity.StrategyEntity;
import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.repository.StrategyRepository;
import com.lottery.infrastructure.persistent.dao.RuleMapper;
import com.lottery.infrastructure.persistent.dao.StrategyAwardMapper;
import com.lottery.infrastructure.persistent.dao.StrategyMapper;
import com.lottery.infrastructure.persistent.po.Rule;
import com.lottery.infrastructure.persistent.po.Strategy;
import com.lottery.infrastructure.persistent.po.StrategyAward;
import com.lottery.infrastructure.persistent.redis.RedisService;
import com.lottery.types.common.Constants;
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
    @Resource
    private StrategyMapper strategyMapper;
    @Resource
    private RuleMapper ruleMapper;

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        List<StrategyAwardEntity> strategyAwardEntities;

        // 优先从redis缓存中获取
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId;
        strategyAwardEntities = redisService.getValue(cacheKey);
        if (strategyAwardEntities != null && !strategyAwardEntities.isEmpty()) {
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
    public void storeStrategyAwardSearchRateTable(String key, Integer rateRange, Map<Integer, Long> strategyAwardSearchRateTable) {
        // 1. 存储 抽奖策略范围值，如1000，用于生成1000以内的随机数
        redisService.setValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key, rateRange);
        // 2. 存储 概率查找表
        Map<Integer, Long> cacheRateTable = redisService.getMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key);
        cacheRateTable.putAll(strategyAwardSearchRateTable);

    }

    @Override
    public Long getStrategyAwardAssemble(String strategyId, Integer rateKey) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId, rateKey);
    }

    @Override
    public int getRateRange(String strategyId) {
        return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId);
    }

    @Override
    public StrategyEntity queryStrategyEntityByStrategyId(Long strategyId) {
        // 优先从redis缓存中获取
        String cacheKey = Constants.RedisKey.STRATEGY_KEY + strategyId;
        StrategyEntity strategyEntity = redisService.getValue(cacheKey);
        if (strategyEntity != null) {
            return strategyEntity;
        }
        LambdaQueryWrapper<Strategy> queryWrapper = new QueryWrapper<Strategy>().lambda()
                .eq(Strategy::getStrategyId, strategyId);
        Strategy strategy = strategyMapper.selectOne(queryWrapper);
        StrategyEntity newStrategyEntity = new StrategyEntity();
        BeanUtils.copyProperties(strategy, newStrategyEntity);
        redisService.setValue(cacheKey, newStrategyEntity);
        return newStrategyEntity;
    }

    @Override
    public RuleEntity queryStrategyRule(Long strategyId, String ruleModel) {
        LambdaQueryWrapper<Rule> queryWrapper = new QueryWrapper<Rule>().lambda()
                .eq(Rule::getStrategyId, strategyId)
                .eq(Rule::getRuleModel, ruleModel);

        Rule rule = ruleMapper.selectOne(queryWrapper);
        RuleEntity ruleEntity = new RuleEntity();
        BeanUtils.copyProperties(rule, ruleEntity);
        return ruleEntity;
    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel) {
        LambdaQueryWrapper<Rule> queryWrapper = new QueryWrapper<Rule>().lambda()
                .eq(Rule::getStrategyId, strategyId)
                .eq(Rule::getRuleModel, ruleModel)
                //如果awardId不为null，则加入查询条件
                .eq(awardId != null, Rule::getAwardId, awardId);
        return ruleMapper.selectOne(queryWrapper).getRuleValue();
    }

}
