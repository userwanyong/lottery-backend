package com.lottery.domain.auth.model.vo;

import lombok.Data;

import java.util.List;

/**
 * 通用分页结果
 */
@Data
public class AuthPageVO<T> {
    private Long total;
    private Integer page;
    private Integer size;
    private List<T> items;
}
