package com.lottery.domain.strategy.service.rule.chain.impl;

import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.repository.StrategyRepository;
import com.lottery.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.lottery.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * @author 永
 * 抽奖前-黑名单责任链
 */
@Slf4j
@Component(Constants.RuleModel.RULE_BLACKLIST)
public class RuleBackListLogicChain extends AbstractLogicChain {

    @Resource
    private StrategyRepository repository;

    @Override
    public RuleEntity logic(String userId, Long strategyId) {
        log.info("【责任链】-黑名单开始 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, Constants.RuleModel.RULE_BLACKLIST);
        // 1. 查询规则的值
        String ruleValue = repository.queryStrategyRuleValue(strategyId, Constants.RuleModel.RULE_BLACKLIST);
        String[] splitRuleValue = ruleValue.split(Constants.COLON);
        Long awardId = Long.valueOf(splitRuleValue[0]);

        // 2. 查询该值对应的黑名单用户
        String[] userBlackIds = splitRuleValue[1].split(Constants.SPLIT);

        // 如果用户在黑名单中，进行接管
        for (String userBlackId : userBlackIds) {
            if (userId.equals(userBlackId)) {
                log.info("【责任链】-黑名单接管 userId: {} strategyId: {} ruleModel: {} awardId: {}", userId, strategyId, Constants.RuleModel.RULE_BLACKLIST, awardId);
                return RuleEntity.builder()
                        .awardId(awardId)
                        .ruleValue("0.01,1")
                        .ruleModel(Constants.RuleModel.RULE_BLACKLIST)
                        .build();
            }
        }
        // 否则过滤其他责任链
        log.info("【责任链】-黑名单放行 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, Constants.RuleModel.RULE_BLACKLIST);
        return next().logic(userId, strategyId);
    }
}
