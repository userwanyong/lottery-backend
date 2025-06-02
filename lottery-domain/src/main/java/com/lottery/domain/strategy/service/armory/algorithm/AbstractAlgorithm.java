package com.lottery.domain.strategy.service.armory.algorithm;

import com.lottery.domain.strategy.repository.StrategyRepository;

import javax.annotation.Resource;
import java.security.SecureRandom;

/**
 * @author 永
 * 装配/抽奖算法抽象类
 */
public abstract class AbstractAlgorithm implements Algorithm {
    protected final SecureRandom secureRandom = new SecureRandom();
    @Resource
    protected StrategyRepository repository;

}
