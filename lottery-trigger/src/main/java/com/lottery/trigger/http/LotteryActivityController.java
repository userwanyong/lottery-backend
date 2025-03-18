package com.lottery.trigger.http;

import com.lottery.domain.activity.model.entity.PartakeOrderResEntity;
import com.lottery.domain.activity.service.ActivityPartakeService;
import com.lottery.domain.activity.service.armory.ActivityArmory;
import com.lottery.domain.award.model.entity.UserAwardRecordEntity;
import com.lottery.domain.award.model.valobj.AwardStateVO;
import com.lottery.domain.award.service.UserAwardService;
import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.LotteryResEntity;
import com.lottery.domain.strategy.service.Lottery;
import com.lottery.domain.strategy.service.armory.StrategyArmory;
import com.lottery.trigger.api.LotteryActivityService;
import com.lottery.trigger.api.dto.req.ActivityDrawRequestDTO;
import com.lottery.trigger.api.dto.res.ActivityDrawResponseDTO;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import com.lottery.types.model.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author 永
 */
@Slf4j
@RestController
@CrossOrigin("*")
@RequestMapping("/activity")
public class LotteryActivityController implements LotteryActivityService {
    @Resource
    private StrategyArmory strategyArmory;
    @Resource
    private Lottery lottery;
    @Resource
    private ActivityArmory activityArmory;
    @Resource
    private UserAwardService userAwardService;
    @Resource
    private ActivityPartakeService activityPartakeService;

    @Override
    @GetMapping("/armory")
    public BaseResponse<Boolean> armory(@RequestParam Long activityId) {
        try {
            log.info("======================[armory]整体装配开始 activityId:{} ======================", activityId);
            // 1. 活动装配
            activityArmory.assembleActivitySkuByActivityId(activityId);
            log.info("[armory]活动装配成功 activityId:{}", activityId);
            // 2. 策略装配
            strategyArmory.assembleLotteryStrategyByActivityId(activityId);
            log.info("[armory]策略装配成功 activityId:{}", activityId);
            log.info("======================[armory]整体装配成功 activityId:{} ======================", activityId);
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), true);
        } catch (Exception e) {
            log.error("======================[armory]整体装配失败 activityId:{} ======================", activityId, e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }

    @Override
    @PostMapping("/draw")
    public BaseResponse<ActivityDrawResponseDTO> draw(@RequestBody ActivityDrawRequestDTO request) {
        try {
            log.info("======================[draw]用户抽奖开始 userId:{} activityId:{} ======================", request.getUserId(), request.getActivityId());
            // 1. 参数校验
            if (StringUtils.isBlank(request.getUserId()) || request.getActivityId() == null) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
            }
            // 2. 参与活动 - 创建抽奖单
            PartakeOrderResEntity partakeOrder = activityPartakeService.createPartakeOrder(request.getUserId(), request.getActivityId());
            log.info("[draw]抽奖单 orderId:{}", partakeOrder.getOrderId());
            // 3. 抽奖策略 - 执行抽奖
            log.info("[draw]执行抽奖");
            LotteryResEntity lotteryResEntity = lottery.performLottery(LotteryReqEntity.builder().userId(partakeOrder.getUserId()).strategyId(partakeOrder.getStrategyId()).build());
            log.info("[draw]抽奖结果 {}",lotteryResEntity);
            // 4. 存放结果 - 写入中奖记录
            UserAwardRecordEntity userAwardRecord = UserAwardRecordEntity.builder()
                    .userId(partakeOrder.getUserId())
                    .activityId(partakeOrder.getActivityId())
                    .strategyId(partakeOrder.getStrategyId())
                    .orderId(partakeOrder.getOrderId())
                    .awardId(Math.toIntExact(lotteryResEntity.getAwardId()))
                    .awardTitle(lotteryResEntity.getAwardTitle())
                    .awardTime(new Date())
                    .awardState(AwardStateVO.create)
                    .build();
            userAwardService.saveUserAwardRecord(userAwardRecord);
            log.info("[draw]记录中奖记录成功");
            // 5. 返回结果
            ActivityDrawResponseDTO result = ActivityDrawResponseDTO.builder()
                    .awardId(Math.toIntExact(lotteryResEntity.getAwardId()))
                    .awardTitle(lotteryResEntity.getAwardTitle())
                    .awardIndex(lotteryResEntity.getSort())
                    .build();
            log.info("======================[draw]用户抽奖结束 userId:{} activityId:{} award:{} ======================", request.getUserId(), request.getActivityId(),result);
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), result);
        } catch (AppException e) {
            log.error("======================[draw]用户抽奖异常 userId:{} activityId:{} ======================", request.getUserId(), request.getActivityId(), e);
            return new BaseResponse<>(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error("======================[draw]用户抽奖异常 userId:{} activityId:{} ======================", request.getUserId(), request.getActivityId(), e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(),ResponseCode.UN_ERROR.getMessage());
        }
    }
}
