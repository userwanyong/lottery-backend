package com.lottery.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 树节点表
 * @author 永
 * @TableName rule_tree_node
 */
@TableName(value ="rule_tree_node")
@Data
public class RuleTreeNode implements Serializable {
    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 规则树ID
     */
    private String treeId;

    /**
     * 规则名
     */
    private String ruleName;

    /**
     * 规则描述
     */
    private String ruleDesc;

    /**
     * 规则的值
     */
    private String ruleValue;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}