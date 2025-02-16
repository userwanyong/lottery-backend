package com.lottery.domain.strategy.service.lottery;

import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.model.entity.RuleFilterReqEntity;
import com.lottery.domain.strategy.model.entity.RuleFilterResEntity;
import com.lottery.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.lottery.domain.strategy.model.valobj.RuleTreeVO;
import com.lottery.domain.strategy.model.valobj.StrategyRuleModelVO;
import com.lottery.domain.strategy.repository.LotteryRepository;
import com.lottery.domain.strategy.service.AbstractLottery;
import com.lottery.domain.strategy.service.rule.chain.LogicChain;
import com.lottery.domain.strategy.service.rule.chain.factory.DefaultLogicChainFactory;
import com.lottery.domain.strategy.service.rule.filter.LogicFilter;
import com.lottery.domain.strategy.service.rule.filter.factory.DefaultLogicFilterFactory;
import com.lottery.domain.strategy.service.rule.tree.factory.DefaultLogicTreeFactory;
import com.lottery.domain.strategy.service.rule.tree.factory.engine.DecisionTreeEngine;
import com.lottery.domain.strategy.service.strategy.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 永
 * 抽奖流程的默认实现
 */
@Slf4j
@Service
public class DefaultLottery extends AbstractLottery {

    @Resource
    private DefaultLogicFilterFactory logicFactory;

    public DefaultLottery(LotteryRepository repository, StrategyService strategyService, DefaultLogicChainFactory defaultLogicChainFactory, DefaultLogicTreeFactory defaultLogicTreeFactory) {
        super(repository, strategyService, defaultLogicChainFactory, defaultLogicTreeFactory);
    }


    @Override
    protected RuleFilterResEntity<RuleFilterResEntity.LotteryCenterEntity> doCheckLotteryCenterLogic(LotteryReqEntity lotteryReqEntity, String... logics) {
        if (logics == null || 0 == logics.length) {
            return RuleFilterResEntity.<RuleFilterResEntity.LotteryCenterEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .message(RuleLogicCheckTypeVO.ALLOW.getMessage())
                    .build();
        }
        // 1. 获取规则过滤器组
        Map<String, LogicFilter<RuleFilterResEntity.LotteryCenterEntity>> logicFilterGroup = logicFactory.openLogicFilter();
        // 2. 过滤解锁规则
        RuleFilterResEntity<RuleFilterResEntity.LotteryCenterEntity> ruleFilterResEntity = null;
        for (String ruleModel : logics) {
            // 得到相应规则过滤器
            LogicFilter<RuleFilterResEntity.LotteryCenterEntity> logicFilter = logicFilterGroup.get(ruleModel);

            RuleFilterReqEntity ruleFilterReqEntity = new RuleFilterReqEntity();
            ruleFilterReqEntity.setUserId(lotteryReqEntity.getUserId());
            ruleFilterReqEntity.setAwardId(lotteryReqEntity.getAwardId());
            ruleFilterReqEntity.setStrategyId(lotteryReqEntity.getStrategyId());
            ruleFilterReqEntity.setRuleModel(ruleModel);

            ruleFilterResEntity = logicFilter.filter(ruleFilterReqEntity);

            // 是放行结果则继续过滤
            log.info("抽奖中规则过滤 userId: {} ruleModel: {} code: {} info: {}", lotteryReqEntity.getUserId(), ruleModel, ruleFilterResEntity.getCode(), ruleFilterResEntity.getMessage());
            if (RuleLogicCheckTypeVO.ALLOW.getCode() != ruleFilterResEntity.getCode()) {
                return ruleFilterResEntity;
            }
        }

        return ruleFilterResEntity;
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
        if (strategyRuleModelVO == null){
            return RuleEntity.builder()
                    .awardId(awardId)
                    .build();
        }
        // 2. 根据规则模型查规则树
        RuleTreeVO ruleTreeVO =repository.queryRuleTreeVO(strategyRuleModelVO.getRuleModels());
        if (ruleTreeVO == null) {
            throw new RuntimeException("存在抽奖策略配置的规则模型 Key，未在库表 rule_tree、rule_tree_node、rule_tree_line 配置对应的规则树信息 " + strategyRuleModelVO.getRuleModels());
        }
        // 3. 获取规则树引擎
        DecisionTreeEngine decisionTreeEngine = defaultLogicTreeFactory.openLogicTree(ruleTreeVO);
        // 4. 执行规则树
        return decisionTreeEngine.process(userId, strategyId, awardId);
    }




}

