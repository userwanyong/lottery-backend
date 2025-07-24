package com.lottery.domain.strategy.service;

import com.lottery.domain.strategy.model.valobj.RuleWeightVO;

import java.util.List;
import java.util.Map;

/**
 * @author 永
 * 策略-抽奖领域-规则相关操作
 */
public interface Rule {
    Map<Long, Integer> queryAwardRuleLockCount(Long[] treeIds);

    List<RuleWeightVO> queryStrategyRuleWeight(Long activityId);
}
