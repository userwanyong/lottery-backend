package com.lottery.domain.strategy.service.rule.factory;

import com.alibaba.fastjson2.util.AnnotationUtils;
import com.lottery.domain.strategy.model.entity.RuleFilterResEntity;
import com.lottery.domain.strategy.service.annotation.LogicStrategy;
import com.lottery.domain.strategy.service.rule.LogicFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 永
 * 规则工厂
 */
@Service
public class DefaultLogicFactory {
    public Map<String, LogicFilter<?>> logicFilterMap = new ConcurrentHashMap<>();

    /**
     * 通过注解找到每个过滤器对应的策略，并将其存储到Map中
     */
    public DefaultLogicFactory(List<LogicFilter<?>> logicFilters) {
        logicFilters.forEach(logic -> {
            LogicStrategy strategy = AnnotationUtils.findAnnotation(logic.getClass(), LogicStrategy.class);
            if (strategy != null) {
                logicFilterMap.put(strategy.logicMode().getCode(), logic);
            }
        });
    }

    public <T extends RuleFilterResEntity.LotteryEntity> Map<String, LogicFilter<T>> openLogicFilter() {
        return (Map<String, LogicFilter<T>>) (Map<?, ?>) logicFilterMap;
    }

    @Getter
    @AllArgsConstructor
    public enum LogicModel {
        RULE_WIGHT("rule_weight", "抽奖前规则-根据抽奖权重返回可抽奖范围KEY","before"),
        RULE_BLACKLIST("rule_blacklist", "抽奖前规则-黑名单规则过滤，在黑名单直接返回","before"),
        RULE_LOCK("rule_lock", "抽奖中规则-是否解锁该奖品","center"),
        RULE_LUCK_AWARD("rule_luck_award", "抽奖后规则-", "after"),
        ;

        private final String code;
        private final String message;
        private final String type;

        public static boolean isCenter(String code){
            return "center".equals(LogicModel.valueOf(code.toUpperCase()).type);
        }
    }
}
