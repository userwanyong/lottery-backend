package com.lottery.trigger.http;

import com.lottery.domain.activity.service.ActivityQuotaService;
import com.lottery.domain.strategy.model.entity.StrategyAwardEntity;
import com.lottery.domain.strategy.model.valobj.RuleWeightVO;
import com.lottery.domain.strategy.service.Lottery;
import com.lottery.domain.strategy.service.Rule;
import com.lottery.domain.strategy.service.armory.StrategyArmory;
import com.lottery.trigger.api.LotteryService;
import com.lottery.trigger.api.dto.req.LotteryAwardListRequestDTO;
import com.lottery.trigger.api.dto.req.StrategyRuleWeightRequestDTO;
import com.lottery.trigger.api.dto.res.LotteryAwardListResponseDTO;
import com.lottery.trigger.api.dto.res.StrategyRuleWeightResponseDTO;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import com.lottery.types.model.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 永
 */
@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/lottery")
public class LotteryController implements LotteryService {

    @Resource
    private StrategyArmory strategyArmory;
    @Resource
    private Lottery lottery;

    @Resource
    private Rule rule;

    @Resource
    private ActivityQuotaService activityQuotaService;

//    @Override
//    @GetMapping("/strategy_armory")
//    public BaseResponse<Boolean> strategyArmory(@RequestParam Long strategyId) {
//        try {
//            boolean result = strategyArmory.assembleLotteryStrategy(strategyId);
//            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), result);
//        } catch (Exception e) {
//            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
//        }
//    }

    @Override
    @PostMapping("/query_lottery_award_list")
    public BaseResponse<List<LotteryAwardListResponseDTO>> queryLotteryAwardList(@RequestBody LotteryAwardListRequestDTO requestDTO) {
        log.info("======================[queryLotteryAwardList]查询奖品列表开始 activityId：{} userId：{} ======================", requestDTO.getActivityId(), requestDTO.getUserId());
        // 1. 参数校验
        if (requestDTO.getActivityId() == null) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
        }
        try {
            // 2. 查询奖品配置
            List<StrategyAwardEntity> strategyAwardEntities = lottery.queryLotteryAwardListByActivityId(requestDTO.getActivityId());
            // 3. 获取规则配置
            String[] treeIds = strategyAwardEntities.stream()
                    .map(StrategyAwardEntity::getRuleModel)
                    .filter(ruleModel -> ruleModel != null && !ruleModel.isEmpty())
                    .toArray(String[]::new);
            // 4. 查询规则配置 - 获取奖品的解锁限制，抽奖N次后解锁
            Map<String, Integer> ruleLockCountMap = rule.queryAwardRuleLockCount(treeIds);
            // 5. 用户今天已经参与的抽奖次数
            Integer count = activityQuotaService.queryTodayUserLotteryCount(requestDTO.getUserId(), requestDTO.getActivityId());
            List<LotteryAwardListResponseDTO> lotteryAwardListResponseDTOList = new ArrayList<>(strategyAwardEntities.size());
            for (StrategyAwardEntity strategyAwardEntity : strategyAwardEntities) {
                //规则的次数
                Integer awardRuleLockCount = ruleLockCountMap.get(strategyAwardEntity.getRuleModel());
                LotteryAwardListResponseDTO lotteryAwardListResponseDTO = new LotteryAwardListResponseDTO();
                BeanUtils.copyProperties(strategyAwardEntity, lotteryAwardListResponseDTO);
                lotteryAwardListResponseDTO.setAwardRuleLockCount(awardRuleLockCount == null ? 0 : awardRuleLockCount);
                lotteryAwardListResponseDTO.setIsAwardUnlock(awardRuleLockCount == null || count >= awardRuleLockCount);
                lotteryAwardListResponseDTO.setWaitUnLockCount(awardRuleLockCount == null || count >= awardRuleLockCount ? 0 : (awardRuleLockCount - count));
                lotteryAwardListResponseDTOList.add(lotteryAwardListResponseDTO);
            }
            log.info("======================[queryLotteryAwardList]查询奖品列表成功 activityId：{} userId：{} ======================", requestDTO.getActivityId(), requestDTO.getUserId());
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), lotteryAwardListResponseDTOList);
        } catch (Exception e) {
            log.info("======================[queryLotteryAwardList]查询奖品列表失败 activityId：{} userId：{} ======================", requestDTO.getActivityId(), requestDTO.getUserId());
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }

