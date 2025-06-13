package com.lottery.domain.credit.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 永
 * 交易名称值对象
 */
@Getter
@AllArgsConstructor
public enum TradeNameVO {
    REBATE("每日签到"),
    CONVERT_SKU("积分兑换"),
    LOTTERY_AWARD("抽奖奖品");
    ;
    private final String name;
}
