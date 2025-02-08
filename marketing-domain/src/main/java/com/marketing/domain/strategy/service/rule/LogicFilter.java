package com.marketing.domain.strategy.service.rule;

import com.marketing.domain.strategy.model.entity.RuleFilterResEntity;
import com.marketing.domain.strategy.model.entity.RuleFilterReqEntity;

/**
 * @author 永
 * 抽奖规则过滤接口
 */
public interface LogicFilter<T extends RuleFilterResEntity.LotteryEntity> {

    RuleFilterResEntity<T> filter(RuleFilterReqEntity ruleFilterReqEntity);

}
