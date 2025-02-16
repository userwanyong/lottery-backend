package com.lottery.domain.strategy.service.rule.filter.impl;

import com.lottery.domain.strategy.model.entity.RuleFilterReqEntity;
import com.lottery.domain.strategy.model.entity.RuleFilterResEntity;
import com.lottery.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.lottery.domain.strategy.repository.LotteryRepository;
import com.lottery.domain.strategy.service.annotation.LogicStrategy;
import com.lottery.domain.strategy.service.rule.filter.LogicFilter;
import com.lottery.domain.strategy.service.rule.filter.factory.DefaultLogicFilterFactory;
import com.lottery.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 永
 * 抽奖前-黑名单过滤规则
 */
@Deprecated
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFilterFactory.LogicModel.RULE_BLACKLIST) // 如果存在黑名单规则，则使用这个过滤器
public class RuleBackListLogicFilter implements LogicFilter<RuleFilterResEntity.LotteryBeforeEntity> {

    @Resource
    private LotteryRepository repository;

    @Override
    public RuleFilterResEntity<RuleFilterResEntity.LotteryBeforeEntity> filter(RuleFilterReqEntity ruleFilterReqEntity) {
        log.info("规则过滤-黑名单过滤开始 userId:{} strategyId:{} ruleModel:{}", ruleFilterReqEntity.getUserId(), ruleFilterReqEntity.getStrategyId(), ruleFilterReqEntity.getRuleModel());

        String userId = ruleFilterReqEntity.getUserId();

        // 1. 查询规则的值
        String ruleValue = repository.queryStrategyRuleValue(ruleFilterReqEntity.getStrategyId(), ruleFilterReqEntity.getAwardId(), ruleFilterReqEntity.getRuleModel());
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        Long awardId = Long.valueOf(splitRuleValue[0]);

        // 2. 查询该值对应的黑名单用户
        String[] userBlackIds = splitRuleValue[1].split(Constants.SPLIT);

        // 如果用户在黑名单中，进行接管
        for (String userBlackId : userBlackIds) {
            if (userId.equals(userBlackId)) {
                log.info("规则过滤-黑名单接管 userId: {} strategyId: {} ruleModel: {} awardId: {}", ruleFilterReqEntity.getStrategyId(), ruleFilterReqEntity.getAwardId(), ruleFilterReqEntity.getRuleModel(), awardId);
                return RuleFilterResEntity.<RuleFilterResEntity.LotteryBeforeEntity>builder()
                        .data(RuleFilterResEntity.LotteryBeforeEntity.builder()
                                .strategyId(ruleFilterReqEntity.getStrategyId())
                                .awardId(awardId)
                                .build())
                        .ruleModel(DefaultLogicFilterFactory.LogicModel.RULE_BLACKLIST.getCode())
                        .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                        .message(RuleLogicCheckTypeVO.TAKE_OVER.getMessage())
                        .build();
            }
        }

        // 否则放行
        log.info("规则过滤-黑名单放行 userId: {} strategyId: {} ruleModel: {}", ruleFilterReqEntity.getStrategyId(), ruleFilterReqEntity.getAwardId(), ruleFilterReqEntity.getRuleModel());
        return RuleFilterResEntity.<RuleFilterResEntity.LotteryBeforeEntity>builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .message(RuleLogicCheckTypeVO.ALLOW.getMessage())
                .build();
    }

}

