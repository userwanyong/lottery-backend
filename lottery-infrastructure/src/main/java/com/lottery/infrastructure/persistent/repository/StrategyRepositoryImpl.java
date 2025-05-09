package com.lottery.infrastructure.persistent.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.lottery.domain.activity.event.AwardStockZeroMessageEvent;
import com.lottery.domain.activity.model.valobj.ActivitySkuStockKeyVO;
import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.model.entity.StrategyAwardEntity;
import com.lottery.domain.strategy.model.entity.StrategyEntity;
import com.lottery.domain.strategy.model.valobj.*;
import com.lottery.domain.strategy.repository.StrategyRepository;
import com.lottery.infrastructure.event.EventPublisher;
import com.lottery.infrastructure.persistent.dao.*;
import com.lottery.infrastructure.persistent.po.*;
import com.lottery.infrastructure.persistent.redis.RedisService;
import com.lottery.types.common.Constants;
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
        strategyAwardEntities = new ArrayList<>(strategyAwards.size());

        //StrategyAward->StrategyAwardEntity
        for (StrategyAward strategyAward : strategyAwards) {
            StrategyAwardEntity strategyAwardEntity = new StrategyAwardEntity();
            BeanUtils.copyProperties(strategyAward, strategyAwardEntity);
            strategyAwardEntity.setRuleModel(strategyAward.getRuleModels());
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
    public String queryStrategyRuleValue(Long strategyId, Long awardId, String ruleModel) {
        LambdaQueryWrapper<Rule> queryWrapper = new QueryWrapper<Rule>().lambda()
                .eq(Rule::getStrategyId, strategyId)
                .eq(Rule::getRuleModel, ruleModel)
                //如果awardId不为null，则加入查询条件
                .eq(awardId != null, Rule::getAwardId, awardId);
        return ruleMapper.selectOne(queryWrapper).getRuleValue();
    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, String ruleModel) {
        return queryStrategyRuleValue(strategyId, null, ruleModel);
    }

    @Override
    public StrategyRuleModelVO queryRuleModelVO(Long strategyId, Long awardId) {
        LambdaQueryWrapper<StrategyAward> queryWrapper = new QueryWrapper<StrategyAward>().lambda()
                .eq(StrategyAward::getStrategyId, strategyId)
                .eq(StrategyAward::getAwardId, awardId);
        StrategyAward strategyAward = strategyAwardMapper.selectOne(queryWrapper);
        return StrategyRuleModelVO.builder().ruleModels(strategyAward.getRuleModels()).build();
    }

    @Override
    public RuleTreeVO queryRuleTreeVO(String treeId) {
        // 优先从缓存获取
        String cacheKey = Constants.RedisKey.RULE_TREE_VO_KEY + treeId;
        RuleTreeVO ruleTreeVOCache = redisService.getValue(cacheKey);
        if (ruleTreeVOCache != null) {
            return ruleTreeVOCache;
        }
        // 否则从数据库获取
        LambdaQueryWrapper<RuleTree> ruleTreeQueryWrapper = new QueryWrapper<RuleTree>().lambda()
                .eq(RuleTree::getTreeId, treeId);
        RuleTree ruleTree = ruleTreeMapper.selectOne(ruleTreeQueryWrapper);

        LambdaQueryWrapper<RuleTreeNode> ruleTreeNodeQueryWrapper = new QueryWrapper<RuleTreeNode>().lambda()
                .eq(RuleTreeNode::getTreeId, treeId);
        List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeMapper.selectList(ruleTreeNodeQueryWrapper);

        LambdaQueryWrapper<RuleTreeNodeLine> ruleTreeNodeLineQueryWrapper = new QueryWrapper<RuleTreeNodeLine>().lambda()
                .eq(RuleTreeNodeLine::getTreeId, treeId);
        List<RuleTreeNodeLine> ruleTreeNodeLines = ruleTreeNodeLineMapper.selectList(ruleTreeNodeLineQueryWrapper);

        //转VO
        HashMap<String, List<RuleTreeNodeLineVO>> ruleTreeNodeLineMap = new HashMap<>();
        for (RuleTreeNodeLine ruleTreeNodeLine : ruleTreeNodeLines) {
            RuleTreeNodeLineVO ruleTreeNodeLineVO = RuleTreeNodeLineVO.builder()
                    .treeId(ruleTreeNodeLine.getTreeId())
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
                    .treeId(ruleTreeNode.getTreeId())
                    .ruleName(ruleTreeNode.getRuleName())
                    .ruleDesc(ruleTreeNode.getRuleDesc())
                    .ruleValue(ruleTreeNode.getRuleValue())
                    .treeNodeLineVOList(ruleTreeNodeLineMap.get(ruleTreeNode.getRuleName()))
                    .build();
            ruleTreeNodeMap.put(ruleTreeNode.getRuleName(), ruleTreeNodeVO);
        }
        RuleTreeVO ruleTreeVO = RuleTreeVO.builder()
                .treeId(ruleTree.getTreeId())
                .treeName(ruleTree.getTreeName())
                .treeDesc(ruleTree.getTreeDesc())
                .treeRootRuleNode(ruleTree.getTreeNodeRuleKey())
                .treeNodeMap(ruleTreeNodeMap)
                .build();

        // 保存到redis
        redisService.setValue(cacheKey, ruleTreeVO);
        return ruleTreeVO;
    }

    @Override
    public Boolean reduceAwardStock(String key,Long strategyId) {
        long count = redisService.decr(key);
        if (count==0){
            //lottery_strategy_award_count_key_200001_123 以_分割，提取200001_123
            String[] split = key.split(Constants.UNDERLINE);
            String strategyAward = split[split.length - 2]+"_"+split[split.length - 1];
            eventPublisher.publish(awardStockZeroMessageEvent.topic(),awardStockZeroMessageEvent.buildEventMessage(strategyAward));
        }
        else if (count < 0) {
            redisService.setAtomicLong(key, 0);
            return false;
        }
        LambdaQueryWrapper<Activity> queryWrapper = new QueryWrapper<Activity>().lambda()
                .eq(Activity::getStrategyId, strategyId);
        Activity activity = activityMapper.selectOne(queryWrapper);
        // 1. 按照cacheKey decr 后的值，如 99、98、97 和 key 组成为库存锁的key进行使用
        // 2. 加锁为了兜底，如果后续有恢复库存，手动处理等，也不会超卖。因为所有的可用库存key，都被加锁了
        String lockKey = key + Constants.UNDERLINE + count;
        long expireMillis = activity.getEndDateTime().getTime() - System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);
        Boolean lock = redisService.setNx(lockKey, expireMillis, TimeUnit.MILLISECONDS);
        if (!lock) {
            log.info("策略奖品库存加锁失败 {}", lockKey);
        }
        return lock;
    }

    @Override
    public void awardStockConsumeSendQueue(LotteryReqEntity lotteryReqEntity) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_QUEUE_KEY+lotteryReqEntity.getStrategyId()+Constants.UNDERLINE+lotteryReqEntity.getAwardId();
        // 获取Redis中的阻塞队列
        RBlockingQueue<LotteryReqEntity> blockingQueue = redisService.getBlockingQueue(cacheKey);
        // 基于阻塞队列创建一个延迟队列
        RDelayedQueue<LotteryReqEntity> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        // 将 lotteryReqEntity 添加到延迟队列，并设置延迟时间为3秒
        delayedQueue.offer(lotteryReqEntity, 3, TimeUnit.SECONDS);
    }

    @Override
    public LotteryReqEntity takeQueueValue(String strategyAward) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_QUEUE_KEY+strategyAward;
        // 获取指定键的阻塞队列
        RBlockingQueue<LotteryReqEntity> destinationQueue = redisService.getBlockingQueue(cacheKey);
        // 从队列中取出并返回一个元素
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
    }

    @Override
    public void cacheStrategyAwardCount(String key, Integer awardCount) {
        if (redisService.isExists(key)) {
            return;
        }
        redisService.setAtomicLong(key, awardCount);
    }

    @Override
    public StrategyAwardEntity queryStrategyAwardEntity(Long strategyId, Long awardId) {
        // 优先从缓存获取
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId + Constants.UNDERLINE + awardId;
        StrategyAwardEntity strategyAwardEntity = redisService.getValue(cacheKey);
        if (strategyAwardEntity!=null) {
            return strategyAwardEntity;
        }
        // 查询数据
        LambdaQueryWrapper<StrategyAward> queryWrapper = new QueryWrapper<StrategyAward>().lambda()
                .eq(StrategyAward::getStrategyId, strategyId)
                .eq(StrategyAward::getAwardId, awardId);

        StrategyAward strategyAwardRes = strategyAwardMapper.selectOne(queryWrapper);
        // 转换数据
        StrategyAwardEntity strategyAwardEntity1 = new StrategyAwardEntity();
        BeanUtils.copyProperties(strategyAwardRes, strategyAwardEntity1);
        // 缓存结果
        redisService.setValue(cacheKey, strategyAwardEntity1);
        // 返回数据
        return strategyAwardEntity1;
    }

    @Override
    public Long queryStrategyIdByActivityId(Long activityId) {
        LambdaQueryWrapper<Activity> queryWrapper = new QueryWrapper<Activity>().lambda()
                .eq(Activity::getActivityId, activityId);
        Activity activity = activityMapper.selectOne(queryWrapper);
        if(activity==null){
            return null;
        }
        return activity.getStrategyId();
    }

    @Override
    public Integer queryTodayUserLotteryCount(String userId, Long strategyId) {
        //获取活动id
        LambdaQueryWrapper<Activity> queryWrapper = new QueryWrapper<Activity>().lambda()
                .eq(Activity::getStrategyId, strategyId);
        Long activityId = activityMapper.selectOne(queryWrapper).getActivityId();
        // 封装参数
        ActivityAccountDay activityAccountDay = new ActivityAccountDay();
        activityAccountDay.setUserId(userId);
        activityAccountDay.setActivityId(activityId);
        activityAccountDay.setDay(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        ActivityAccountDay activityAccountDayRes = activityAccountDayMapper.queryActivityAccountDayByUserId(activityAccountDay);
        return activityAccountDayRes.getDayCount()- activityAccountDayRes.getDayCountSurplus();
    }

    @Override
    public Map<String, Integer> queryAwardRuleLockCount(String[] treeIds) {
        if (treeIds == null || treeIds.length==0){
            return new HashMap<>();
        }
        LambdaQueryWrapper<RuleTreeNode> queryWrapper = new QueryWrapper<RuleTreeNode>().lambda()
                .eq(RuleTreeNode::getRuleName, "rule_lock")
                .in(RuleTreeNode::getTreeId, Arrays.asList(treeIds));
        List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeMapper.selectList(queryWrapper);
        HashMap<String, Integer> map = new HashMap<>();
        for (RuleTreeNode ruleTreeNode : ruleTreeNodes) {
            String treeId = ruleTreeNode.getTreeId();
            String ruleValue = ruleTreeNode.getRuleValue();
            map.put(treeId, Integer.valueOf(ruleValue));
        }
        return map;

    }

    @Override
    public List<String> getStrategyAwardList() {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_QUEUE_KEY;
        List<String> resultValue = redisService.getValue(cacheKey);
        if (resultValue != null && !resultValue.isEmpty()) {
            return resultValue;
        }
        List<StrategyAward> strategyAwards = strategyAwardMapper.selectList(null);
        resultValue = strategyAwards.stream()
                .map(strategyAward -> strategyAward.getStrategyId()+"_"+strategyAward.getAwardId())
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
                .eq(StrategyAward::getStrategyId, split[1])
                .eq(StrategyAward::getAwardId, split[2]);
        strategyAwardMapper.update(null, updateWrapper);

    }

    @Override
    public void clearQueueValue(String strategyAward) {
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_COUNT_QUEUE_KEY+strategyAward;
        RBlockingQueue<LotteryReqEntity> blockingQueue = redisService.getBlockingQueue(cacheKey);
        blockingQueue.clear();
        RDelayedQueue<LotteryReqEntity> delayedQueue = redisService.getDelayedQueue(blockingQueue);
        delayedQueue.clear();
    }

    @Override
    public List<RuleWeightVO> queryStrategyRuleWeight(String userId, Long activityId) {
        // 优先从缓存获取
        Long strategyId = queryStrategyIdByActivityId(activityId);
        String cacheKey = Constants.RedisKey.STRATEGY_RULE_WEIGHT_KEY + strategyId;
        List<RuleWeightVO> ruleWeightVOList = redisService.getValue(cacheKey);
        if (ruleWeightVOList!=null) {
            return ruleWeightVOList;
        }
        // 1.查询权重规则配置
        LambdaQueryWrapper<Rule> queryWrapper = new QueryWrapper<Rule>().lambda()
                .eq(Rule::getStrategyId,strategyId)
                .eq(Rule::getRuleModel, Constants.RuleModel.RULE_WIGHT);
        String ruleValue = ruleMapper.selectOne(queryWrapper).getRuleValue();
        // 2.处理规则的值
        RuleEntity ruleEntity = new RuleEntity();
        ruleEntity.setRuleModel(Constants.RuleModel.RULE_WIGHT);
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
}
