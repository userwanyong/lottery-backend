package com.lottery.domain.activity.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 永
 * 用户订单状态值对象
 */
@Getter
@AllArgsConstructor
public enum UserOrderStateVO {
    create("create", "创建"),
    used("used", "已使用"),
    cancel("cancel", "已作废"),
            ;
    private final String code;
    private final String desc;
}
