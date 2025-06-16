package com.lottery.domain.rebate.model.entity;

import com.lottery.domain.rebate.model.valobj.BehaviorTypeVO;
import lombok.Data;

/**
 * @author 永
 * 行为实体
 */
@Data
public class BehaviorEntity {
    /**
     * 用户ID
     */
    private String userId;
    /**
     * 活动ID
     */
    private Long activityId;
    /**
     * 行为类型；sign 签到、openai_pay 支付
     */
    private BehaviorTypeVO behaviorTypeVO;
    /**
     * 业务ID；签到则是日期字符串，支付则是外部的业务ID
     */
    private String outBusinessNo;
}
