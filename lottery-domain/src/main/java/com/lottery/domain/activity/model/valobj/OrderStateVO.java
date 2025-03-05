package com.lottery.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 永
 * 订单状态值对象
 */
@Getter
@AllArgsConstructor
public enum OrderStateVO {
    completed("completed", "完成");

    private final String code;
    private final String desc;

}
