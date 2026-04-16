package com.lottery.domain.strategy.service;

import com.lottery.domain.channel.service.ChannelService;
import com.lottery.domain.strategy.event.SendLotteryMessageEvent;
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
import com.lottery.types.event.BaseEvent;
import com.lottery.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 永
 * 策略-抽奖领域-抽奖标准流程
 */
@Slf4j
public abstract class AbstractLottery implements Lottery {

    protected StrategyRepository repository;
    protected StrategyService strategyService;
    protected ChannelService channelService;
    protected DefaultLogicChainFactory defaultLogicChainFactory;
    protected DefaultLogicTreeFactory defaultLogicTreeFactory;

    @Resource
    private SendLotteryMessageEvent sendLotteryMessageEvent;

    public AbstractLottery(
            StrategyRepository repository,
            StrategyService strategyService,
            ChannelService channelService,
            DefaultLogicChainFactory defaultLogicChainFactory,
            DefaultLogicTreeFactory defaultLogicTreeFactory
    ) {
        this.repository = repository;
        this.strategyService = strategyService;
        this.channelService = channelService;
        this.defaultLogicChainFactory = defaultLogicChainFactory;
        this.defaultLogicTreeFactory = defaultLogicTreeFactory;
    }

    @Override
    public LotteryResEntity doLottery(LotteryReqEntity lotteryReqEntity) {
        // 1. 参数校验
        String userId = lotteryReqEntity.getUserId();
        Long strategyId = lotteryReqEntity.getStrategyId();
        Long activityId = lotteryReqEntity.getActivityId();
        if (strategyId == null || activityId == null) {
            log.error("[AbstractLottery]抽奖失败,strategyId/activityId为 null 用户ID：{}", userId);
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
        }
        // 2. 责任链
        RuleEntity chainAward = logicChain(userId, strategyId, activityId);
        log.debug("[AbstractLottery]抽奖责任链通过 用户ID：{}, 策略ID：{}, 奖品ID：{}, 奖品规则模型：{}", userId, strategyId, chainAward.getAwardId(), chainAward.getRuleModel());
        if (!Constants.RuleModel.DEFAULT.equals(chainAward.getRuleModel())) {
            return buildLotteryAwardEntity(activityId, chainAward.getAwardId());
        }
        // 3. 规则树
        RuleEntity treeAward = logicTree(userId, strategyId, activityId, chainAward.getAwardId());
        log.debug("[AbstractLottery]默认规则执行规则树 用户ID：{}, 策略ID：{}, 奖品ID：{}, 奖品规则模型：{}", userId, strategyId, treeAward.getAwardId(), treeAward.getRuleValue());
        LotteryResEntity resEntity = buildLotteryAwardEntity(activityId, treeAward.getAwardId());

//        SendLotteryMessageEvent.LotteryMessage message = SendLotteryMessageEvent.LotteryMessage.builder()
//                .userId(userId)
//                .awardConfig(resEntity.getAwardConfig())
//                .awardTitle(resEntity.getAwardTitle())
//                .awardId(resEntity.getAwardId())
//                .activityId(String.valueOf(activityId))
//                .sort(resEntity.getSort())
//                .awardTime(resEntity.getAwardTime())
//                .build();
//        BaseEvent.EventMessage<SendLotteryMessageEvent.LotteryMessage> lotteryMessageEventMessage =
//                sendLotteryMessageEvent.buildEventMessage(message);
//        repository.sendLotteryMessageToMq(sendLotteryMessageEvent.topic(), lotteryMessageEventMessage);

        //6. 返回
        return resEntity;

    }

    private LotteryResEntity buildLotteryAwardEntity(Long activityId, Long awardId) {
        StrategyAwardEntity strategyAward = repository.queryActivityAwardEntity(activityId, awardId);
        return LotteryResEntity.builder()
                .awardId(awardId)
                .awardTitle(strategyAward.getAwardTitle())
                .awardConfig(strategyAward.getAwardConfig())
                .sort(strategyAward.getSort())
                .awardTime(new Date())
                .build();
    }

    public abstract RuleEntity logicChain(String userId, Long strategyId, Long activityId);

    public abstract RuleEntity logicTree(String userId, Long strategyId, Long activityId, Long awardId);


}

