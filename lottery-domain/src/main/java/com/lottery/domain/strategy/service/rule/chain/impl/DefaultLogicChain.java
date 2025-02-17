package com.lottery.domain.strategy.service.rule.chain.impl;

import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.lottery.domain.strategy.service.strategy.StrategyService;
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
    public RuleEntity logic(String userId, Long strategyId) {
        Long awardId = strategyService.getRandomAwardId(strategyId);
        log.info("【责任链】-默认处理 userId: {} strategyId: {} awardId: {} ruleModel: {}", userId, strategyId, awardId, Constants.RuleModel.DEFAULT);
        return RuleEntity.builder()
                .awardId(awardId)
                .ruleModel(Constants.RuleModel.DEFAULT)
                .build();
    }
}
