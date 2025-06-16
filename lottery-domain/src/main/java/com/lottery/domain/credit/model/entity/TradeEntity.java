package com.lottery.domain.credit.model.entity;

import com.lottery.domain.credit.model.valobj.TradeNameVO;
import com.lottery.domain.credit.model.valobj.TradeTypeVO;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 永
 * 交易实体对象
 */
@Data
public class TradeEntity {
    /** 用户ID */
    private String userId;
    /** 活动ID */
    private Long activityId;
    /** 交易名称 */
    private TradeNameVO tradeName;
    /** 交易类型；交易类型；forward-正向、reverse-逆向 */
    private TradeTypeVO tradeType;
    /** 交易金额 */
    private BigDecimal amount;
    /** 业务仿重ID - 外部透传。返利、行为等唯一标识 */
    private String outBusinessNo;
}
