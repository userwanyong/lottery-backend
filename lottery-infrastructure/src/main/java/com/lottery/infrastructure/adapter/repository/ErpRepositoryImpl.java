package com.lottery.infrastructure.adapter.repository;


import com.lottery.infrastructure.dao.*;
import com.lottery.infrastructure.dao.po.*;
import com.lottery.querys.adapter.repository.ErpRepository;
import com.lottery.querys.model.valobj.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
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
}
