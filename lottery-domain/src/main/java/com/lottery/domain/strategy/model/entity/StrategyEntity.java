package com.lottery.domain.strategy.model.entity;

import com.lottery.types.common.Constants;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 永
 * 策略实体
 */
@Data
public class StrategyEntity {
    /**
     * 抽奖策略ID
     */
    private Long id;

    /**
     * 抽奖策略描述
     */
    private String strategyDesc;

    /**
     * 规则模型
     */
    private String ruleModels;

    /**
     * 获取每一个规则模型
     *
     * @return 数组
     */
    public String[] ruleModels() {
        if (StringUtils.isBlank(ruleModels)) {
            return null;
        }
        return ruleModels.split(Constants.SPLIT);
    }

    /**
     * 获取权重模型
     *
     * @return String
     */
    public String getRuleWeight() {
        String[] ruleModels = this.ruleModels();
        for (String ruleModel : ruleModels) {
            if (Constants.RuleModel.RULE_WIGHT.equals(ruleModel)) {
                return ruleModel;
            }
        }
        return null;
    }
}
