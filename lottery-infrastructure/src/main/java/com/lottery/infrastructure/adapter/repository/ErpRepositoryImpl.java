package com.lottery.infrastructure.adapter.repository;


import cn.bugstack.middleware.db.router.strategy.IDBRouterStrategy;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lottery.domain.rebate.model.aggregate.RebateAggregate;
import com.lottery.domain.rebate.model.entity.RebateOrderEntity;
import com.lottery.domain.rebate.model.entity.TaskEntity;
import com.lottery.domain.strategy.model.entity.StrategyAwardEntity;
import com.lottery.infrastructure.dao.*;
import com.lottery.infrastructure.dao.po.*;
import com.lottery.infrastructure.redis.RedisService;
import com.lottery.querys.adapter.repository.ErpRepository;
import com.lottery.querys.model.valobj.*;
import com.lottery.types.common.Constants;
import com.lottery.types.model.MyPage;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 永
 */
@Repository
public class ErpRepositoryImpl implements ErpRepository {
    @Resource
    private ActivityMapper activityMapper;
    @Resource
    private ActivityCountMapper activityCountMapper;
    @Resource
    private ActivitySkuMapper activitySkuMapper;
    @Resource
    private BehaviorRebateMapper behaviorRebateMapper;
    @Resource
    private AwardMapper awardMapper;
    @Resource
    private StrategyMapper strategyMapper;
    @Resource
    private RuleMapper ruleMapper;
    @Resource
    private StrategyAwardMapper strategyAwardMapper;
    @Resource
    private RuleTreeMapper ruleTreeMapper;
    @Resource
    private RuleTreeNodeMapper ruleTreeNodeMapper;
    @Resource
    private RuleTreeNodeLineMapper ruleTreeNodeLineMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private UserAwardRecordMapper userAwardRecordMapper;
    @Resource
    private IDBRouterStrategy dbRouter;

    @Override
    public List<ActivityVO> queryActivityVOList() {
        List<Activity> activities = activityMapper.selectList(null);
        activities.sort((o1, o2) -> o2.getUpdateTime().compareTo(o1.getUpdateTime()));
        ArrayList<ActivityVO> list = new ArrayList<>();
        for (Activity activity : activities) {
            ActivityVO activityVO = new ActivityVO();
            BeanUtils.copyProperties(activity, activityVO);
            list.add(activityVO);
        }
        return list;
    }

    @Override
    public void addActivityVO(ActivityVO activityVO) {
        Activity activity = new Activity();
        BeanUtils.copyProperties(activityVO, activity);
        activityMapper.insert(activity);
    }

    @Override
    public void updateActivityVO(ActivityVO activityVO) {
        Activity activity = new Activity();
        BeanUtils.copyProperties(activityVO, activity);
        activityMapper.updateById(activity);
    }

    @Override
    public void deleteActivityVO(Long activityId) {
        activityMapper.deleteById(activityId);
    }

    @Override
    public List<ActivityCountVO> queryActivityCountVOList() {
        List<ActivityCount> activityCounts = activityCountMapper.selectList(null);
        activityCounts.sort((o1, o2) -> o2.getUpdateTime().compareTo(o1.getUpdateTime()));
        ArrayList<ActivityCountVO> list = new ArrayList<>();
        for (ActivityCount activityCount : activityCounts) {
            ActivityCountVO activityCountVO = new ActivityCountVO();
            BeanUtils.copyProperties(activityCount, activityCountVO);
            list.add(activityCountVO);
        }
        return list;
    }

    @Override
    public void addActivityCountVO(ActivityCountVO activityCountVO) {
        ActivityCount activityCount = new ActivityCount();
        BeanUtils.copyProperties(activityCountVO, activityCount);
        activityCountMapper.insert(activityCount);
    }

    @Override
    public void updateActivityCountVO(ActivityCountVO activityCountVO) {
        ActivityCount activityCount = new ActivityCount();
        BeanUtils.copyProperties(activityCountVO, activityCount);
        activityCountMapper.updateById(activityCount);
    }

    @Override
    public void deleteActivityCountVO(Long activityCountId) {
        activityCountMapper.deleteById(activityCountId);
    }

    @Override
    public List<ActivitySkuVO> queryActivitySkuVOList() {
        List<ActivitySku> activitySkus = activitySkuMapper.selectList(null);
        activitySkus.sort((o1, o2) -> o2.getUpdateTime().compareTo(o1.getUpdateTime()));
        ArrayList<ActivitySkuVO> list = new ArrayList<>();
        for (ActivitySku activitySku : activitySkus) {
            ActivitySkuVO activitySkuVO = new ActivitySkuVO();
            BeanUtils.copyProperties(activitySku, activitySkuVO);
            list.add(activitySkuVO);
        }
        return list;
    }

