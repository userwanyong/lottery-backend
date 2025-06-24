package com.lottery.querys.adapter.repository;


import com.lottery.querys.model.valobj.*;

import java.util.List;

/**
 * @author 永
 */
public interface ErpRepository {
    List<ActivityVO> queryActivityVOList();

    void addActivityVO(ActivityVO activityVO);

    void updateActivityVO(ActivityVO activityVO);

    void deleteActivityVO(Long activityId);

    List<ActivityCountVO> queryActivityCountVOList();

    void addActivityCountVO(ActivityCountVO activityCountVO);

    void updateActivityCountVO(ActivityCountVO activityCountVO);

    void deleteActivityCountVO(Long activityCountId);

    List<ActivitySkuVO> queryActivitySkuVOList();

    void addActivitySkuVO(ActivitySkuVO activitySkuVO);

    void updateActivitySkuVO(ActivitySkuVO activitySkuVO);

    void deleteActivitySkuVO(Long activitySkuId);

    List<BehaviorRebateVO> queryBehaviorRebateVOList();

    void addBehaviorRebateVO(BehaviorRebateVO behaviorRebateVO);

    void updateBehaviorRebateVO(BehaviorRebateVO behaviorRebateVO);

    void deleteBehaviorRebateVO(Long behaviorRebateId);

    List<AwardResponseVO> queryAwardVOList();

    void addAwardVO(AwardResponseVO awardResponseVO);

    void updateAwardVO(AwardResponseVO awardResponseVO);

    void deleteAwardVO(Long awardId);

    List<StrategyVO> queryStrategyVOList();

    void addStrategyVO(StrategyVO strategyVO);

    void updateStrategyVO(StrategyVO strategyVO);

    void deleteStrategyVO(Long strategyId);

    List<RuleVO> queryRuleVOList();

    void addRuleVO(RuleVO ruleVO);

    void updateRuleVO(RuleVO ruleVO);

    void deleteRuleVO(Long ruleId);

    List<StrategyAwardVO> queryStrategyAwardVOList();

    void addStrategyAwardVO(StrategyAwardVO strategyAwardVO);

    void updateStrategyAwardVO(StrategyAwardVO strategyAwardVO);

    void deleteStrategyAwardVO(Long strategyAwardId);

    List<RuleTreeVO> queryRuleTreeVOList();
}
