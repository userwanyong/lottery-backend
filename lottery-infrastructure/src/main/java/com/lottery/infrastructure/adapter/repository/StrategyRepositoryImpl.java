package com.lottery.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lottery.domain.activity.event.AwardStockZeroMessageEvent;
import com.lottery.domain.strategy.event.SendLotteryMessageEvent;
import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.model.entity.StrategyAwardEntity;
import com.lottery.domain.strategy.model.entity.StrategyEntity;
import com.lottery.domain.strategy.model.valobj.*;
import com.lottery.domain.strategy.repository.StrategyRepository;
import com.lottery.infrastructure.dao.*;
import com.lottery.infrastructure.dao.po.*;
import com.lottery.infrastructure.event.EventPublisher;
import com.lottery.infrastructure.redis.RedisService;
import com.lottery.types.common.Constants;
import com.lottery.types.event.BaseEvent;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 永
 * 策略领域仓储实现
 */
@Repository
@Slf4j
public class StrategyRepositoryImpl implements StrategyRepository {
    @Resource
    private AwardMapper awardMapper;
    @Resource
    private StrategyAwardMapper strategyAwardMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private StrategyMapper strategyMapper;
    @Resource
    private ActivityAccountDayMapper activityAccountDayMapper;
    @Resource
    private ActivityMapper activityMapper;
    @Resource
    private RuleMapper ruleMapper;
    @Resource
    private RuleTreeMapper ruleTreeMapper;
    @Resource
    private RuleTreeNodeMapper ruleTreeNodeMapper;
    @Resource
    private RuleTreeNodeLineMapper ruleTreeNodeLineMapper;
    @Resource
    private EventPublisher eventPublisher;
    @Resource
    private AwardStockZeroMessageEvent awardStockZeroMessageEvent;

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {
        List<StrategyAwardEntity> strategyAwardEntities;

        // 优先从redis缓存中获取
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_LIST_KEY + strategyId;
        strategyAwardEntities = redisService.getValue(cacheKey);
        if (strategyAwardEntities != null && !strategyAwardEntities.isEmpty()) {
            return strategyAwardEntities;
        }
        // 否则查询数据库
        LambdaQueryWrapper<StrategyAward> queryWrapper = new QueryWrapper<StrategyAward>()
                .lambda()
                .eq(StrategyAward::getStrategyId, strategyId);
        List<StrategyAward> strategyAwards = strategyAwardMapper.selectList(queryWrapper);
        // 提取出strategyAwards中所有的awardId
        Set<Long> awardIds = strategyAwards.stream().map(StrategyAward::getAwardId).collect(Collectors.toSet());
        // 批量查询数据库
        List<Award> awards = awardMapper.selectBatchIds(awardIds);
        // 放到map集合中 awardId为key image为value
        Map<Long, String> awardMap = awards.stream().collect(Collectors.toMap(Award::getId, Award::getImage));
        strategyAwardEntities = new ArrayList<>(strategyAwards.size());


        //StrategyAward->StrategyAwardEntity
        for (StrategyAward strategyAward : strategyAwards) {
            StrategyAwardEntity strategyAwardEntity = new StrategyAwardEntity();
            BeanUtils.copyProperties(strategyAward, strategyAwardEntity);
            // image 从Map中取出
            strategyAwardEntity.setImage(awardMap.get(strategyAward.getAwardId()));
            strategyAwardEntity.setRuleTreeId(strategyAward.getRuleTreeId());
            strategyAwardEntities.add(strategyAwardEntity);
        }
        //保存到redis中
        redisService.setValue(cacheKey, strategyAwardEntities);
        return strategyAwardEntities;
    }

    @Override
    public <K, V> void storeStrategyAwardSearchRateTable(String key, Integer rateRange, Map<K, V> strategyAwardSearchRateTable) {
        // 1. 存储 抽奖策略范围值，如1000，用于生成1000以内的随机数
        String rateRangeKey = Constants.RedisKey.RATE_RANGE_KEY + key;
        redisService.setValue(rateRangeKey, rateRange);
        log.debug("[StrategyRepositoryImpl]存储策略抽奖概率范围值：{}", rateRange);
        // 2. 存储 概率查找表
        String rateTableKey = Constants.RedisKey.RATE_TABLE_KEY + key;
        Map<K, V> cacheRateTable = redisService.getMap(rateTableKey);
        cacheRateTable.putAll(strategyAwardSearchRateTable);
        log.debug("[StrategyRepositoryImpl]存储策略抽奖概率查找表：{}", cacheRateTable);

    }

