package com.lottery.domain.strategy.service;

import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.model.entity.StrategyAwardEntity;
import com.lottery.domain.strategy.model.valobj.RuleTreeVO;
import com.lottery.domain.strategy.model.valobj.StrategyRuleModelVO;
import com.lottery.domain.strategy.repository.StrategyRepository;
import com.lottery.domain.strategy.service.rule.chain.LogicChain;
import com.lottery.domain.strategy.service.rule.chain.factory.DefaultLogicChainFactory;
import com.lottery.domain.strategy.service.rule.tree.factory.DefaultLogicTreeFactory;
import com.lottery.domain.strategy.service.rule.tree.factory.engine.DecisionTreeEngine;
import com.lottery.domain.strategy.service.armory.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 永
 * 策略-抽奖领域-抽奖流程的默认实现
 */
@Slf4j
@Service
public class DefaultLottery extends AbstractLottery implements Stock {

    public DefaultLottery(StrategyRepository repository, StrategyService strategyService, DefaultLogicChainFactory defaultLogicChainFactory, DefaultLogicTreeFactory defaultLogicTreeFactory) {
        super(repository, strategyService, defaultLogicChainFactory, defaultLogicTreeFactory);
    }

    @Override
    public RuleEntity lotteryLogicChain(String userId, Long strategyId) {
        // 1. 获取责任链
        LogicChain logicChain = defaultLogicChainFactory.openLogicChain(strategyId);
        // 2. 依次执行责任链
        return logicChain.logic(userId, strategyId);
    }

    @Override
    public RuleEntity lotteryLogicTree(String userId, Long strategyId, Long awardId) {
        // 1. 查规则模型，如果为空，说明未设置规则，直接返回抽到的奖品实体
        StrategyRuleModelVO strategyRuleModelVO = repository.queryRuleModelVO(strategyId, awardId);
        if (strategyRuleModelVO == null) {
            return RuleEntity.builder()
                    .awardId(awardId)
                    .build();
        }
        // 2. 根据规则模型查数据库表构建规则树树根
        RuleTreeVO ruleTreeVO = repository.queryRuleTreeVO(strategyRuleModelVO.getRuleModels());
        if (ruleTreeVO == null) {
            throw new RuntimeException("存在抽奖策略配置的规则模型 Key，未在库表 rule_tree、rule_tree_node、rule_tree_line 配置对应的规则树信息 " + strategyRuleModelVO.getRuleModels());
        }
        // 3. 获取规则树引擎
        DecisionTreeEngine decisionTreeEngine = defaultLogicTreeFactory.openLogicTree(ruleTreeVO);
        // 4. 执行规则树
        return decisionTreeEngine.process(userId, strategyId, awardId);
    }


    @Override
    public LotteryReqEntity takeQueueValue() {
        return repository.takeQueueValue();
    }

    @Override
    public void updateStrategyAwardStock(Long strategyId, Long awardId) {
        repository.updateStrategyAwardStock(strategyId, awardId);
    }

    @Override
    public List<StrategyAwardEntity> queryLotteryAwardList(Long strategyId) {
        return repository.queryStrategyAwardList(strategyId);
    }
}

