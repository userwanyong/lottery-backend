package com.lottery.trigger.api;

import com.lottery.trigger.api.dto.req.ActivityDrawRequestDTO;
import com.lottery.trigger.api.dto.req.SkuProductShopCartRequestDTO;
import com.lottery.trigger.api.dto.req.UserActivityAccountRequestDTO;
import com.lottery.trigger.api.dto.res.ActivityDrawResponseDTO;
import com.lottery.trigger.api.dto.res.SkuProductResponseDTO;
import com.lottery.trigger.api.dto.res.UserActivityAccountResponseDTO;
import com.lottery.types.model.BaseResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 永
 * 抽奖活动相关服务接口
 */
public interface LotteryActivityService {
    /**
     * 活动装配，数据预热缓存
     *
     * @param activityId 活动ID
     * @return 装配结果
     */
    BaseResponse<Boolean> armory(Long activityId);

    /**
     * 活动抽奖接口
     *
     * @param request 请求对象
     * @return 返回结果
     */
    BaseResponse<ActivityDrawResponseDTO> draw(ActivityDrawRequestDTO request);

    /**
     * 日历签到返利接口
     *
     * @param userId 用户ID
     * @return 签到结果
     */
    BaseResponse<Boolean> calendarSignRebate(String userId);

    /**
     * 查询账户额度接口
     *
     * @param requestDTO 请求参数
     * @return UserActivityAccountResponseDTO
     */
    BaseResponse<UserActivityAccountResponseDTO> queryUserActivityAccount(UserActivityAccountRequestDTO requestDTO);

    /**
     * 查询今日是否已签到
     *
     * @param userId 用户id
     * @return Boolean
     */
    BaseResponse<Boolean> isCalendarSignRebate(String userId);

    /**
     * 查询sku商品集合
     *
     * @param activityId 活动ID
     * @return 商品集合
     */
    BaseResponse<List<SkuProductResponseDTO>> querySkuProductListByActivityId(Long activityId);

    /**
     * 查询用户积分值
     *
     * @param userId 用户ID
     * @return 可用积分
     */
    BaseResponse<BigDecimal> queryUserCreditAccount(String userId);

    /**
     * 积分支付兑换商品
     *
     * @param request 请求对象「用户ID、sku商品ID」
     * @return Boolean
     */
    BaseResponse<Boolean> creditPayExchangeSku(SkuProductShopCartRequestDTO request);

}
