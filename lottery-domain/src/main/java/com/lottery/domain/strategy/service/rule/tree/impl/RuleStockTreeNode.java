package com.lottery.domain.strategy.service.rule.tree.impl;

import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.lottery.domain.strategy.repository.StrategyRepository;
import com.lottery.domain.strategy.service.rule.tree.LogicTree;
import com.lottery.domain.strategy.service.rule.tree.factory.DefaultLogicTreeFactory;
import com.lottery.domain.strategy.service.armory.StrategyService;
import com.lottery.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 永
 * 规则树-库存扣减节点
 */
@Component(Constants.RuleModel.RULE_STOCK)
@Slf4j
public class RuleStockTreeNode implements LogicTree {

    @Resource
    private StrategyService strategyService;
    @Resource
    private StrategyRepository repository;

    @Override
    public DefaultLogicTreeFactory.TreeActionEntity logic(String userId, Long strategyId,Long activityId, Long awardId, String ruleValue) {
        log.info("【规则树 RuleStockTreeNode-库存扣减节点开始执行】");
        // 扣减库存
        Boolean result = strategyService.reduceAwardStock(strategyId, activityId,awardId);
        // 扣减成功，放行
        if (result) {
            log.info("【规则树 RuleStockTreeNode】-库存扣减-成功-放行 userId:{} strategyId:{} awardId:{}", userId, strategyId, awardId);
            // 写入延迟队列，延迟消费 更新数据库记录
            repository.awardStockConsumeSendQueue(LotteryReqEntity.builder()
                    .strategyId(strategyId)
                    .awardId(awardId)
                    .build());
            return DefaultLogicTreeFactory.TreeActionEntity.builder()
                    .ruleLogicCheckType(RuleLogicCheckTypeVO.ALLOW)
                    .ruleEntity(RuleEntity.builder()
                            .awardId(awardId)
                            .ruleValue(ruleValue)
                            .build())
                    .build();
        }
        // 否则拦截
        log.info("【规则树 RuleStockTreeNode】-库存扣减-失败-拦截 userId:{} strategyId:{} awardId:{}", userId, strategyId, awardId);
        return DefaultLogicTreeFactory.TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .build();
    }
}
