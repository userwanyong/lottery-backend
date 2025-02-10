package com.lottery.domain.strategy.model.valobj;

import com.lottery.domain.strategy.service.rule.filter.factory.DefaultLogicFilterFactory;
import com.lottery.types.common.Constants;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @author 永
 * 抽奖规则
 */
@Getter
@Builder
public class StrategyRuleModelVO {

    private String ruleModels;

    /**
     * 获取抽奖中规则；或者使用 lambda 表达式
     */
    public String[] lotteryCenterRuleModelList() {
        List<String> ruleModelList = Arrays.stream(ruleModels.split(Constants.SPLIT))
                .filter(DefaultLogicFilterFactory.LogicModel::isCenter)
                .toList();
        return ruleModelList.toArray(new String[0]);
    }
}
