package com.lottery.trigger.api.dto.res;


import lombok.Data;

import java.util.Date;

/**
 * @author 永
 */
@Data
public class ActivityCountResponseDTO {
    private Long id;

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
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
