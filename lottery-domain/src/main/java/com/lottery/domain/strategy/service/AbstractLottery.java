package com.lottery.domain.strategy.service;

import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.LotteryResEntity;
import com.lottery.domain.strategy.model.entity.RuleEntity;
import com.lottery.domain.strategy.model.entity.StrategyAwardEntity;
import com.lottery.domain.strategy.repository.StrategyRepository;
import com.lottery.domain.strategy.service.armory.StrategyService;
import com.lottery.domain.strategy.service.rule.chain.factory.DefaultLogicChainFactory;
import com.lottery.domain.strategy.service.rule.tree.factory.DefaultLogicTreeFactory;
import com.lottery.types.common.Constants;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 永
 * 策略-抽奖领域-抽奖标准流程
 */
@Slf4j
public abstract class AbstractLottery implements Lottery {

    protected StrategyRepository repository;
    protected StrategyService strategyService;
    protected DefaultLogicChainFactory defaultLogicChainFactory;
    protected DefaultLogicTreeFactory defaultLogicTreeFactory;

    public AbstractLottery(StrategyRepository repository, StrategyService strategyService, DefaultLogicChainFactory defaultLogicChainFactory, DefaultLogicTreeFactory defaultLogicTreeFactory) {
        this.repository = repository;
        this.strategyService = strategyService;
        this.defaultLogicChainFactory = defaultLogicChainFactory;
        this.defaultLogicTreeFactory = defaultLogicTreeFactory;
    }

    @Override
    public LotteryResEntity performLottery(LotteryReqEntity lotteryReqEntity) {
        // 1. 参数校验
        String userId = lotteryReqEntity.getUserId();
        Long strategyId = lotteryReqEntity.getStrategyId();
        if (strategyId == null) {
            log.error("[AbstractLottery]抽奖失败,strategyId为 null 用户ID：{}", userId);
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
        }
        // 2. 责任链
        RuleEntity chainAward = lotteryLogicChain(userId, strategyId);
        log.debug("[AbstractLottery]抽奖责任链通过 用户ID：{}, 策略ID：{}, 奖品ID：{}, 奖品规则模型：{}", userId, strategyId, chainAward.getAwardId(), chainAward.getRuleModel());
        // 只有默认规则才走规则树
        if (!Constants.RuleModel.DEFAULT.equals(chainAward.getRuleModel())) {
            return buildLotteryAwardEntity(strategyId, chainAward.getAwardId(), chainAward.getRuleValue());
        }
        // 3. 规则树
        RuleEntity treeAward = lotteryLogicTree(userId, strategyId, chainAward.getAwardId());
        log.debug("[AbstractLottery]默认规则执行规则树 用户ID：{}, 策略ID：{}, 奖品ID：{}, 奖品规则模型：{}", userId, strategyId, treeAward.getAwardId(), treeAward.getRuleValue());

        // 4. 返回结果
        return buildLotteryAwardEntity(strategyId, treeAward.getAwardId(), treeAward.getRuleValue());
    }

    private LotteryResEntity buildLotteryAwardEntity(Long strategyId, Long awardId, String awardConfig) {
        StrategyAwardEntity strategyAward = repository.queryStrategyAwardEntity(strategyId, awardId);
        return LotteryResEntity.builder()
                .awardId(awardId)
                .awardTitle(strategyAward.getAwardTitle())
                .awardConfig(awardConfig)
                .sort(strategyAward.getSort())
                .build();
    }

    public abstract RuleEntity lotteryLogicChain(String userId, Long strategyId);

    public abstract RuleEntity lotteryLogicTree(String userId, Long strategyId, Long awardId);


}

