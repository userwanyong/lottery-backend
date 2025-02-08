package com.lottery.domain.strategy.service.lottery;

import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.LotteryResEntity;
import com.lottery.domain.strategy.model.entity.RuleFilterResEntity;
import com.lottery.domain.strategy.model.entity.StrategyEntity;
import com.lottery.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.lottery.domain.strategy.repository.StrategyRepository;
import com.lottery.domain.strategy.service.LotteryStrategy;
import com.lottery.domain.strategy.service.rule.factory.DefaultLogicFactory;
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

    public AbstractLottery(StrategyRepository repository, StrategyService strategyDispatch) {
        this.repository = repository;
        this.strategyService = strategyDispatch;
    }

    @Override
    public LotteryResEntity performRaffle(LotteryReqEntity lotteryReqEntity) {
        // 1. 参数校验
        String userId = lotteryReqEntity.getUserId();
        Long strategyId = lotteryReqEntity.getStrategyId();
        if (strategyId == null || StringUtils.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
        }

        // 2. 查询策略
        StrategyEntity strategy = repository.queryStrategyEntityByStrategyId(strategyId);

        // 3. 抽奖前的规则过滤
        LotteryReqEntity lotteryReq = LotteryReqEntity.builder()
                .userId(userId)
                .strategyId(strategyId)
                .build();
        RuleFilterResEntity<RuleFilterResEntity.LotteryBeforeEntity> ruleFilterResEntity = this.doCheckRaffleBeforeLogic(lotteryReq, strategy.ruleModels());

        // 4. 根据过滤的返回值（放行or接管）如果被接管，判断规则类型做对应处理
        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode() == ruleFilterResEntity.getCode()) {
            if (DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode().equals(ruleFilterResEntity.getRuleModel())) {
                // 黑名单返回固定的奖品ID
                return LotteryResEntity.builder()
                        .awardId(ruleFilterResEntity.getData().getAwardId())
                        .build();
            } else if (DefaultLogicFactory.LogicModel.RULE_WIGHT.getCode().equals(ruleFilterResEntity.getRuleModel())) {
                // 权重根据返回的信息进行抽奖
                RuleFilterResEntity.LotteryBeforeEntity lotteryBeforeEntity = ruleFilterResEntity.getData();
                String ruleWeightValueKey = lotteryBeforeEntity.getRuleWeightValueKey();
                Long awardId = strategyService.getRandomAwardId(strategyId, ruleWeightValueKey);
                return LotteryResEntity.builder()
                        .awardId(awardId)
                        .build();
            }
        }

        // 5. 否则执行默认抽奖流程
        Long awardId = strategyService.getRandomAwardId(strategyId);
        // 6. 返回抽到的奖品 TODO 这里只填充了ID，后期改改吗？
        return LotteryResEntity.builder()
                .awardId(awardId)
                .build();
    }

    protected abstract RuleFilterResEntity<RuleFilterResEntity.LotteryBeforeEntity> doCheckRaffleBeforeLogic(LotteryReqEntity lotteryReqEntity, String... logics);

}

