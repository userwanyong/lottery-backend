package com.lottery.domain.strategy.service;

import com.lottery.domain.channel.service.ChannelService;
import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.model.entity.StrategyAwardEntity;
import com.lottery.domain.strategy.model.valobj.RuleTreeVO;
import com.lottery.domain.strategy.model.valobj.RuleWeightVO;
import com.lottery.domain.strategy.repository.StrategyRepository;
import com.lottery.domain.strategy.service.armory.StrategyService;
import com.lottery.domain.strategy.service.rule.chain.factory.DefaultLogicChainFactory;
import com.lottery.domain.strategy.service.rule.tree.factory.DefaultLogicTreeFactory;
import com.lottery.domain.strategy.service.rule.tree.factory.engine.DecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author 永
 * 策略-抽奖领域-抽奖流程的默认实现
 */
@Slf4j
@Service
public class DefaultLottery extends AbstractLottery implements Stock, Rule {

    public DefaultLottery(
            StrategyRepository repository,
            StrategyService strategyService,
            ChannelService channelService,
            DefaultLogicChainFactory defaultLogicChainFactory,
            DefaultLogicTreeFactory defaultLogicTreeFactory
    ) {
        super(repository, strategyService, channelService, defaultLogicChainFactory, defaultLogicTreeFactory);
    }

    @Override
    public RuleEntity logicChain(String userId, Long strategyId, Long activityId) {
        // 1. 获取责任链（带缓存）
        DefaultLogicChainFactory.ChainedLogic chainedLogic = defaultLogicChainFactory.getChainedLogic(strategyId);
        // 2. 执行责任链
        return chainedLogic.execute(userId, strategyId, activityId);
    }

    @Override
    public RuleEntity logicTree(String userId, Long strategyId, Long activityId, Long awardId) {
        Long ruleTreeId = repository.queryRuleModelVO(activityId, awardId);
        if (ruleTreeId == 0L) {
            return RuleEntity.builder().awardId(awardId).build();
        }
        RuleTreeVO ruleTreeVO = repository.queryRuleTreeVO(ruleTreeId);
        if (ruleTreeVO == null) {
            throw new RuntimeException("rule tree is not configured, treeId=" + ruleTreeId);
        }
        DecisionTreeEngine decisionTreeEngine = defaultLogicTreeFactory.getLogicTree(ruleTreeVO);
        return decisionTreeEngine.process(userId, strategyId, activityId, awardId);
    }

    @Override
    public LotteryReqEntity takeQueueValue(String activityAward) {
        return repository.takeQueueValue(activityAward);
    }

    @Override
    public void updateActivityAwardStock(Long activityId, Long awardId) {
        repository.updateActivityAwardStock(activityId, awardId);
    }

    @Override
    public List<String> getActivityAwardList() {
        return repository.getActivityAwardList();
    }

    @Override
    public void clearAwardStock(String activityAward) {
        repository.clearAwardStock(activityAward);
    }

    @Override
    public void clearQueueValue(String activityAward) {
        repository.clearQueueValue(activityAward);
    }

    @Override
    public List<StrategyAwardEntity> queryLotteryAwardListByActivityId(Long activityId) {
        return repository.queryActivityAwardList(activityId);
    }

    @Override
    public Map<Long, Integer> queryAwardRuleLockCount(Long[] treeIds) {
        return repository.queryAwardRuleLockCount(treeIds);
    }

    @Override
    public List<RuleWeightVO> queryStrategyRuleWeight(Long activityId) {
        return repository.queryStrategyRuleWeight(activityId);
    }
}

