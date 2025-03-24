package com.lottery.domain.award.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 永
 * 积分账户状态枚举值对象
 */
@Getter
@AllArgsConstructor
public enum AccountStatusVO {
    open("open", "开启"),
    close("close", "冻结"),
    ;
    private final String code;
    private final String desc;
}
