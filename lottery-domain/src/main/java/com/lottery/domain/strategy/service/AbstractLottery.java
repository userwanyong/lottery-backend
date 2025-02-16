package com.lottery.domain.strategy.service;

import com.lottery.domain.strategy.model.entity.*;
import com.lottery.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.lottery.domain.strategy.model.valobj.StrategyRuleModelVO;
import com.lottery.domain.strategy.repository.LotteryRepository;
import com.lottery.domain.strategy.service.rule.chain.factory.DefaultLogicChainFactory;
import com.lottery.domain.strategy.service.rule.tree.factory.DefaultLogicTreeFactory;
import com.lottery.domain.strategy.service.strategy.StrategyService;
import com.lottery.types.common.Constants;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 永
 * 抽奖标准流程
 */
@Slf4j
public abstract class AbstractLottery implements LotteryStrategy {

    protected LotteryRepository repository;
    protected StrategyService strategyService;

    protected DefaultLogicChainFactory defaultLogicChainFactory;
    protected DefaultLogicTreeFactory defaultLogicTreeFactory;

    public AbstractLottery(LotteryRepository repository, StrategyService strategyService, DefaultLogicChainFactory defaultLogicChainFactory, DefaultLogicTreeFactory defaultLogicTreeFactory) {
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
        if (strategyId == null || StringUtils.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
        }

        // 2. 责任链
        RuleEntity chainAward=lotteryLogicChain(userId,strategyId);
        log.info("抽奖策略计算-责任链 {} {} {} {}", userId, strategyId, chainAward.getAwardId(), chainAward.getRuleModel());
        // 只有默认规则才走规则树
        if (!Constants.RuleModel.DEFAULT.equals(chainAward.getRuleModel())) {
            return LotteryResEntity.builder()
                    .awardId(chainAward.getAwardId())
                    .build();
        }
        // 3. 规则树
        RuleEntity treeAward=lotteryLogicTree(userId,strategyId,chainAward.getAwardId());
        log.info("抽奖策略计算-规则树 {} {} {} {}", userId, strategyId, treeAward.getAwardId(), treeAward.getRuleValue());

        // 4. 返回结果
        return LotteryResEntity.builder()
                .awardId(treeAward.getAwardId())
                .awardConfig(treeAward.getRuleValue())
                .build();


    }

    protected abstract RuleFilterResEntity<RuleFilterResEntity.LotteryCenterEntity> doCheckLotteryCenterLogic(LotteryReqEntity lotteryReqEntity, String... logics);
    public abstract RuleEntity lotteryLogicChain(String userId, Long strategyId);
    public abstract RuleEntity lotteryLogicTree(String userId, Long strategyId,Long awardId);


}

