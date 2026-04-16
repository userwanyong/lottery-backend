package com.lottery.infrastructure.adapter.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.lottery.domain.activity.event.AwardStockZeroMessageEvent;
import com.lottery.domain.activity.model.entity.ActivityEntity;
import com.lottery.domain.strategy.event.SendLotteryMessageEvent;
import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.model.entity.StrategyAwardEntity;
import com.lottery.domain.strategy.model.entity.StrategyEntity;
import com.lottery.domain.strategy.model.valobj.RuleLimitTypeVO;
import com.lottery.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.lottery.domain.strategy.model.valobj.RuleTreeNodeLineVO;
import com.lottery.domain.strategy.model.valobj.RuleTreeNodeVO;
import com.lottery.domain.strategy.model.valobj.RuleTreeVO;
import com.lottery.domain.strategy.model.valobj.RuleWeightVO;
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
    public List<StrategyAwardEntity> queryActivityAwardList(Long activityId) {
        String cacheKey = Constants.RedisKey.ACTIVITY_AWARD_LIST_KEY + activityId;
        List<StrategyAwardEntity> strategyAwardEntities = redisService.getValue(cacheKey);
        if (strategyAwardEntities != null && !strategyAwardEntities.isEmpty()) {
            return strategyAwardEntities;
        }

        LambdaQueryWrapper<StrategyAward> queryWrapper = new QueryWrapper<StrategyAward>()
                .lambda()
                .eq(StrategyAward::getActivityId, activityId);
        List<StrategyAward> strategyAwards = strategyAwardMapper.selectList(queryWrapper);
        if (strategyAwards == null || strategyAwards.isEmpty()) {
            return new ArrayList<>();
        }

        Set<Long> awardIds = strategyAwards.stream().map(StrategyAward::getAwardId).collect(Collectors.toSet());
        List<Award> awards = awardIds.isEmpty() ? new ArrayList<>() : awardMapper.selectBatchIds(awardIds);
        for (Award award : awards) {
            redisService.setValue(Constants.RedisKey.AWARD_KEY + award.getId(), award);
        }
        Map<Long, String> awardMap = awards.stream().collect(Collectors.toMap(Award::getId, Award::getImage));

        strategyAwardEntities = new ArrayList<>(strategyAwards.size());
        for (StrategyAward strategyAward : strategyAwards) {
            StrategyAwardEntity strategyAwardEntity = new StrategyAwardEntity();
            BeanUtils.copyProperties(strategyAward, strategyAwardEntity);
            strategyAwardEntity.setImage(awardMap.get(strategyAward.getAwardId()));
            strategyAwardEntities.add(strategyAwardEntity);
        }
        redisService.setValue(cacheKey, strategyAwardEntities);
        return strategyAwardEntities;
    }

    @Override
    public <K, V> void storeStrategyAwardSearchRateTable(String key, Integer rateRange, Map<K, V> strategyAwardSearchRateTable) {
        redisService.setValue(Constants.RedisKey.RATE_RANGE_KEY + key, rateRange);
        Map<K, V> cacheRateTable = redisService.getMap(Constants.RedisKey.RATE_TABLE_KEY + key);
        cacheRateTable.putAll(strategyAwardSearchRateTable);
    }

    @Override
    public Long getStrategyAwardAssemble(String strategyId, Integer rateKey) {
        return redisService.getFromMap(Constants.RedisKey.RATE_TABLE_KEY + strategyId, rateKey);
    }

    @Override
    public int getRateRange(String strategyId) {
        Integer rateRange = redisService.getValue(Constants.RedisKey.RATE_RANGE_KEY + strategyId);
        return rateRange == null ? 0 : rateRange;
    }

    @Override
    public StrategyEntity queryStrategyEntityByStrategyId(Long strategyId) {
        String cacheKey = Constants.RedisKey.STRATEGY_KEY + strategyId;
        StrategyEntity strategyEntity = redisService.getValue(cacheKey);
        if (strategyEntity != null) {
            return strategyEntity;
        }
        Strategy strategy = strategyMapper.selectOne(new QueryWrapper<Strategy>().lambda().eq(Strategy::getId, strategyId));
        if (strategy == null) {
            return null;
        }
        StrategyEntity newStrategyEntity = new StrategyEntity();
        BeanUtils.copyProperties(strategy, newStrategyEntity);
        redisService.setValue(cacheKey, newStrategyEntity);
        return newStrategyEntity;
    }

    @Override
    public RuleEntity queryStrategyRule(String ruleModel) {
        Rule rule = ruleMapper.selectOne(new QueryWrapper<Rule>().lambda().eq(Rule::getRuleModel, ruleModel));
        if (rule == null) {
            return null;
        }
        RuleEntity ruleEntity = new RuleEntity();
        BeanUtils.copyProperties(rule, ruleEntity);
        return ruleEntity;
    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, Long awardId, String ruleModel) {
        // 1. 查Redis缓存
        String ruleValueCacheKey = Constants.RedisKey.STRATEGY_RULE_VALUE_KEY + strategyId + Constants.UNDERLINE + ruleModel;
        String cachedValue = redisService.getValue(ruleValueCacheKey);
        if (cachedValue != null) {
            return cachedValue;
        }

        // 2. 缓存未命中，查DB
        Strategy strategy = new Strategy();
        String cacheKey = Constants.RedisKey.STRATEGY_KEY + strategyId;
        StrategyEntity strategyEntity = redisService.getValue(cacheKey);
        if (strategyEntity != null) {
            BeanUtils.copyProperties(strategyEntity, strategy);
        } else {
            strategy = strategyMapper.selectOne(new LambdaQueryWrapper<Strategy>().eq(Strategy::getId, strategyId));
            if (strategy == null || strategy.getRuleModels() == null) {
                return null;
            }
        }

        String[] split = strategy.getRuleModels().split(",");
        LambdaQueryWrapper<Rule> queryWrapper = null;
        for (String model : split) {
            if (model.contains(ruleModel)) {
                queryWrapper = new QueryWrapper<Rule>().lambda().eq(Rule::getRuleModel, model);
                break;
            }
        }
        if (queryWrapper == null) {
            return null;
        }
        Rule rule = ruleMapper.selectOne(queryWrapper);
        String ruleValue = rule == null ? null : rule.getRuleValue();

        // 3. 写入缓存
        if (ruleValue != null) {
            redisService.setValue(ruleValueCacheKey, ruleValue);
        }
        return ruleValue;
    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, String ruleModel) {
        return queryStrategyRuleValue(strategyId, null, ruleModel);
    }

    @Override
    public Long queryRuleModelVO(Long activityId, Long awardId) {
        // 1. 查Redis缓存
        String cacheKey = Constants.RedisKey.RULE_MODEL_KEY + activityId + Constants.UNDERLINE + awardId;
        Long cachedTreeId = redisService.getValue(cacheKey);
        if (cachedTreeId != null) {
            return cachedTreeId;
        }

        // 2. 缓存未命中，查DB
        StrategyAward strategyAward = strategyAwardMapper.selectOne(new QueryWrapper<StrategyAward>().lambda()
                .eq(StrategyAward::getActivityId, activityId)
                .eq(StrategyAward::getAwardId, awardId));
        if (strategyAward == null || strategyAward.getRuleTreeId() == null) {
            // 缓存0L防穿透
            redisService.setValue(cacheKey, 0L);
            return 0L;
        }

        // 3. 写入缓存
        Long ruleTreeId = strategyAward.getRuleTreeId();
        redisService.setValue(cacheKey, ruleTreeId);
        return ruleTreeId;
    }

    @Override
    public RuleTreeVO queryRuleTreeVO(Long treeId) {
        String cacheKey = Constants.RedisKey.RULE_TREE_KEY + treeId;
        RuleTreeVO ruleTreeVOCache = redisService.getValue(cacheKey);
        if (ruleTreeVOCache != null) {
            return ruleTreeVOCache;
        }

        RuleTree ruleTree = ruleTreeMapper.selectOne(new QueryWrapper<RuleTree>().lambda().eq(RuleTree::getId, treeId));
        if (ruleTree == null) {
            return null;
        }

        List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeMapper.selectList(new QueryWrapper<RuleTreeNode>().lambda()
                .eq(RuleTreeNode::getRuleTreeId, treeId));
        List<RuleTreeNodeLine> ruleTreeNodeLines = ruleTreeNodeLineMapper.selectList(new QueryWrapper<RuleTreeNodeLine>().lambda()
                .eq(RuleTreeNodeLine::getRuleTreeId, treeId));

        Map<String, List<RuleTreeNodeLineVO>> ruleTreeNodeLineMap = new HashMap<>();
        for (RuleTreeNodeLine ruleTreeNodeLine : ruleTreeNodeLines) {
            RuleTreeNodeLineVO ruleTreeNodeLineVO = RuleTreeNodeLineVO.builder()
                    .ruleTreeId(ruleTreeNodeLine.getRuleTreeId())
                    .ruleNodeFrom(ruleTreeNodeLine.getRuleNodeFrom())
                    .ruleNodeTo(ruleTreeNodeLine.getRuleNodeTo())
                    .ruleLimitType(RuleLimitTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitType()))
                    .ruleLimitValue(RuleLogicCheckTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitValue()))
                    .build();
            ruleTreeNodeLineMap.computeIfAbsent(ruleTreeNodeLine.getRuleNodeFrom(), k -> new ArrayList<>()).add(ruleTreeNodeLineVO);
        }

        Map<String, RuleTreeNodeVO> ruleTreeNodeMap = new HashMap<>();
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
        redisService.setValue(cacheKey, ruleTreeVO);
        return ruleTreeVO;
    }

    @Override
    public Boolean reduceAwardStock(String key, Long activityId) {
        long count = redisService.decr(key);
        if (count == 0) {
            String[] split = key.split(Constants.UNDERLINE);
            String activityAward = split[split.length - 2] + Constants.UNDERLINE + split[split.length - 1];
            eventPublisher.publish(
                    awardStockZeroMessageEvent.topic(),
                    awardStockZeroMessageEvent.buildEventMessage(activityAward)
            );
        } else if (count < 0) {
            redisService.setAtomicLong(key, 0);
            return false;
        }

        Activity activity = new Activity();
        String cacheKey = Constants.RedisKey.ACTIVITY_KEY + activityId;
        ActivityEntity activityEntity = redisService.getValue(cacheKey);
        if (activityEntity != null) {
            BeanUtils.copyProperties(activityEntity, activity);
        } else {
            activity = activityMapper.selectOne(new QueryWrapper<Activity>().lambda().eq(Activity::getId, activityId));
        }

        long newCount = count + 1;
        String lockKey = key + Constants.UNDERLINE + newCount;
        long expireMillis = activity.getEndDateTime().getTime() - System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);
        Boolean lock = redisService.setNx(lockKey, expireMillis, TimeUnit.MILLISECONDS);
        if (!lock) {
            log.warn("activity award stock lock failed {}", lockKey);
        }
        return lock;
    }

    @Override
    public void awardStockConsumeSendQueue(LotteryReqEntity lotteryReqEntity) {
        String cacheKey = Constants.RedisKey.ACTIVITY_AWARD_COUNT_QUEUE_KEY
                + lotteryReqEntity.getActivityId()
                + Constants.UNDERLINE
                + lotteryReqEntity.getAwardId();
        RBlockingQueue<LotteryReqEntity> blockingQueue = redisService.getBlockingQueue(cacheKey);
        RDelayedQueue<LotteryReqEntity> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        delayedQueue.offer(lotteryReqEntity, 3, TimeUnit.SECONDS);
    }

    @Override
    public LotteryReqEntity takeQueueValue(String activityAward) {
        String cacheKey = Constants.RedisKey.ACTIVITY_AWARD_COUNT_QUEUE_KEY + activityAward;
        RBlockingQueue<LotteryReqEntity> destinationQueue = redisService.getBlockingQueue(cacheKey);
        return destinationQueue.poll();
    }

    @Override
    public void updateActivityAwardStock(Long activityId, Long awardId) {
        StrategyAward strategyAward = new StrategyAward();
        strategyAward.setActivityId(activityId);
        strategyAward.setAwardId(awardId);
        LambdaUpdateWrapper<StrategyAward> queryWrapper = new UpdateWrapper<StrategyAward>().lambda()
                .setSql("award_count_surplus = award_count_surplus - 1")
                .eq(StrategyAward::getActivityId, activityId)
                .eq(StrategyAward::getAwardId, awardId)
                .gt(StrategyAward::getAwardCountSurplus, 0);
        strategyAwardMapper.update(strategyAward, queryWrapper);
    }

    @Override
    public void cacheStrategyAwardCount(String key, Integer awardCount) {
        if (redisService.isExists(key)) {
            redisService.remove(key);
        }
        redisService.setAtomicLong(key, awardCount);
    }

    @Override
    public StrategyAwardEntity queryActivityAwardEntity(Long activityId, Long awardId) {
        String cacheKey = Constants.RedisKey.ACTIVITY_AWARD_KEY + activityId + Constants.UNDERLINE + awardId;
        StrategyAwardEntity strategyAwardEntity = redisService.getValue(cacheKey);
        if (strategyAwardEntity == null) {
            StrategyAward strategyAwardRes = strategyAwardMapper.selectOne(new QueryWrapper<StrategyAward>().lambda()
                    .eq(StrategyAward::getActivityId, activityId)
                    .eq(StrategyAward::getAwardId, awardId));
            if (strategyAwardRes == null) {
                return null;
            }
            strategyAwardEntity = new StrategyAwardEntity();
            BeanUtils.copyProperties(strategyAwardRes, strategyAwardEntity);
            redisService.setValue(cacheKey, strategyAwardEntity);
        }

        String awardCacheKey = Constants.RedisKey.AWARD_KEY + awardId;
        Award award = redisService.getValue(awardCacheKey);
        if (award == null) {
            award = awardMapper.selectById(awardId);
            if (award != null) {
                redisService.setValue(awardCacheKey, award);
            }
        }
        if (award != null) {
            strategyAwardEntity.setAwardConfig(award.getAwardConfig());
        }
        return strategyAwardEntity;
    }

    @Override
    public Long queryStrategyIdByActivityId(Long activityId) {
        Activity activity = activityMapper.selectOne(new QueryWrapper<Activity>().lambda().eq(Activity::getId, activityId));
        return activity == null ? null : activity.getStrategyId();
    }

    @Override
    public Integer queryTodayUserLotteryCount(String userId, Long strategyId, Long activityId) {
        ActivityAccountDay activityAccountDay = new ActivityAccountDay();
        activityAccountDay.setUserId(userId);
        activityAccountDay.setActivityId(activityId);
        activityAccountDay.setDay(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        ActivityAccountDay activityAccountDayRes = activityAccountDayMapper.queryActivityAccountDayByUserId(activityAccountDay);
        if (activityAccountDayRes == null) {
            return 0;
        }
        return activityAccountDayRes.getDayCount() - activityAccountDayRes.getDayCountSurplus();
    }

    @Override
    public Map<Long, Integer> queryAwardRuleLockCount(Long[] treeIds) {
        if (treeIds == null || treeIds.length == 0) {
            return new HashMap<>();
        }
        List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeMapper.selectList(new QueryWrapper<RuleTreeNode>().lambda()
                .eq(RuleTreeNode::getRuleName, Constants.RuleModel.RULE_LOCK)
                .in(RuleTreeNode::getRuleTreeId, Arrays.asList(treeIds)));
        Map<Long, Integer> map = new HashMap<>();
        for (RuleTreeNode ruleTreeNode : ruleTreeNodes) {
            map.put(ruleTreeNode.getRuleTreeId(), Integer.valueOf(ruleTreeNode.getRuleValue()));
        }
        return map;
    }

    @Override
    public List<String> getActivityAwardList() {
        String cacheKey = Constants.RedisKey.ACTIVITY_AWARD_LIST_KEY + "all";
        List<String> resultValue = redisService.getValue(cacheKey);
        if (resultValue != null && !resultValue.isEmpty()) {
            return resultValue;
        }
        List<StrategyAward> strategyAwards = strategyAwardMapper.selectList(null);
        resultValue = strategyAwards.stream()
                .map(strategyAward -> strategyAward.getActivityId() + Constants.UNDERLINE + strategyAward.getAwardId())
                .collect(Collectors.toList());
        redisService.setValue(cacheKey, resultValue);
        return resultValue;
    }

    @Override
    public void clearAwardStock(String activityAward) {
        String[] split = activityAward.split(Constants.UNDERLINE);
        LambdaUpdateWrapper<StrategyAward> updateWrapper = new LambdaUpdateWrapper<StrategyAward>()
                .set(StrategyAward::getAwardCountSurplus, 0)
                .set(StrategyAward::getUpdateTime, new Date())
                .eq(StrategyAward::getActivityId, Long.parseLong(split[0]))
                .eq(StrategyAward::getAwardId, Long.parseLong(split[1]));
        strategyAwardMapper.update(null, updateWrapper);
    }

    @Override
    public void clearQueueValue(String activityAward) {
        String cacheKey = Constants.RedisKey.ACTIVITY_AWARD_COUNT_QUEUE_KEY + activityAward;
        RBlockingQueue<LotteryReqEntity> blockingQueue = redisService.getBlockingQueue(cacheKey);
        blockingQueue.clear();
        RDelayedQueue<LotteryReqEntity> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        delayedQueue.clear();
    }

    @Override
    public List<RuleWeightVO> queryStrategyRuleWeight(Long activityId) {
        String cacheKey = Constants.RedisKey.ACTIVITY_RULE_WEIGHT_KEY + activityId;
        List<RuleWeightVO> ruleWeightVOList = redisService.getValue(cacheKey);
        if (ruleWeightVOList != null) {
            return ruleWeightVOList;
        }

        Long strategyId = queryStrategyIdByActivityId(activityId);
        StrategyEntity strategyEntity = queryStrategyEntityByStrategyId(strategyId);
        if (strategyEntity == null || strategyEntity.getRuleWeight() == null) {
            return new ArrayList<>();
        }

        Rule rule = ruleMapper.selectOne(new QueryWrapper<Rule>().lambda().eq(Rule::getRuleModel, strategyEntity.getRuleWeight()));
        if (rule == null) {
            return new ArrayList<>();
        }

        RuleEntity ruleEntity = new RuleEntity();
        ruleEntity.setRuleModel(strategyEntity.getRuleWeight());
        ruleEntity.setRuleValue(rule.getRuleValue());
        Map<String, List<Long>> ruleWeightValues = ruleEntity.getRuleWeightValues();

        List<RuleWeightVO> newRuleWeightVOList = new ArrayList<>();
        for (String ruleWeightKey : ruleWeightValues.keySet()) {
            List<Long> awardIds = ruleWeightValues.get(ruleWeightKey);
            List<RuleWeightVO.Award> awardList = new ArrayList<>();
            for (Long awardId : awardIds) {
                StrategyAward strategyAward = strategyAwardMapper.selectOne(new QueryWrapper<StrategyAward>().lambda()
                        .eq(StrategyAward::getActivityId, activityId)
                        .eq(StrategyAward::getAwardId, awardId));
                if (strategyAward == null) {
                    continue;
                }
                awardList.add(RuleWeightVO.Award.builder()
                        .awardId(awardId)
                        .awardTitle(strategyAward.getAwardTitle())
                        .build());
            }
            newRuleWeightVOList.add(RuleWeightVO.builder()
                    .ruleValue(rule.getRuleValue())
                    .awardIds(awardIds)
                    .awardList(awardList)
                    .weight(Integer.valueOf(ruleWeightKey.split(Constants.COLON)[0]))
                    .build());
        }

        redisService.setValue(cacheKey, newRuleWeightVOList);
        return newRuleWeightVOList;
    }

    @Override
    public void cacheStrategyArmoryAlgorithm(String key, String name) {
        String cacheKey = Constants.RedisKey.ACTIVITY_ALGORITHM_KEY + key;
        if (redisService.isExists(cacheKey)) {
            redisService.remove(cacheKey);
        }
        redisService.setValue(cacheKey, name);
    }

    @Override
    public String queryStrategyArmoryAlgorithmFromCache(String key) {
        String cacheKey = Constants.RedisKey.ACTIVITY_ALGORITHM_KEY + key;
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
        } catch (Exception e) {
            log.error("send lottery message failed userId:{} topic:{}", message.getData().getUserId(), topic, e);
        }
    }

    @Override
    public String queryRuleValue(Long awardId) {
        Award award = awardMapper.selectOne(new LambdaQueryWrapper<Award>().eq(Award::getId, awardId));
        return award == null ? null : award.getAwardConfig();
    }

    @Override
    public void deleteCacheKeyByStrategyId(Long strategyId) {
        redisService.deleteKeysWithPattern(Constants.RedisKey.STRATEGY_KEY + strategyId);
        redisService.deleteKeysWithPattern(Constants.RedisKey.STRATEGY_RULE_VALUE_KEY + strategyId);
    }

    @Override
    public void deleteCacheKeyByActivityId(Long activityId) {
        redisService.deleteKeysWithPattern(activityId.toString());
        redisService.remove(Constants.RedisKey.ACTIVITY_RULE_WEIGHT_KEY + activityId);
        redisService.deleteKeysWithPattern(Constants.RedisKey.RULE_MODEL_KEY + activityId);
    }

    @Override
    public void deleteCacheKeyByTreeId(Long treeId) {
        redisService.remove(Constants.RedisKey.RULE_TREE_KEY + treeId);
    }

    @Override
    public List<Long> queryActivityIdsByStrategyId(Long strategyId) {
        List<Activity> activities = activityMapper.selectList(new QueryWrapper<Activity>().lambda()
                .eq(Activity::getStrategyId, strategyId));
        return activities.stream().map(Activity::getId).toList();
    }
}