    @Override
    public Long getStrategyAwardAssemble(String strategyId, Integer rateKey) {
        return redisService.getFromMap(Constants.RedisKey.RATE_TABLE_KEY + strategyId, rateKey);
    }

    @Override
    public int getRateRange(String strategyId) {
        return redisService.getValue(Constants.RedisKey.RATE_RANGE_KEY + strategyId);
    }

    @Override
    public StrategyEntity queryStrategyEntityByStrategyId(Long strategyId) {
        //todo 缓存-关
//        // 优先从redis缓存中获取
//        String cacheKey = Constants.RedisKey.STRATEGY_KEY + strategyId;
//        StrategyEntity strategyEntity = redisService.getValue(cacheKey);
//        if (strategyEntity != null) {
//            return strategyEntity;
//        }
        LambdaQueryWrapper<Strategy> queryWrapper = new QueryWrapper<Strategy>().lambda()
                .eq(Strategy::getId, strategyId);
        Strategy strategy = strategyMapper.selectOne(queryWrapper);
        StrategyEntity newStrategyEntity = new StrategyEntity();
        BeanUtils.copyProperties(strategy, newStrategyEntity);
        //todo 缓存-关
//        redisService.setValue(cacheKey, newStrategyEntity);
        return newStrategyEntity;
    }

    @Override
    public RuleEntity queryStrategyRule(String ruleModel) {
        LambdaQueryWrapper<Rule> queryWrapper = new QueryWrapper<Rule>().lambda()
                .eq(Rule::getRuleModel, ruleModel);
        Rule rule = ruleMapper.selectOne(queryWrapper);
        RuleEntity ruleEntity = new RuleEntity();
        BeanUtils.copyProperties(rule, ruleEntity);
        return ruleEntity;
    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, Long awardId, String ruleModel) {
        LambdaQueryWrapper<Strategy> wrapper = new LambdaQueryWrapper<Strategy>().eq(Strategy::getId, strategyId);
        Strategy strategy = strategyMapper.selectOne(wrapper);
        if (strategy == null) {
            return null;
        }
        String ruleModels = strategy.getRuleModels();
        String[] split = ruleModels.split(",");
        LambdaQueryWrapper<Rule> queryWrapper = null;
        for (String model : split) {
            if (model.contains(ruleModel)) {
                queryWrapper = new QueryWrapper<Rule>().lambda()
                        .eq(Rule::getRuleModel, model);
            }
        }

        return ruleMapper.selectOne(queryWrapper).getRuleValue();
    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, String ruleModel) {
        return queryStrategyRuleValue(strategyId, null, ruleModel);
    }

    @Override
    public Long queryRuleModelVO(Long strategyId, Long awardId) {
        LambdaQueryWrapper<StrategyAward> queryWrapper = new QueryWrapper<StrategyAward>().lambda()
                .eq(StrategyAward::getStrategyId, strategyId)
                .eq(StrategyAward::getAwardId, awardId);
        StrategyAward strategyAward = strategyAwardMapper.selectOne(queryWrapper);
        return strategyAward.getRuleTreeId() == null ? 0L : strategyAward.getRuleTreeId();
    }

