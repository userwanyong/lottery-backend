package com.lottery.trigger.http;

import com.lottery.domain.activity.model.entity.ActivityAccountEntity;
import com.lottery.domain.activity.model.entity.PartakeOrderResEntity;
import com.lottery.domain.activity.service.ActivityPartakeService;
import com.lottery.domain.activity.service.ActivityQuotaService;
import com.lottery.domain.activity.service.armory.ActivityArmory;
import com.lottery.domain.award.model.entity.UserAwardRecordEntity;
import com.lottery.domain.award.model.valobj.AwardStateVO;
import com.lottery.domain.award.service.UserAwardService;
import com.lottery.domain.rebate.model.entity.BehaviorEntity;
import com.lottery.domain.rebate.model.entity.RebateOrderEntity;
import com.lottery.domain.rebate.model.valobj.BehaviorTypeVO;
import com.lottery.domain.rebate.service.RebateService;
import com.lottery.domain.strategy.model.entity.LotteryReqEntity;
import com.lottery.domain.strategy.model.entity.LotteryResEntity;
import com.lottery.domain.strategy.service.Lottery;
import com.lottery.domain.strategy.service.armory.StrategyArmory;
import com.lottery.trigger.api.LotteryActivityService;
import com.lottery.trigger.api.dto.req.ActivityDrawRequestDTO;
import com.lottery.trigger.api.dto.req.UserActivityAccountRequestDTO;
import com.lottery.trigger.api.dto.res.ActivityDrawResponseDTO;
import com.lottery.trigger.api.dto.res.UserActivityAccountResponseDTO;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import com.lottery.types.model.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    @Resource
    private RebateService rebateService;
    @Resource
    private ActivityQuotaService activityQuotaService;

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
            log.info("[draw]抽奖结果 {}", lotteryResEntity);
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
            log.info("======================[draw]用户抽奖结束 userId:{} activityId:{} award:{} ======================", request.getUserId(), request.getActivityId(), result);
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), result);
        } catch (AppException e) {
            log.error("======================[draw]用户抽奖异常 userId:{} activityId:{} ======================", request.getUserId(), request.getActivityId(), e);
            return new BaseResponse<>(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("======================[draw]用户抽奖异常 userId:{} activityId:{} ======================", request.getUserId(), request.getActivityId(), e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }

    @Override
    @PostMapping("/calendar_sign_rebate")
    public BaseResponse<Boolean> calendarSignRebate(@RequestParam String userId) {
        try {
            log.info("======================[calendarSignRebate]用户签到返现开始 userId:{} ======================", userId);
            BehaviorEntity behaviorEntity = new BehaviorEntity();
            behaviorEntity.setUserId(userId);
            behaviorEntity.setBehaviorTypeVO(BehaviorTypeVO.SIGN);
            behaviorEntity.setOutBusinessNo(new SimpleDateFormat("yyyyMMdd").format(new Date()));
            List<String> orderIds = rebateService.createRebateOrder(behaviorEntity);
            log.info("======================[calendarSignRebate]用户签到返现成功 userId:{} orderIds:{} ======================", userId, orderIds);
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), true);
        } catch (AppException e) {
            log.error("======================[calendarSignRebate]用户签到返现异常 userId:{} ======================", userId, e);
            return new BaseResponse<>(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("======================[calendarSignRebate]用户签到返现异常 userId:{} ======================", userId, e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }

    @Override
    @PostMapping("/query_user_activity_account")
    public BaseResponse<UserActivityAccountResponseDTO> queryUserActivityAccount(@RequestBody UserActivityAccountRequestDTO requestDTO) {
        try {
            log.info("======================[queryUserActivityAccount]查询用户参与次数开始 userId:{} activityId:{} ======================", requestDTO.getUserId(), requestDTO.getActivityId());
            // 1.参数校验
            if (StringUtils.isBlank(requestDTO.getUserId()) || requestDTO.getActivityId() == null) {
                throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getMessage());
            }
            // 2.查询
            ActivityAccountEntity activityAccountEntity = activityQuotaService.queryUserActivityAccount(requestDTO.getUserId(), requestDTO.getActivityId());
            // 3.返回结果
            UserActivityAccountResponseDTO res = new UserActivityAccountResponseDTO();
            BeanUtils.copyProperties(activityAccountEntity, res);
            log.info("======================[queryUserActivityAccount]查询用户参与次数成功 userId:{} activityId:{} ======================", requestDTO.getUserId(), requestDTO.getActivityId());
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), res);
        } catch (AppException e) {
            log.error("======================[queryUserActivityAccount]查询用户参与次数异常 userId:{} activityId:{} ======================", requestDTO.getUserId(), requestDTO.getActivityId(), e);
            return new BaseResponse<>(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("======================[queryUserActivityAccount]查询用户参与次数异常 userId:{} activityId:{} ======================", requestDTO.getUserId(), requestDTO.getActivityId(), e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }

    @Override
    @PostMapping("/is_calendar_sign_rebate")
    public BaseResponse<Boolean> isCalendarSignRebate(@RequestParam String userId) {
        try {
            log.info("======================[isCalendarSignRebate]查询用户当日是否已签到开始 userId:{} ======================", userId);
            String outBusinessNo = new SimpleDateFormat("yyyyMMdd").format(new Date());
            List<RebateOrderEntity> rebateOrderEntities = rebateService.queryRebateOrder(userId, outBusinessNo);
            boolean b = !rebateOrderEntities.isEmpty();
            log.info("======================[isCalendarSignRebate]查询用户当日是否已签到成功 userId:{} 当日是否已签到:{} ======================", userId, b);
            return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), b);
        } catch (AppException e) {
            log.error("======================[isCalendarSignRebate]查询用户当日是否已签到异常 userId:{} ======================", userId, e);
            return new BaseResponse<>(e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("======================[isCalendarSignRebate]查询用户当日是否已签到异常 userId:{} ======================", userId, e);
            return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
        }
    }

}