    @Override
    public void addActivitySkuVO(ActivitySkuVO activitySkuVO) {
        ActivitySku activitySku = new ActivitySku();
        BeanUtils.copyProperties(activitySkuVO, activitySku);
        activitySkuMapper.insert(activitySku);
    }

    @Override
    public void updateActivitySkuVO(ActivitySkuVO activitySkuVO) {
        ActivitySku activitySku = new ActivitySku();
        BeanUtils.copyProperties(activitySkuVO, activitySku);
        activitySkuMapper.updateById(activitySku);
    }

    @Override
    public void deleteActivitySkuVO(Long activitySkuId) {
        activitySkuMapper.deleteById(activitySkuId);
    }

    @Override
    public List<BehaviorRebateVO> queryBehaviorRebateVOList() {
        List<BehaviorRebate> behaviorRebates = behaviorRebateMapper.selectList(null);
        behaviorRebates.sort((o1, o2) -> o2.getUpdateTime().compareTo(o1.getUpdateTime()));
        ArrayList<BehaviorRebateVO> list = new ArrayList<>();
        for (BehaviorRebate behaviorRebate : behaviorRebates) {
            BehaviorRebateVO behaviorRebateVO = new BehaviorRebateVO();
            BeanUtils.copyProperties(behaviorRebate, behaviorRebateVO);
            list.add(behaviorRebateVO);
        }
        return list;
    }

    @Override
    public void addBehaviorRebateVO(BehaviorRebateVO behaviorRebateVO) {
        BehaviorRebate behaviorRebate = new BehaviorRebate();
        BeanUtils.copyProperties(behaviorRebateVO, behaviorRebate);
        behaviorRebateMapper.insert(behaviorRebate);
    }

    @Override
    public void updateBehaviorRebateVO(BehaviorRebateVO behaviorRebateVO) {
        BehaviorRebate behaviorRebate = new BehaviorRebate();
        BeanUtils.copyProperties(behaviorRebateVO, behaviorRebate);
        behaviorRebateMapper.updateById(behaviorRebate);
    }

    @Override
    public void deleteBehaviorRebateVO(Long behaviorRebateId) {
        behaviorRebateMapper.deleteById(behaviorRebateId);
    }

    @Override
    public List<AwardResponseVO> queryAwardVOList() {
        List<Award> awards = awardMapper.queryAwardList();
        return awards.stream().map(award -> {
            AwardResponseVO awardResponseVO = new AwardResponseVO();
            BeanUtils.copyProperties(award, awardResponseVO);
            return awardResponseVO;
        }).toList();
    }

    @Override
    public void addAwardVO(AwardResponseVO awardResponseVO) {
        Award award = new Award();
        BeanUtils.copyProperties(awardResponseVO, award);
        awardMapper.insert(award);
    }

    @Override
    public void updateAwardVO(AwardResponseVO awardResponseVO) {
        Award award = new Award();
        BeanUtils.copyProperties(awardResponseVO, award);
        awardMapper.updateById(award);
        // 删除所有以lottery_strategy_award_list_key_开头的redis key
        redisService.deleteKeysWithPrefix(Constants.RedisKey.STRATEGY_AWARD_LIST_KEY);
    }

    @Override
    public void deleteAwardVO(Long awardId) {
        awardMapper.deleteById(awardId);
    }

    @Override
    public List<StrategyVO> queryStrategyVOList() {
        List<Strategy> strategies = strategyMapper.selectList(null);
        strategies.sort((o1, o2) -> o2.getUpdateTime().compareTo(o1.getUpdateTime()));
        return strategies.stream().map(strategy -> {
            StrategyVO strategyVO = new StrategyVO();
            BeanUtils.copyProperties(strategy, strategyVO);
            return strategyVO;
        }).toList();
    }

    @Override
    public void addStrategyVO(StrategyVO strategyVO) {
        Strategy strategy = new Strategy();
        BeanUtils.copyProperties(strategyVO, strategy);
        strategyMapper.insert(strategy);
    }

    @Override
    public void updateStrategyVO(StrategyVO strategyVO) {
        Strategy strategy = new Strategy();
        BeanUtils.copyProperties(strategyVO, strategy);
        strategyMapper.updateById(strategy);
    }

    @Override
    public void deleteStrategyVO(Long strategyId) {
        strategyMapper.deleteById(strategyId);
    }

    @Override
    public List<RuleVO> queryRuleVOList() {
        List<Rule> rules = ruleMapper.selectList(null);
        // 按更新时间倒序
        rules.sort((o1, o2) -> o2.getUpdateTime().compareTo(o1.getUpdateTime()));
        return rules.stream().map(rule -> {
            RuleVO ruleVO = new RuleVO();
            BeanUtils.copyProperties(rule, ruleVO);
            return ruleVO;
        }).toList();
    }

