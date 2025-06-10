package com.lottery.infrastructure.es;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lottery.infrastructure.dao.po.UserBehaviorRebateOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 永
 * @description 针对表【user_behavior_rebate_order_000(用户行为返利流水表)】的数据库操作Mapper
 */
@Mapper
public interface EsUserBehaviorRebateOrderMapper extends BaseMapper<UserBehaviorRebateOrder> {

}