    @Override
    public RuleTreeVO queryRuleTreeVO(Long treeId) {
        //todo 缓存-关
//        // 优先从缓存获取
//        String cacheKey = Constants.RedisKey.RULE_TREE_KEY + treeId;
//        RuleTreeVO ruleTreeVOCache = redisService.getValue(cacheKey);
//        if (ruleTreeVOCache != null) {
//            return ruleTreeVOCache;
//        }
        // 否则从数据库获取
        LambdaQueryWrapper<RuleTree> ruleTreeQueryWrapper = new QueryWrapper<RuleTree>().lambda()
                .eq(RuleTree::getId, treeId);
        RuleTree ruleTree = ruleTreeMapper.selectOne(ruleTreeQueryWrapper);

        LambdaQueryWrapper<RuleTreeNode> ruleTreeNodeQueryWrapper = new QueryWrapper<RuleTreeNode>().lambda()
                .eq(RuleTreeNode::getRuleTreeId, treeId);
        List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeMapper.selectList(ruleTreeNodeQueryWrapper);

        LambdaQueryWrapper<RuleTreeNodeLine> ruleTreeNodeLineQueryWrapper = new QueryWrapper<RuleTreeNodeLine>().lambda()
                .eq(RuleTreeNodeLine::getRuleTreeId, treeId);
        List<RuleTreeNodeLine> ruleTreeNodeLines = ruleTreeNodeLineMapper.selectList(ruleTreeNodeLineQueryWrapper);

        //转VO
        HashMap<String, List<RuleTreeNodeLineVO>> ruleTreeNodeLineMap = new HashMap<>();
        for (RuleTreeNodeLine ruleTreeNodeLine : ruleTreeNodeLines) {
            RuleTreeNodeLineVO ruleTreeNodeLineVO = RuleTreeNodeLineVO.builder()
                    .ruleTreeId(ruleTreeNodeLine.getRuleTreeId())
                    .ruleNodeFrom(ruleTreeNodeLine.getRuleNodeFrom())
                    .ruleNodeTo(ruleTreeNodeLine.getRuleNodeTo())
                    .ruleLimitType(RuleLimitTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitType()))
                    .ruleLimitValue(RuleLogicCheckTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitValue()))
                    .build();
            List<RuleTreeNodeLineVO> ruleTreeNodeLineVOList = ruleTreeNodeLineMap.computeIfAbsent(ruleTreeNodeLine.getRuleNodeFrom(), k -> new ArrayList<>());
            ruleTreeNodeLineVOList.add(ruleTreeNodeLineVO);
        }
        HashMap<String, RuleTreeNodeVO> ruleTreeNodeMap = new HashMap<>();
        for (RuleTreeNode ruleTreeNode : ruleTreeNodes) {
            RuleTreeNodeVO ruleTreeNodeVO = RuleTreeNodeVO.builder()
                    .ruleTreeId(ruleTreeNode.getRuleTreeId())
                    .ruleName(ruleTreeNode.getRuleName())
                    .ruleDesc(ruleTreeNode.getRuleDesc())
                    .ruleValue(ruleTreeNode.getRuleValue())
                    .treeNodeLineVOList(ruleTreeNodeLineMap.get(ruleTreeNode.getRuleName()))
                    .build();
            ruleTreeNodeMap.put(ruleTreeNode.getRuleName(), ruleTreeNodeVO);
        }
        RuleTreeVO ruleTreeVO = RuleTreeVO.builder()
                .id(ruleTree.getId())
                .treeName(ruleTree.getTreeName())
                .treeDesc(ruleTree.getTreeDesc())
                .treeRootRuleNode(ruleTree.getTreeNodeRuleKey())
                .treeNodeMap(ruleTreeNodeMap)
                .build();

        //todo 缓存-关
