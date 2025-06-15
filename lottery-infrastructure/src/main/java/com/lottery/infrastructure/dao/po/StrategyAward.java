package com.lottery.infrastructure.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 策略_奖品详情表
 *
 * @author 永
 * @TableName strategy_award
 */
@TableName(value = "strategy_award")
@Data
public class StrategyAward implements Serializable {
    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 雪花ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 抽奖策略ID
     */
    private Long strategyId;
    /**
     * 抽奖奖品ID
     */
    private Long awardId;
    /**
     * 抽奖奖品标题
     */
    private String awardTitle;
    /**
     * 抽奖奖品副标题
     */
    private String awardSubtitle;
    /**
     * 奖品库存总量
     */
    private Integer awardCount;
    /**
     * 奖品库存剩余
     */
    private Integer awardCountSurplus;
    /**
     * 奖品中奖概率
     */
    private BigDecimal awardRate;
    /**
     * 规则模型（rule配置的模型同步到此表，便于使用）
     */
    private String ruleModels;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
}