package com.lottery.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 永
 * 交易类型值对象
 */
@Getter
@AllArgsConstructor
public enum OrderTradeTypeVO {
    credit_pay_trade("credit_pay_trade","积分兑换，需要支付"),
    rebate_no_pay_trade("rebate_no_pay_trade", "签到返利奖品，不需要支付"),
    ;

    private final String code;
    private final String desc;

}
