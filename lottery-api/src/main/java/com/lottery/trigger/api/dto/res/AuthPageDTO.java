package com.lottery.trigger.api.dto.res;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class AuthPageDTO<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long total;
    private Integer page;
    private Integer size;
    private List<T> items;
}
