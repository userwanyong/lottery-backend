package com.lottery.trigger.api.dto.req;


import lombok.Data;

import java.util.Date;

/**
 * @author 永
 */
@Data
public class AwardRequestDTO {
    private Long id;
    /**
     * 奖品对接标识（每一个都是一个对应的发奖策略）
     */
    private String awardKey;
    /**
     * 奖品配置信息
     */
    private String awardConfig;
    /**
     * 奖品内容描述
     */
    private String awardDesc;
    /**
     * 奖品图片
     */
    private String image;
}
