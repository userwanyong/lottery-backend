package com.lottery.domain.strategy.service.rule.chain.factory;

import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.model.entity.StrategyEntity;
import com.lottery.domain.strategy.repository.StrategyRepository;
import com.lottery.domain.strategy.service.rule.chain.LogicChain;
import com.lottery.types.common.Constants;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 永
 * 责任链工厂
 */
@Service
public class DefaultLogicChainFactory {
    private final Map<String, LogicChain> logicChainGroup;
    protected StrategyRepository repository;
    private final ConcurrentHashMap<Long, ChainedLogic> chainCache = new ConcurrentHashMap<>();

    public DefaultLogicChainFactory(@Lazy Map<String, LogicChain> logicChainGroup, StrategyRepository repository) {
        this.logicChainGroup = logicChainGroup;
        this.repository = repository;
    }

    /**
     * 获取责任链（带缓存）
     */
    public ChainedLogic getChainedLogic(Long strategyId) {
        return chainCache.computeIfAbsent(strategyId, this::buildChainedLogic);
    }

    /**
     * 清除责任链缓存
     */
    public void clearChainCache(Long strategyId) {
        chainCache.remove(strategyId);
    }

    /**
     * 构建责任链（不修改单例Bean）
     */
    private ChainedLogic buildChainedLogic(Long strategyId) {
        StrategyEntity strategy = repository.queryStrategyEntityByStrategyId(strategyId);
        String[] ruleModels = strategy.ruleModels();
        if (ruleModels == null || ruleModels.length == 0) {
            return new ChainedLogic(logicChainGroup.get(Constants.RuleModel.DEFAULT), null);
        }
        // 转化ruleModels为rule_blacklist、rule_weight特定的形式
        String[] newRuleModels = new String[ruleModels.length];
        for (int i = 0; i < ruleModels.length; i++) {
            if (ruleModels[i].contains(Constants.RuleModel.RULE_BLACKLIST)){
                newRuleModels[i]=Constants.RuleModel.RULE_BLACKLIST;
            }else if (ruleModels[i].contains(Constants.RuleModel.RULE_WIGHT)){
                newRuleModels[i]=Constants.RuleModel.RULE_WIGHT;
            }
        }
        // 从后往前组装独立包装链
        ChainedLogic tail = new ChainedLogic(logicChainGroup.get(Constants.RuleModel.DEFAULT), null);
        for (int i = newRuleModels.length - 1; i >= 0; i--) {
            tail = new ChainedLogic(logicChainGroup.get(newRuleModels[i]), tail);
        }
        return tail;
    }

    /**
     * 责任链包装器（独立实例，不修改单例Bean字段）
     */
    public static class ChainedLogic {
        private final LogicChain current;
        private final ChainedLogic next;

        public ChainedLogic(LogicChain current, ChainedLogic next) {
            this.current = current;
            this.next = next;
        }

        /**
         * 递归迭代执行责任链
         * 当前节点返回非null结果表示接管，返回null表示放行到下一节点
         */
        public RuleEntity execute(String userId, Long strategyId, Long activityId) {
            RuleEntity result = current.logic(userId, strategyId, activityId);
            if (result != null) {
                return result;
            }
            if (next != null) {
                return next.execute(userId, strategyId, activityId);
            }
            return null;
        }
    }
}
