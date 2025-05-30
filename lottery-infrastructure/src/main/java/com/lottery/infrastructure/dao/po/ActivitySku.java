package com.lottery.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 抽奖活动sku表
 * @author 永
 * @TableName activity_sku
 */
@TableName(value ="activity_sku")
@Data
public class ActivitySku implements Serializable {
    /**
     * 自增ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 商品sku - 把每一个组合当做一个商品
     */
    private Long sku;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 活动个人参与次数ID
     */
    private Long activityCountId;

    /**
     * 商品库存
     */
    private Integer stockCount;

    /**
     * 剩余库存
     */
    private Integer stockCountSurplus;

    /**
     * 兑换所需积分
     */
    private BigDecimal productAmount;

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