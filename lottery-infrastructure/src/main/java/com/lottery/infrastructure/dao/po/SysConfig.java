package com.lottery.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统动态配置表（轻量版：替代 Zookeeper DCC 配置中心）
 */
@TableName("sys_config")
@Data
public class SysConfig implements Serializable {

    /**
     * 配置键（主键，如 degradeSwitch、rateLimiterSwitch）
     */
    @TableId(type = IdType.INPUT)
    private String configKey;

    /**
     * 配置值
     */
    private String configValue;

    /**
     * 备注
     */
    private String remark;

    /**
     * 更新时间
     */
    private Date updateTime;

    @Serial
    private static final long serialVersionUID = 1L;
}