    @Override
    public void addRuleVO(RuleVO ruleVO) {
        Rule rule = new Rule();
        BeanUtils.copyProperties(ruleVO, rule);
        ruleMapper.insert(rule);
    }

    @Override
    public void updateRuleVO(RuleVO ruleVO) {
        Rule rule = new Rule();
        BeanUtils.copyProperties(ruleVO, rule);
        ruleMapper.updateById(rule);
    }

    @Override
    public void deleteRuleVO(Long ruleId) {
        ruleMapper.deleteById(ruleId);
    }

    @Override
    public List<StrategyAwardVO> queryStrategyAwardVOList() {
        List<StrategyAward> strategyAwards = strategyAwardMapper.selectList(null);
        strategyAwards.sort((o1, o2) -> o2.getUpdateTime().compareTo(o1.getUpdateTime()));
        return strategyAwards.stream().map(strategyAward -> {
            StrategyAwardVO strategyAwardVO = new StrategyAwardVO();
            BeanUtils.copyProperties(strategyAward, strategyAwardVO);
            return strategyAwardVO;
        }).toList();
    }

    @Override
    public void addStrategyAwardVO(StrategyAwardVO strategyAwardVO) {
        StrategyAward strategyAward = new StrategyAward();
        BeanUtils.copyProperties(strategyAwardVO, strategyAward);
        strategyAwardMapper.insert(strategyAward);
    }

    @Override
    public void updateStrategyAwardVO(StrategyAwardVO strategyAwardVO) {
        StrategyAward strategyAward = new StrategyAward();
        BeanUtils.copyProperties(strategyAwardVO, strategyAward);
        strategyAwardMapper.updateById(strategyAward);
        redisService.deleteKeysWithPrefix(Constants.RedisKey.STRATEGY_AWARD_LIST_KEY);
    }

    @Override
    public void deleteStrategyAwardVO(Long strategyAwardId) {
        strategyAwardMapper.deleteById(strategyAwardId);
    }

    @Override
    public List<RuleTreeVO> queryRuleTreeVOList() {
        List<RuleTree> ruleTrees = ruleTreeMapper.selectList(null);
        ruleTrees.sort((o1, o2) -> o2.getUpdateTime().compareTo(o1.getUpdateTime()));
        return ruleTrees.stream().map(ruleTree -> {
            RuleTreeVO ruleTreeVO = new RuleTreeVO();
            BeanUtils.copyProperties(ruleTree, ruleTreeVO);
            return ruleTreeVO;
        }).toList();
    }

    @Override
    public void addRuleTreeVO(RuleTreeVO ruleTreeVO) {
        RuleTree ruleTree = new RuleTree();
        BeanUtils.copyProperties(ruleTreeVO, ruleTree);
        ruleTreeMapper.insert(ruleTree);
    }

    @Override
    public void updateRuleTreeVO(RuleTreeVO ruleTreeVO) {
        RuleTree ruleTree = new RuleTree();
        BeanUtils.copyProperties(ruleTreeVO, ruleTree);
        ruleTreeMapper.updateById(ruleTree);
    }

    @Override
    public void deleteRuleTreeVO(Long ruleTreeId) {
        ruleTreeMapper.deleteById(ruleTreeId);
    }

    @Override
    public List<RuleTreeNodeVO> queryRuleTreeNodeVO() {
        List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeMapper.selectList(null);
        ruleTreeNodes.sort((o1, o2) -> o2.getUpdateTime().compareTo(o1.getUpdateTime()));
        return ruleTreeNodes.stream().map(ruleTreeNode -> {
            RuleTreeNodeVO ruleTreeNodeVO = new RuleTreeNodeVO();
            BeanUtils.copyProperties(ruleTreeNode, ruleTreeNodeVO);
            return ruleTreeNodeVO;
        }).toList();
    }

    @Override
    public void addRuleTreeNodeVO(RuleTreeNodeVO ruleTreeNodeVO) {
        RuleTreeNode ruleTreeNode = new RuleTreeNode();
        BeanUtils.copyProperties(ruleTreeNodeVO, ruleTreeNode);
        ruleTreeNodeMapper.insert(ruleTreeNode);
    }

    @Override
    public void updateRuleTreeNodeVO(RuleTreeNodeVO ruleTreeNodeVO) {
        RuleTreeNode ruleTreeNode = new RuleTreeNode();
        BeanUtils.copyProperties(ruleTreeNodeVO, ruleTreeNode);
        ruleTreeNodeMapper.updateById(ruleTreeNode);
    }

    @Override
    public void deleteRuleTreeNodeVO(Long ruleTreeNodeId) {
        ruleTreeNodeMapper.deleteById(ruleTreeNodeId);
    }

