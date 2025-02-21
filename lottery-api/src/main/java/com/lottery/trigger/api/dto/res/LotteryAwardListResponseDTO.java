package com.lottery.trigger.api.dto.res;

import lombok.Data;

/**
 * @author 永
 * 查询抽奖奖品列表，出参
 */
@Data
public class LotteryAwardListResponseDTO {
    // 奖品ID
    private Long awardId;
    // 奖品标题
    private String awardTitle;
    // 奖品副标题【抽奖1次后解锁】
    private String awardSubtitle;
    // 排序编号
    private Integer sort;
}
