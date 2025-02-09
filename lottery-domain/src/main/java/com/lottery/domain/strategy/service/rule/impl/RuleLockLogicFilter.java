package com.lottery.domain.strategy.service.rule.impl;

import com.lottery.domain.strategy.model.entity.RuleFilterReqEntity;
import com.lottery.domain.strategy.model.entity.RuleFilterResEntity;
import com.lottery.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.lottery.domain.strategy.repository.StrategyRepository;
import com.lottery.domain.strategy.service.annotation.LogicStrategy;
import com.lottery.domain.strategy.service.rule.LogicFilter;
import com.lottery.domain.strategy.service.rule.factory.DefaultLogicFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * @author 永
 * 抽奖中-判断是否符合解锁奖品的条件（抽奖次数是否达到）
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_LOCK) // 如果存在解锁规则，则使用这个过滤器
public class RuleLockLogicFilter implements LogicFilter<RuleFilterResEntity.LotteryCenterEntity> {

    @Resource
    private StrategyRepository repository;

    // TODO 用户抽奖次数，后期从数据库查询
    private Long userLotteryCount = 0L;

    @Override
    public RuleFilterResEntity<RuleFilterResEntity.LotteryCenterEntity> filter(RuleFilterReqEntity ruleFilterReqEntity) {
        log.info("规则过滤-次数锁 userId:{} strategyId:{} ruleModel:{}", ruleFilterReqEntity.getUserId(), ruleFilterReqEntity.getStrategyId(), ruleFilterReqEntity.getRuleModel());
        // 1. 查询规则的值（解锁所需次数，如3）
        String ruleValue = repository.queryStrategyRuleValue(ruleFilterReqEntity.getStrategyId(), ruleFilterReqEntity.getAwardId(), ruleFilterReqEntity.getRuleModel());
        // 2. 判断用户抽奖次数
        // 小于等于该值，拦截
        if (userLotteryCount < Long.parseLong(ruleValue)) {
            return RuleFilterResEntity.<RuleFilterResEntity.LotteryCenterEntity>builder()
                    .data(RuleFilterResEntity.LotteryCenterEntity.builder()
                            .awardId(101L) //todo 兜底奖品,后续更改
                            .build())
                    .ruleModel(DefaultLogicFactory.LogicModel.RULE_LOCK.getCode())
                    .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                    .message(RuleLogicCheckTypeVO.TAKE_OVER.getMessage())
                    .build();
        }
        // 大于等于该值，放行
        return RuleFilterResEntity.<RuleFilterResEntity.LotteryCenterEntity>builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .message(RuleLogicCheckTypeVO.ALLOW.getMessage())
                .build();
    }
}
