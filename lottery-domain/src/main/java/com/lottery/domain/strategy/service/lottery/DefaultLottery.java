package com.lottery.domain.strategy.service.lottery;

import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.RuleFilterReqEntity;
import com.lottery.domain.strategy.model.entity.RuleFilterResEntity;
import com.lottery.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.lottery.domain.strategy.repository.StrategyRepository;
import com.lottery.domain.strategy.service.AbstractLottery;
import com.lottery.domain.strategy.service.rule.chain.factory.DefaultLogicChainFactory;
import com.lottery.domain.strategy.service.rule.filter.LogicFilter;
import com.lottery.domain.strategy.service.rule.filter.factory.DefaultLogicFilterFactory;
import com.lottery.domain.strategy.service.strategy.StrategyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author 永
 * 抽奖流程的默认实现
 */
@Slf4j
@Service
public class DefaultLottery extends AbstractLottery {

    @Resource
    private DefaultLogicFilterFactory logicFactory;

    public DefaultLottery(StrategyRepository repository, StrategyService strategyService, DefaultLogicChainFactory defaultLogicChainFactory, DefaultLogicFilterFactory logicFactory) {
        super(repository, strategyService, defaultLogicChainFactory);
    }

    @Override
    protected RuleFilterResEntity<RuleFilterResEntity.LotteryCenterEntity> doCheckLotteryCenterLogic(LotteryReqEntity lotteryReqEntity, String... logics) {
        if (logics == null || 0 == logics.length) {
            return RuleFilterResEntity.<RuleFilterResEntity.LotteryCenterEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .message(RuleLogicCheckTypeVO.ALLOW.getMessage())
                    .build();
        }
        // 1. 获取规则过滤器组
        Map<String, LogicFilter<RuleFilterResEntity.LotteryCenterEntity>> logicFilterGroup = logicFactory.openLogicFilter();
        // 2. 过滤解锁规则
        RuleFilterResEntity<RuleFilterResEntity.LotteryCenterEntity> ruleFilterResEntity = null;
        for (String ruleModel : logics) {
            // 得到相应规则过滤器
            LogicFilter<RuleFilterResEntity.LotteryCenterEntity> logicFilter = logicFilterGroup.get(ruleModel);

            RuleFilterReqEntity ruleFilterReqEntity = new RuleFilterReqEntity();
            ruleFilterReqEntity.setUserId(lotteryReqEntity.getUserId());
            ruleFilterReqEntity.setAwardId(lotteryReqEntity.getAwardId());
            ruleFilterReqEntity.setStrategyId(lotteryReqEntity.getStrategyId());
            ruleFilterReqEntity.setRuleModel(ruleModel);

            ruleFilterResEntity = logicFilter.filter(ruleFilterReqEntity);

            // 是放行结果则继续过滤
            log.info("抽奖中规则过滤 userId: {} ruleModel: {} code: {} info: {}", lotteryReqEntity.getUserId(), ruleModel, ruleFilterResEntity.getCode(), ruleFilterResEntity.getMessage());
            if (RuleLogicCheckTypeVO.ALLOW.getCode() != ruleFilterResEntity.getCode()) {
                return ruleFilterResEntity;
            }
        }

        return ruleFilterResEntity;
    }

}

