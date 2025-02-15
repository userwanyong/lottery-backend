package com.lottery.domain.strategy.model.valobj;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author 永
 * 规则树-节点
 */
@Data
@Builder
public class RuleTreeNodeVO {
    /** 规则树ID */
    private Integer treeId;
    /** 规则名 */
    private String ruleName;
    /** 规则描述 */
    private String ruleDesc;
    /** 规则的值 */
    private String ruleValue;
    /** 规则连线 */
    private List<RuleTreeNodeLineVO> treeNodeLineVOList;
}
