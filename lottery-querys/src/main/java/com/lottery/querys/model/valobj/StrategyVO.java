package com.lottery.querys.model.valobj;

import lombok.Data;

import java.util.Date;


/**
 * @author 永
 */
@Data
public class StrategyVO {
    /**
     * 雪花ID
     */
    private Long id;

    /**
     * 抽奖策略描述
     */
    private String strategyDesc;
    /**
     * 规则模型
     */
    private String ruleModels;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
}