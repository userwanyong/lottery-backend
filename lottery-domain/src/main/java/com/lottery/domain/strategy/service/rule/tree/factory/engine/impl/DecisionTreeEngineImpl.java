package com.lottery.domain.strategy.service.rule.tree.factory.engine.impl;

import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.model.entity.StrategyAwardEntity;
import com.lottery.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.lottery.domain.strategy.model.valobj.RuleTreeNodeLineVO;
import com.lottery.domain.strategy.model.valobj.RuleTreeNodeVO;
import com.lottery.domain.strategy.model.valobj.RuleTreeVO;
import com.lottery.domain.strategy.service.rule.tree.LogicTree;
import com.lottery.domain.strategy.service.rule.tree.factory.DefaultLogicTreeFactory;
import com.lottery.domain.strategy.service.rule.tree.factory.engine.DecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @author 永
 * 组合规则树（引擎）
 */
@Slf4j
public class DecisionTreeEngineImpl implements DecisionTreeEngine {
    private final Map<String, LogicTree> logicTreeNodeGroup;
    private final RuleTreeVO ruleTreeVO;
    public DecisionTreeEngineImpl(Map<String, LogicTree> logicTreeNodeGroup, RuleTreeVO ruleTreeVO) {
        this.logicTreeNodeGroup = logicTreeNodeGroup;
        this.ruleTreeVO = ruleTreeVO;
    }
    @Override
    public RuleEntity process(String userId, Long strategyId, Long awardId) {
        RuleEntity ruleEntity = null;
        // 1. 获取基础信息（根节点、所有规则树节点的Map集合）
        String nextNode = ruleTreeVO.getTreeRootRuleNode();
        Map<String, RuleTreeNodeVO> treeNodeMap = ruleTreeVO.getTreeNodeMap();
        // 2. 获取起始节点（根节点记录第一个要执行的规则）
        RuleTreeNodeVO ruleTreeNode = treeNodeMap.get(nextNode);
        // 3. 按条件遍历规则树节点
        while (ruleTreeNode!=null) {
            // 3.1. 获取规则树的节点
            LogicTree logicTreeNode = logicTreeNodeGroup.get(ruleTreeNode.getRuleName());
            // 3.2. 节点计算，判断走向
            DefaultLogicTreeFactory.TreeActionEntity logicEntity = logicTreeNode.logic(userId, strategyId, awardId);
            RuleLogicCheckTypeVO ruleLogicCheckTypeVO = logicEntity.getRuleLogicCheckType();
            ruleEntity = logicEntity.getRuleEntity();
            log.info("决策树引擎【{}】treeId:{} node:{} code:{}", ruleTreeVO.getTreeName(), ruleTreeVO.getTreeId(), nextNode, ruleLogicCheckTypeVO.getCode());
            // 3.3. 获取下个节点
            nextNode = getNextNode(ruleLogicCheckTypeVO.getCode(), ruleTreeNode.getTreeNodeLineVOList());
            ruleTreeNode = treeNodeMap.get(nextNode);
        }
        // 4. 返回最终结果
        return ruleEntity;
    }
    public String getNextNode(Integer matterValue, List<RuleTreeNodeLineVO> treeNodeLineVOList) {
        if (treeNodeLineVOList==null || treeNodeLineVOList.isEmpty()) {
            return null;
        }
        for (RuleTreeNodeLineVO nodeLine : treeNodeLineVOList) {
            if (decisionLogic(matterValue, nodeLine)) {
                return nodeLine.getRuleNodeTo();
            }
        }
        throw new RuntimeException("决策树引擎，nextNode 计算失败，未找到可执行节点！");
    }
    public boolean decisionLogic(Integer matterValue, RuleTreeNodeLineVO nodeLine) {
        switch (nodeLine.getRuleLimitType()) {
            case EQUAL:
                return matterValue.equals(nodeLine.getRuleLimitValue().getCode());
            // 以下规则暂时不需要实现
            case GT:
            case LT:
            case GTE:
            case LTE:
            default:
                return false;
        }
    }
}
