package com.lottery.domain.strategy.model.entity;

import lombok.Builder;
import lombok.Data;

/**
 * @author 永
 * 抽奖响应实体
 */
@Data
@Builder
public class LotteryResEntity {

    /**
     * 抽奖奖品ID
     */
    private Long awardId;

    /**
     * 奖品名称
     */
    private String awardTitle;

    /**
     * 奖品配置信息
     */
    private String awardConfig;

    /**
     * 奖品顺序号
     */
    private Integer sort;

}
