package com.lottery.domain.strategy.service.rule.chain.factory;

import com.lottery.domain.strategy.model.entity.StrategyEntity;
import com.lottery.domain.strategy.repository.StrategyRepository;
import com.lottery.domain.strategy.service.rule.chain.LogicChain;
import com.lottery.types.common.Constants;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * @author 永
 * 责任链工厂
 */
@Service
public class DefaultLogicChainFactory {
    private final Map<String, LogicChain> logicChainGroup;
    protected StrategyRepository repository;

    public DefaultLogicChainFactory(Map<String, LogicChain> logicChainGroup, StrategyRepository repository) {
        this.logicChainGroup = logicChainGroup;
        this.repository = repository;
    }

    /**
     * 构建责任链
     */
    public LogicChain openLogicChain(Long strategyId) {
        // 1. 查询策略
        StrategyEntity strategy = repository.queryStrategyEntityByStrategyId(strategyId);
        String[] ruleModels = strategy.ruleModels();
        // 2. 如果未配置策略规则，则装填默认责任链
        if (ruleModels == null || ruleModels.length == 0) {
            return logicChainGroup.get(Constants.RuleModel.DEFAULT);
        }
        // 3. 依次装填责任链；rule_blacklist、rule_weight
        LogicChain logicChain = logicChainGroup.get(ruleModels[0]);
        LogicChain current = logicChain;
        for (int i = 1; i < ruleModels.length; i++) {
            LogicChain nextChain = logicChainGroup.get(ruleModels[i]);
            current = current.appendNext(nextChain);
        }
        // 4. 责任链的最后装填默认责任链
        current.appendNext(logicChainGroup.get(Constants.RuleModel.DEFAULT));
        // 5. 返回责任链
        return logicChain;
    }
}