    @Override
    public List<RuleTreeNodeLineVO> queryRuleTreeNodeLineVO() {
        List<RuleTreeNodeLine> ruleTreeNodeLines = ruleTreeNodeLineMapper.selectList(null);
        ruleTreeNodeLines.sort((o1, o2) -> o2.getUpdateTime().compareTo(o1.getUpdateTime()));
        return ruleTreeNodeLines.stream().map(ruleTreeNodeLine -> {
            RuleTreeNodeLineVO ruleTreeNodeLineVO = new RuleTreeNodeLineVO();
            BeanUtils.copyProperties(ruleTreeNodeLine, ruleTreeNodeLineVO);
            return ruleTreeNodeLineVO;
        }).toList();
    }

    @Override
    public void addRuleTreeNodeLineVO(RuleTreeNodeLineVO ruleTreeNodeLineVO) {
        RuleTreeNodeLine ruleTreeNodeLine = new RuleTreeNodeLine();
        BeanUtils.copyProperties(ruleTreeNodeLineVO, ruleTreeNodeLine);
        ruleTreeNodeLineMapper.insert(ruleTreeNodeLine);
    }

    @Override
    public void updateRuleTreeNodeLineVO(RuleTreeNodeLineVO ruleTreeNodeLineVO) {
        RuleTreeNodeLine ruleTreeNodeLine = new RuleTreeNodeLine();
        BeanUtils.copyProperties(ruleTreeNodeLineVO, ruleTreeNodeLine);
        ruleTreeNodeLineMapper.updateById(ruleTreeNodeLine);
    }

    @Override
    public void deleteRuleTreeNodeLineVO(Long ruleTreeNodeLineId) {
        ruleTreeNodeLineMapper.deleteById(ruleTreeNodeLineId);
    }

    @Override
    public List<RuleTreeNodeVO> queryRuleTreeNodeVOByRuleTreeId(String ruleTreeId) {
        LambdaQueryWrapper<RuleTreeNode> queryWrapper = new LambdaQueryWrapper<RuleTreeNode>().eq(RuleTreeNode::getRuleTreeId, ruleTreeId);
        List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeMapper.selectList(queryWrapper);
        return ruleTreeNodes.stream().map(ruleTreeNodeLine -> {
            RuleTreeNodeVO ruleTreeNodeVO = new RuleTreeNodeVO();
            BeanUtils.copyProperties(ruleTreeNodeLine, ruleTreeNodeVO);
            return ruleTreeNodeVO;
        }).toList();
    }

    @Override
    public MyPage<EsUserAwardRecordVO> queryUserAwardRecordVOListByPage(Integer pageNum, Integer pageSize, Long activityId, String userId) {
        // 根据中奖时间倒序排序
        LambdaQueryWrapper<UserAwardRecord> queryWrapper = new LambdaQueryWrapper<UserAwardRecord>().orderByDesc(UserAwardRecord::getAwardTime);
        queryWrapper.eq(UserAwardRecord::getUserId, userId);
        queryWrapper.eq(UserAwardRecord::getActivityId, activityId);
        Page<UserAwardRecord> page = new Page<>(pageNum, pageSize);
        MyPage<EsUserAwardRecordVO> myPage = new MyPage<>();
        try {
            dbRouter.doRouter(userId);
            Page<UserAwardRecord> userAwardRecordPage = userAwardRecordMapper.selectPage(page, queryWrapper);
            if (userAwardRecordPage.getRecords().isEmpty()){
                return myPage;
            }
            String key = Constants.RedisKey.STRATEGY_AWARD_LIST_KEY + userAwardRecordPage.getRecords().get(0).getStrategyId();
            ArrayList<StrategyAwardEntity> arrayList = redisService.getValue(key);
            // 将所有数据 awardId 作为键 image 作为值封装为一个map集合
            Map<Long, String> awardMap = arrayList.stream().collect(Collectors.toMap(StrategyAwardEntity::getAwardId, StrategyAwardEntity::getImage));
            List<EsUserAwardRecordVO> list = userAwardRecordPage.getRecords().stream().map(userAwardRecord -> {
                EsUserAwardRecordVO esUserAwardRecordVO = new EsUserAwardRecordVO();
                BeanUtils.copyProperties(userAwardRecord, esUserAwardRecordVO);
                // 根据奖品 id 从 reids 中获取图片url
                esUserAwardRecordVO.setImage(awardMap.get(userAwardRecord.getAwardId()));
                return esUserAwardRecordVO;
            }).toList();
            myPage.setTotal(userAwardRecordPage.getTotal());
            myPage.setItems(list);
            return myPage;
        } finally {
            dbRouter.clear();
        }
    }
}