//    @Override
//    @PostMapping("/random_lottery")
//    public BaseResponse<LotteryResponseDTO> randomLottery(@RequestBody LotteryRequestDTO requestDTO) {
//        try {
//            LotteryResEntity lotteryResEntity = lottery.performLottery(LotteryReqEntity.builder().strategyId(requestDTO.getStrategyId()).build());
//            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), LotteryResponseDTO.builder()
//                    .awardId(lotteryResEntity.getAwardId())
//                    .awardIndex(lotteryResEntity.getSort())
//                    .build());
//        } catch (AppException e) {
//            return new BaseResponse<>(e.getCode(), e.getMessage());
//        } catch (Exception e) {
//            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
//        }
//    }

    @Override
    @PostMapping("/query_strategy_rule_weight")
    public BaseResponse<List<StrategyRuleWeightResponseDTO>> queryStrategyRuleWeight(@RequestBody StrategyRuleWeightRequestDTO requestDTO) {
        try {
            log.info("======================[queryStrategyRuleWeight]查询用户抽奖权重开始 userId:{} ======================", requestDTO.getUserId());
            // 1.参数校验
            if (requestDTO.getUserId() == null|| "null".equals(requestDTO.getUserId())) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
            }
            // 2.用户已经参与的抽奖次数
            Integer count = activityQuotaService.queryTotalUserLotteryCount(requestDTO.getUserId(), requestDTO.getActivityId());
            // 3.查询配置
            List<RuleWeightVO> ruleWeightVOList = rule.queryStrategyRuleWeight(requestDTO.getUserId(), requestDTO.getActivityId());
            // 4.封装
            List<StrategyRuleWeightResponseDTO> strategyRuleWeightResponseDTOList = new ArrayList<>(ruleWeightVOList.size());
            for (RuleWeightVO ruleWeightVO : ruleWeightVOList) {
                List<StrategyRuleWeightResponseDTO.StrategyAward> strategyAwardList = new ArrayList<>();
                for (RuleWeightVO.Award award : ruleWeightVO.getAwardList()) {
                    StrategyRuleWeightResponseDTO.StrategyAward strategyAward = new StrategyRuleWeightResponseDTO.StrategyAward();
                    strategyAward.setAwardId(award.getAwardId());
                    strategyAward.setAwardTitle(award.getAwardTitle());
                    strategyAwardList.add(strategyAward);
                }
                StrategyRuleWeightResponseDTO strategyRuleWeightResponseDTO = new StrategyRuleWeightResponseDTO();
                strategyRuleWeightResponseDTO.setRuleWeightCount(ruleWeightVO.getWeight());
                strategyRuleWeightResponseDTO.setUserActivityAccountTotalUseCount(count);
                strategyRuleWeightResponseDTO.setStrategyAwards(strategyAwardList);
                strategyRuleWeightResponseDTOList.add(strategyRuleWeightResponseDTO);
            }
            log.info("======================[queryStrategyRuleWeight]查询用户抽奖权重成功 userId:{} ======================", requestDTO.getUserId());
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), strategyRuleWeightResponseDTOList);
        } catch (AppException e) {
            log.error("======================[queryStrategyRuleWeight]查询用户抽奖权重异常 userId:{} ======================", requestDTO.getUserId(), e);
            return new BaseResponse<>(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("======================[queryStrategyRuleWeight]查询用户抽奖权重异常 userId:{} ======================", requestDTO.getUserId(), e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }
}
