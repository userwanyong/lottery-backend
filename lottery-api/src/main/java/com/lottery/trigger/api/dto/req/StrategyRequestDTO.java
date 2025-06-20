package com.lottery.trigger.api.dto.req;

import lombok.Data;


/**
 * @author 永
 */
@Data
public class StrategyRequestDTO {
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

}