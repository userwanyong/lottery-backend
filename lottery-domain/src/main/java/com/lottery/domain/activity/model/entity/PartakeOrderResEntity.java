package com.lottery.domain.activity.model.entity;

import com.lottery.domain.activity.model.valobj.UserOrderStateVO;
import lombok.Data;

import java.util.Date;

/**
 * @author 永
 * 创建抽奖单响应体
 */
@Data
public class PartakeOrderResEntity {
    private Long id;

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 活动ID
     */
    private Long activityId;

//    /**
//     * 订单ID
//     */
//    private Long orderId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 抽奖策略ID
     */
    private Long strategyId;

    /**
     * 下单时间
     */
    private Date orderTime;

    /**
     * 订单状态；create-创建、used-已使用、cancel-已作废
     */
    private UserOrderStateVO orderState;
}
