package com.lottery.domain.strategy.service.rule.chain.impl;

import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.service.armory.StrategyService;
import com.lottery.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.lottery.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;

/**
 * @author 永
 * 抽奖前-默认责任链
 */
@Slf4j
@Component(Constants.RuleModel.DEFAULT)
public class DefaultLogicChain extends AbstractLogicChain {

    @Resource
    private StrategyService strategyService;

    @Override
    public RuleEntity logic(String userId, Long strategyId, Long activityId) {
        Long awardId = strategyService.getRandomAwardId(activityId);
        log.info("【抽奖责任链-DefaultLogicChain】-默认处理 userId:{} strategyId:{} activityId:{} awardId:{}", userId, strategyId, activityId, awardId);
        return RuleEntity.builder()
                .awardId(awardId)
                .ruleModel(Constants.RuleModel.DEFAULT)
                .build();
    }
}
