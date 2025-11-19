package com.lottery.domain.strategy.service.rule.tree.factory;

import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.model.entity.StrategyAwardEntity;
import com.lottery.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.lottery.domain.strategy.model.valobj.RuleTreeVO;
import com.lottery.domain.strategy.service.rule.tree.LogicTree;
import com.lottery.domain.strategy.service.rule.tree.factory.engine.DecisionTreeEngine;
import com.lottery.domain.strategy.service.rule.tree.factory.engine.impl.DecisionTreeEngineImpl;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 永
 * 规则树工厂
 */
@Service
public class DefaultLogicTreeFactory {
    private final Map<String, LogicTree> logicTreeNodeGroup;
    public DefaultLogicTreeFactory(Map<String, LogicTree> logicTreeNodeGroup) {
        this.logicTreeNodeGroup = logicTreeNodeGroup;
    }
    public DecisionTreeEngine getLogicTree(RuleTreeVO ruleTreeVO) {
        return new DecisionTreeEngineImpl(logicTreeNodeGroup, ruleTreeVO);
    }

    /**
     * 决策树动作实体
     */
    @Data
    @Builder
    public static class TreeActionEntity {
        private RuleLogicCheckTypeVO ruleLogicCheckType;
        private RuleEntity ruleEntity;
    }
}
