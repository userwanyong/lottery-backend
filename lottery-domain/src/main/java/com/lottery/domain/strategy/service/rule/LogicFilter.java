package com.lottery.domain.strategy.service.rule;

import com.lottery.domain.strategy.model.entity.RuleFilterReqEntity;
import com.lottery.domain.strategy.model.entity.RuleFilterResEntity;

/**
 * @author 永
 * 抽奖规则过滤接口
 */
public interface LogicFilter<T extends RuleFilterResEntity.LotteryEntity> {

    RuleFilterResEntity<T> filter(RuleFilterReqEntity ruleFilterReqEntity);

}
