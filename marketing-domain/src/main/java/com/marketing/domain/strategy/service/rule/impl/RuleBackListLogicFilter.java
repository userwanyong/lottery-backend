package com.marketing.domain.strategy.service.rule.impl;

import com.marketing.domain.strategy.model.entity.RuleFilterResEntity;
import com.marketing.domain.strategy.model.entity.RuleFilterReqEntity;
import com.marketing.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.marketing.domain.strategy.repository.StrategyRepository;
import com.marketing.domain.strategy.service.annotation.LogicStrategy;
import com.marketing.domain.strategy.service.rule.LogicFilter;
import com.marketing.domain.strategy.service.rule.factory.DefaultLogicFactory;
import com.marketing.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * @author 永
 * 抽奖前-黑名单用户过滤规则
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.RULE_BLACKLIST) // 如果存在黑名单规则，则使用这个过滤器
public class RuleBackListLogicFilter implements LogicFilter<RuleFilterResEntity.LotteryBeforeEntity> {

    @Resource
    private StrategyRepository repository;

    @Override
    public RuleFilterResEntity<RuleFilterResEntity.LotteryBeforeEntity> filter(RuleFilterReqEntity ruleFilterReqEntity) {
        log.info("规则过滤-黑名单 userId:{} strategyId:{} ruleModel:{}", ruleFilterReqEntity.getUserId(), ruleFilterReqEntity.getStrategyId(), ruleFilterReqEntity.getRuleModel());

        String userId = ruleFilterReqEntity.getUserId();

        // 1. 查询规则的值
        String ruleValue = repository.queryStrategyRuleValue(ruleFilterReqEntity.getStrategyId(), ruleFilterReqEntity.getAwardId(), ruleFilterReqEntity.getRuleModel());
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        Long awardId = Long.valueOf(splitRuleValue[0]);

        // 2. 查询黑名单用户
        String[] userBlackIds = splitRuleValue[1].split(Constants.SPLIT);

        // 如果用户在黑名单中，进行接管
        for (String userBlackId : userBlackIds) {
            if (userId.equals(userBlackId)) {
                return RuleFilterResEntity.<RuleFilterResEntity.LotteryBeforeEntity>builder()
                        .ruleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode())
                        .data(RuleFilterResEntity.LotteryBeforeEntity.builder()
                                .strategyId(ruleFilterReqEntity.getStrategyId())
                                .awardId(awardId)
                                .build())
                        .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                        .message(RuleLogicCheckTypeVO.TAKE_OVER.getMessage())
                        .build();
            }
        }

        // 否则放行
        return RuleFilterResEntity.<RuleFilterResEntity.LotteryBeforeEntity>builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .message(RuleLogicCheckTypeVO.ALLOW.getMessage())
                .build();
    }

}

