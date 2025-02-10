package com.lottery.domain.strategy.service;

import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.LotteryResEntity;
import com.lottery.domain.strategy.model.entity.RuleFilterResEntity;
import com.lottery.domain.strategy.model.entity.StrategyEntity;
import com.lottery.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.lottery.domain.strategy.model.valobj.StrategyRuleModelVO;
import com.lottery.domain.strategy.repository.StrategyRepository;
import com.lottery.domain.strategy.service.rule.chain.LogicChain;
import com.lottery.domain.strategy.service.rule.chain.factory.DefaultLogicChainFactory;
import com.lottery.domain.strategy.service.rule.filter.factory.DefaultLogicFilterFactory;
import com.lottery.domain.strategy.service.strategy.StrategyService;
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

    protected StrategyRepository repository;
    protected StrategyService strategyService;

    private final DefaultLogicChainFactory defaultLogicChainFactory;

    public AbstractLottery(StrategyRepository repository, StrategyService strategyService, DefaultLogicChainFactory defaultLogicChainFactory) {
        this.repository = repository;
        this.strategyService = strategyService;
        this.defaultLogicChainFactory = defaultLogicChainFactory;
    }

    @Override
    public LotteryResEntity performRaffle(LotteryReqEntity lotteryReqEntity) {
        // 1. 参数校验
        String userId = lotteryReqEntity.getUserId();
        Long strategyId = lotteryReqEntity.getStrategyId();
        if (strategyId == null || StringUtils.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
        }

        // 2. 获取责任链
        LogicChain logicChain = defaultLogicChainFactory.openChain(strategyId);

        // 3. 依次执行责任链，获取奖品id
        Long awardId = logicChain.chain(userId, strategyId);

        // 4. 查询奖品规则「抽奖中（拿到奖品ID时，过滤规则）、抽奖后（扣减完奖品库存后过滤，抽奖中拦截和无库存则走兜底）」
        StrategyRuleModelVO strategyRuleModelVO = repository.queryRuleModelVO(strategyId, awardId);

        // 5. 抽奖中的规则过滤
        LotteryReqEntity lotteryReqCenter = LotteryReqEntity.builder()
                .userId(userId)
                .strategyId(strategyId)
                .awardId(awardId)
                .build();
        RuleFilterResEntity<RuleFilterResEntity.LotteryCenterEntity> ruleFilterResEntityCenter = this.doCheckLotteryCenterLogic(lotteryReqCenter, strategyRuleModelVO.lotteryCenterRuleModelList());

        // 6. 根据过滤的返回值（放行or接管）如果被接管，进行处理
        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode() == ruleFilterResEntityCenter.getCode()){
            return LotteryResEntity.builder()
                    .awardId(ruleFilterResEntityCenter.getData().getAwardId())
                    .build();
        }

        // 7. 返回抽到的奖品 TODO 这里只填充了ID，后期改改吗？
        return LotteryResEntity.builder()
                .awardId(awardId)
                .build();
    }

    protected abstract RuleFilterResEntity<RuleFilterResEntity.LotteryCenterEntity> doCheckLotteryCenterLogic(LotteryReqEntity lotteryReqEntity, String... logics);

}

