package com.lottery.domain.strategy.service;

import com.lottery.domain.channel.service.ChannelService;
import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.model.entity.StrategyAwardEntity;
import com.lottery.domain.strategy.model.valobj.RuleTreeVO;
import com.lottery.domain.strategy.model.valobj.RuleWeightVO;
import com.lottery.domain.strategy.repository.StrategyRepository;
import com.lottery.domain.strategy.service.armory.StrategyService;
import com.lottery.domain.strategy.service.rule.chain.LogicChain;
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

    public DefaultLottery(StrategyRepository repository, StrategyService strategyService, ChannelService channelService, DefaultLogicChainFactory defaultLogicChainFactory, DefaultLogicTreeFactory defaultLogicTreeFactory) {
        super(repository, strategyService, channelService, defaultLogicChainFactory, defaultLogicTreeFactory);
    }

    @Override
    public RuleEntity logicChain(String userId, Long strategyId, Long activityId) {
        // 1. 获取责任链
        LogicChain logicChain = defaultLogicChainFactory.getLogicChain(strategyId);
        // 2. 依次执行责任链
        return logicChain.logic(userId, strategyId, activityId);
    }

    @Override
    public RuleEntity logicTree(String userId, Long strategyId, Long activityId, Long awardId) {
        // 1. 查规则模型，如果为空，说明未设置规则，直接返回抽到的奖品实体
        Long ruleTreeId = repository.queryRuleModelVO(strategyId, awardId);
        if (ruleTreeId == 0L) {
            return RuleEntity.builder()
                    .awardId(awardId)
                    .build();
        }
        // 2. 根据规则模型查数据库表构建规则树树根
        RuleTreeVO ruleTreeVO = repository.queryRuleTreeVO(ruleTreeId);
        if (ruleTreeVO == null) {
            log.error("[DefaultLottery.lotteryLogicTree]存在奖品规则模型 id:{},但未在库表配置对应的规则树信息", ruleTreeId);
            throw new RuntimeException("[DefaultLottery.lotteryLogicTree]存在奖品规则模型 id: " + ruleTreeId + ",但未在库表配置对应的规则树信息");
        }
        // 3. 获取规则树引擎
        DecisionTreeEngine decisionTreeEngine = defaultLogicTreeFactory.getLogicTree(ruleTreeVO);
        // 4. 执行规则树
        return decisionTreeEngine.process(userId, strategyId, activityId, awardId);
    }


    @Override
    public LotteryReqEntity takeQueueValue(String strategyAward) {
        return repository.takeQueueValue(strategyAward);
    }

    @Override
    public void updateStrategyAwardStock(Long strategyId, Long awardId) {
        repository.updateStrategyAwardStock(strategyId, awardId);
    }

    @Override
    public List<String> getStrategyAwardList() {
        return repository.getStrategyAwardList();
    }

    @Override
    public void clearAwardStock(String strategyAward) {
        repository.clearAwardStock(strategyAward);
    }

    @Override
    public void clearQueueValue(String strategyAward) {
        repository.clearQueueValue(strategyAward);
    }

    @Override
    public List<StrategyAwardEntity> queryLotteryAwardList(Long strategyId) {
        return repository.queryStrategyAwardList(strategyId);
    }

    @Override
    public List<StrategyAwardEntity> queryLotteryAwardListByActivityId(Long activityId) {
        Long strategyId = repository.queryStrategyIdByActivityId(activityId);
        return queryLotteryAwardList(strategyId);
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

