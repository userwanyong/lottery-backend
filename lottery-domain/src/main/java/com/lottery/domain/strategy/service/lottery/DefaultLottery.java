package com.lottery.domain.strategy.service.lottery;

import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.RuleFilterReqEntity;
import com.lottery.domain.strategy.model.entity.RuleFilterResEntity;
import com.lottery.domain.strategy.model.valobj.RuleLogicCheckTypeVO;
import com.lottery.domain.strategy.repository.StrategyRepository;
import com.lottery.domain.strategy.service.rule.LogicFilter;
import com.lottery.domain.strategy.service.rule.factory.DefaultLogicFactory;
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
    private DefaultLogicFactory logicFactory;

    public DefaultLottery(StrategyRepository repository, StrategyService strategyDispatch) {
        super(repository, strategyDispatch);
    }

    @Override
    protected RuleFilterResEntity<RuleFilterResEntity.LotteryBeforeEntity> doCheckRaffleBeforeLogic(LotteryReqEntity lotteryReqEntity, String... logics) {
        // 1. 获取规则过滤器组
        Map<String, LogicFilter<RuleFilterResEntity.LotteryBeforeEntity>> logicFilterGroup = logicFactory.openLogicFilter();

        // 2. 黑名单规则优先过滤
        String ruleBackList = Arrays.stream(logics)
                .filter(str -> str.contains(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode()))
                .findFirst()
                .orElse(null);

        // 3. 若存在黑名单规则，判断用户是否符合黑名单，若符合，直接返回
        if (StringUtils.isNotBlank(ruleBackList)) {// StringUtils.isNotBlank()->如果字符串为 null、空字符串 ("") 或者只包含空白字符（如空格、制表符等），则返回 false

            // 获取黑名单的过滤器
            LogicFilter<RuleFilterResEntity.LotteryBeforeEntity> logicFilter = logicFilterGroup.get(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode());

            RuleFilterReqEntity ruleFilterReqEntity = new RuleFilterReqEntity();
            ruleFilterReqEntity.setUserId(lotteryReqEntity.getUserId());
            ruleFilterReqEntity.setAwardId(ruleFilterReqEntity.getAwardId());//todo Why?
            ruleFilterReqEntity.setStrategyId(lotteryReqEntity.getStrategyId());
            ruleFilterReqEntity.setRuleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode());

            RuleFilterResEntity<RuleFilterResEntity.LotteryBeforeEntity> ruleFilterResEntity = logicFilter.filter(ruleFilterReqEntity);

            if (RuleLogicCheckTypeVO.ALLOW.getCode() != ruleFilterResEntity.getCode()) {
                return ruleFilterResEntity;
            }
        }

        // 4. 否则顺序过滤剩余规则，如权重规则
        List<String> ruleList = Arrays.stream(logics)
                .filter(s -> !s.equals(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode()))
                .toList();

        RuleFilterResEntity<RuleFilterResEntity.LotteryBeforeEntity> ruleFilterResEntity = null;
        for (String ruleModel : ruleList) {
            LogicFilter<RuleFilterResEntity.LotteryBeforeEntity> logicFilter = logicFilterGroup.get(ruleModel);

            RuleFilterReqEntity ruleFilterReqEntity = new RuleFilterReqEntity();
            ruleFilterReqEntity.setUserId(lotteryReqEntity.getUserId());
            ruleFilterReqEntity.setAwardId(ruleFilterReqEntity.getAwardId());
            ruleFilterReqEntity.setStrategyId(lotteryReqEntity.getStrategyId());
            ruleFilterReqEntity.setRuleModel(ruleModel);

            ruleFilterResEntity = logicFilter.filter(ruleFilterReqEntity);

            // 非放行结果则顺序过滤
            log.info("抽奖前规则过滤 userId: {} ruleModel: {} code: {} info: {}", lotteryReqEntity.getUserId(), ruleModel, ruleFilterResEntity.getCode(), ruleFilterResEntity.getMessage());
            if (RuleLogicCheckTypeVO.ALLOW.getCode() != ruleFilterResEntity.getCode()) {
                return ruleFilterResEntity;
            }
        }

        return ruleFilterResEntity;
    }

}

