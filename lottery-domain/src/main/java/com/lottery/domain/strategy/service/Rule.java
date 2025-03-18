package com.lottery.domain.strategy.service;

import java.util.Map;

/**
 * @author 永
 * 策略-抽奖领域-规则相关操作
 */
public interface Rule {
    Map<String, Integer> queryAwardRuleLockCount(String[] treeIds);
}
