package com.lottery.domain.activity.model.entity;

import com.lottery.domain.activity.model.valobj.OrderStateVO;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author 永
 * 活动参与实体对象
 */
@Data
@Builder
public class ActivityOrderEntity {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 抽奖策略ID
     */
    private Long strategyId;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 下单时间
     */
    private Date orderTime;

    /**
     * 总次数
     */
    private Integer totalCount;

    /**
     * 日次数
     */
    private Integer dayCount;

    /**
     * 月次数
     */
    private Integer monthCount;

    /**
     * 订单状态
     */
    private OrderStateVO state;

}