//        // 保存到redis
//        redisService.setValue(cacheKey, ruleTreeVO);
        return ruleTreeVO;
    }

    @Override
    public Boolean reduceAwardStock(String key, Long strategyId, Long activityId) {
        long count = redisService.decr(key);
        if (count == 0) {
            //lottery_strategy_award_count_key_200001_123 以_分割，提取200001_123
            String[] split = key.split(Constants.UNDERLINE);
            String strategyAward = split[split.length - 2] + "_" + split[split.length - 1];
            log.debug("[ActivityRepositoryImpl]已无库存，发送MQ消息清空数据库库存 strategyAward: {}", strategyAward);
            eventPublisher.publish(awardStockZeroMessageEvent.topic(), awardStockZeroMessageEvent.buildEventMessage(strategyAward));
        } else if (count < 0) {
            redisService.setAtomicLong(key, 0);
            return false;
        }
        LambdaQueryWrapper<Activity> queryWrapper = new QueryWrapper<Activity>().lambda()
                .eq(Activity::getStrategyId, strategyId)
                .eq(Activity::getId, activityId);
        Activity activity = activityMapper.selectOne(queryWrapper);
        // 1. 按照cacheKey decr 后的值，如 99、98、97 和 key 组成为库存锁的key进行使用
        // 2. 加锁为了兜底，如果后续有恢复库存，手动处理等，也不会超卖。因为所有的可用库存key，都被加锁了
        long newCount = count + 1;
        String lockKey = key + Constants.UNDERLINE + newCount;
        long expireMillis = activity.getEndDateTime().getTime() - System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);
        Boolean lock = redisService.setNx(lockKey, expireMillis, TimeUnit.MILLISECONDS);
        log.debug("[ActivityRepositoryImpl]策略奖品库存加锁成功 {}", lockKey);
        if (!lock) {
            log.warn("[ActivityRepositoryImpl]策略奖品库存加锁失败 {}", lockKey);
        }
        return lock;
    }

    @Override
    public void awardStockConsumeSendQueue(LotteryReqEntity lotteryReqEntity) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_QUEUE_KEY + lotteryReqEntity.getStrategyId() + Constants.UNDERLINE + lotteryReqEntity.getAwardId();
        // 获取Redis中的阻塞队列
        RBlockingQueue<LotteryReqEntity> blockingQueue = redisService.getBlockingQueue(cacheKey);
        // 基于阻塞队列创建一个延迟队列
        RDelayedQueue<LotteryReqEntity> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        // 将 lotteryReqEntity 添加到延迟队列，并设置延迟时间为3秒
        delayedQueue.offer(lotteryReqEntity, 3, TimeUnit.SECONDS);
        log.debug("[ActivityRepositoryImpl]延迟队列创建成功 awardId: {}", lotteryReqEntity.getAwardId());
    }

    @Override
    public LotteryReqEntity takeQueueValue(String strategyAward) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_QUEUE_KEY + strategyAward;
        // 获取指定键的阻塞队列
        RBlockingQueue<LotteryReqEntity> destinationQueue = redisService.getBlockingQueue(cacheKey);
        // 从队列中取出并返回一个元素
        log.debug("[ActivityRepositoryImpl]从阻塞队列中取出值 strategyAward: {}", strategyAward);
        return destinationQueue.poll();
    }

    @Override
    public void updateStrategyAwardStock(Long strategyId, Long awardId) {
        StrategyAward strategyAward = new StrategyAward();
        strategyAward.setStrategyId(strategyId);
        strategyAward.setAwardId(awardId);
        LambdaUpdateWrapper<StrategyAward> queryWrapper = new UpdateWrapper<StrategyAward>().lambda()
                .setSql("award_count_surplus = award_count_surplus - 1")
                .eq(StrategyAward::getStrategyId, strategyId)
                .eq(StrategyAward::getAwardId, awardId)
                .gt(StrategyAward::getAwardCountSurplus, 0);
        strategyAwardMapper.update(strategyAward, queryWrapper);
        log.debug("[ActivityRepositoryImpl]更新策略奖品库存成功 strategyId: {}, awardId: {}", strategyId, awardId);
    }

    @Override
    public void cacheStrategyAwardCount(String key, Integer awardCount) {
        if (redisService.isExists(key)) {
            redisService.remove(key);
        }
        redisService.setAtomicLong(key, awardCount);
    }

    @Override
    public StrategyAwardEntity queryStrategyAwardEntity(Long strategyId, Long awardId) {
        //todo 缓存-关
//        // 优先从缓存获取
//        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId + Constants.UNDERLINE + awardId;
//        StrategyAwardEntity strategyAwardEntity = redisService.getValue(cacheKey);
//        if (strategyAwardEntity != null) {
//            return strategyAwardEntity;
//        }
        // 查询数据
        LambdaQueryWrapper<StrategyAward> queryWrapper = new QueryWrapper<StrategyAward>().lambda()
                .eq(StrategyAward::getStrategyId, strategyId)
                .eq(StrategyAward::getAwardId, awardId);

        StrategyAward strategyAwardRes = strategyAwardMapper.selectOne(queryWrapper);
        // 转换数据
        StrategyAwardEntity strategyAwardEntity1 = new StrategyAwardEntity();
        BeanUtils.copyProperties(strategyAwardRes, strategyAwardEntity1);
        //todo 缓存-关
//        // 缓存结果
//        redisService.setValue(cacheKey, strategyAwardEntity1);
        // 返回数据
        return strategyAwardEntity1;
    }

    @Override
    public Long queryStrategyIdByActivityId(Long activityId) {
        LambdaQueryWrapper<Activity> queryWrapper = new QueryWrapper<Activity>().lambda()
                .eq(Activity::getId, activityId);
        Activity activity = activityMapper.selectOne(queryWrapper);
        if (activity == null) {
            return null;
        }
        return activity.getStrategyId();
    }

    @Override
    public Integer queryTodayUserLotteryCount(String userId, Long strategyId, Long activityId) {
        ActivityAccountDay activityAccountDay = new ActivityAccountDay();
        activityAccountDay.setUserId(userId);
        activityAccountDay.setActivityId(activityId);
        activityAccountDay.setDay(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        ActivityAccountDay activityAccountDayRes = activityAccountDayMapper.queryActivityAccountDayByUserId(activityAccountDay);
        return activityAccountDayRes.getDayCount() - activityAccountDayRes.getDayCountSurplus();
    }

    @Override
    public Map<Long, Integer> queryAwardRuleLockCount(Long[] treeIds) {
        if (treeIds == null || treeIds.length == 0) {
            return new HashMap<>();
        }
        LambdaQueryWrapper<RuleTreeNode> queryWrapper = new QueryWrapper<RuleTreeNode>().lambda()
                .eq(RuleTreeNode::getRuleName, Constants.RuleModel.RULE_LOCK)
                .in(RuleTreeNode::getRuleTreeId, Arrays.asList(treeIds));
        List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeMapper.selectList(queryWrapper);
        HashMap<Long, Integer> map = new HashMap<>();
        for (RuleTreeNode ruleTreeNode : ruleTreeNodes) {
            Long ruleTreeId = ruleTreeNode.getRuleTreeId();
            String ruleValue = ruleTreeNode.getRuleValue();
            map.put(ruleTreeId, Integer.valueOf(ruleValue));
        }
        return map;

    }

    @Override
    public List<String> getStrategyAwardList() {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_LIST_KEY;
        List<String> resultValue = redisService.getValue(cacheKey);
        if (resultValue != null && !resultValue.isEmpty()) {
            return resultValue;
        }
        List<StrategyAward> strategyAwards = strategyAwardMapper.selectList(null);
        resultValue = strategyAwards.stream()
                .map(strategyAward -> strategyAward.getStrategyId() + "_" + strategyAward.getAwardId())
                .collect(Collectors.toList());
        redisService.setValue(cacheKey, resultValue);
        return resultValue;
    }

    @Override
    public void clearAwardStock(String strategyAward) {
        //使用_拆分
        String[] split = strategyAward.split("_");
        LambdaUpdateWrapper<StrategyAward> updateWrapper = new LambdaUpdateWrapper<StrategyAward>()
                .set(StrategyAward::getAwardCountSurplus, 0)
                .set(StrategyAward::getUpdateTime, new Date())
                .eq(StrategyAward::getStrategyId, split[0])
                .eq(StrategyAward::getAwardId, split[1]);
        strategyAwardMapper.update(null, updateWrapper);
        log.debug("[ActivityRepositoryImpl]清空奖品库存成功 strategyId: {}, awardId: {}", split[0], split[1]);

    }

    @Override
    public void clearQueueValue(String strategyAward) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_QUEUE_KEY + strategyAward;
        RBlockingQueue<LotteryReqEntity> blockingQueue = redisService.getBlockingQueue(cacheKey);
        blockingQueue.clear();
        RDelayedQueue<LotteryReqEntity> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        delayedQueue.clear();
        log.debug("[ActivityRepositoryImpl]清空strategyAward库存为0的阻塞队列与延时队列成功 strategyAward: {}", strategyAward);
    }

    @Override
    public List<RuleWeightVO> queryStrategyRuleWeight(Long activityId) {
        // 优先从缓存获取
        Long strategyId = queryStrategyIdByActivityId(activityId);
        String cacheKey = Constants.RedisKey.STRATEGY_RULE_WEIGHT_KEY + strategyId;
        List<RuleWeightVO> ruleWeightVOList = redisService.getValue(cacheKey);
        if (ruleWeightVOList != null) {
            return ruleWeightVOList;
        }

        // 根据策略ID查策略表，得到包含rule_weight的规则模型
        StrategyEntity strategyEntity = queryStrategyEntityByStrategyId(strategyId);
        String ruleModel = strategyEntity.getRuleWeight();

        // 1.查询权重规则配置
        LambdaQueryWrapper<Rule> queryWrapper = new QueryWrapper<Rule>().lambda()
                .eq(Rule::getRuleModel, ruleModel);
        Rule rule = ruleMapper.selectOne(queryWrapper);
        if (rule == null) {
            // 未配置权重
            return new ArrayList<>();
        }
        String ruleValue = rule.getRuleValue();
        // 2.处理规则的值
        RuleEntity ruleEntity = new RuleEntity();
        ruleEntity.setRuleModel(ruleModel);
        ruleEntity.setRuleValue(ruleValue);
        Map<String, List<Long>> ruleWeightValues = ruleEntity.getRuleWeightValues();
        // 3.组装权重奖品
        List<RuleWeightVO> newRuleWeightVOList = new ArrayList<>();
        ruleWeightValues.keySet().forEach(ruleWeightKey -> {
            List<Long> awardIds = ruleWeightValues.get(ruleWeightKey);
            List<RuleWeightVO.Award> awardList = new ArrayList<>();
            awardIds.forEach(awardId -> {
                LambdaQueryWrapper<StrategyAward> wrapper = new QueryWrapper<StrategyAward>().lambda()
                        .eq(StrategyAward::getStrategyId, strategyId)
                        .eq(StrategyAward::getAwardId, awardId);
                StrategyAward strategyAward = strategyAwardMapper.selectOne(wrapper);
                awardList.add(RuleWeightVO.Award.builder()
                        .awardId(awardId)
                        .awardTitle(strategyAward.getAwardTitle())
                        .build());
            });
            newRuleWeightVOList.add(RuleWeightVO.builder()
                    .ruleValue(ruleValue)
                    .awardIds(awardIds)
                    .awardList(awardList)
                    .weight(Integer.valueOf(ruleWeightKey.split(Constants.COLON)[0]))
                    .build());
        });
        // 放入缓存
        redisService.setValue(cacheKey, newRuleWeightVOList);
        return newRuleWeightVOList;
    }

    @Override
    public void cacheStrategyArmoryAlgorithm(String key, String name) {
        String cacheKey = Constants.RedisKey.STRATEGY_ALGORITHM_KEY + key;
        if (redisService.isExists(cacheKey)) {
            redisService.remove(cacheKey);
        }
        redisService.setValue(cacheKey, name);
    }

    @Override
    public String queryStrategyArmoryAlgorithmFromCache(String key) {
        String cacheKey = Constants.RedisKey.STRATEGY_ALGORITHM_KEY + key;
        if (!redisService.isExists(cacheKey)) {
            return null;
        }
        return redisService.getValue(cacheKey);
    }

    @Override
    public <K, V> Map<K, V> getMap(String key) {
        return redisService.getMap(Constants.RedisKey.RATE_TABLE_KEY + key);
    }

    @Override
    public void sendLotteryMessageToMq(String topic, BaseEvent.EventMessage<SendLotteryMessageEvent.LotteryMessage> message) {
        try {
            eventPublisher.publish(topic, message);
            log.debug("[StrategyRepositoryImpl]发送中奖广播消息成功 userId: {} topic: {}", message.getData().getUserId(), topic);
        } catch (Exception e) {
            log.error("[StrategyRepositoryImpl]发送中奖广播消息失败 userId: {} topic: {}", message.getData().getUserId(), topic);
        }
    }

    @Override
    public String queryRuleValue(Long awardId) {
        LambdaQueryWrapper<Award> queryWrapper = new LambdaQueryWrapper<Award>().eq(Award::getId, awardId);
        return awardMapper.selectOne(queryWrapper).getAwardConfig();
    }

    @Override
    public void deleteCacheKeyByStrategyId(Long strategyId) {
        redisService.deleteKeysWithPattern(strategyId.toString());
    }

    @Override
    public void deleteCacheKeyByTreeId(Long treeId) {
        redisService.remove(Constants.RedisKey.RULE_TREE_KEY + treeId);
    }

}
