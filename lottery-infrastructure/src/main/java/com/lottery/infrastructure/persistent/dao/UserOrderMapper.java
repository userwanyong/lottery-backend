package com.lottery.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import com.lottery.infrastructure.persistent.po.UserOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
* @author 永
* @description 针对表【user_order(用户抽奖订单表)】的数据库操作Mapper
*/
@Mapper
@DBRouterStrategy(splitTable = true)
public interface UserOrderMapper extends BaseMapper<UserOrder> {

    @DBRouter
    @Select("select * from user_order where user_id = #{userId} and activity_id = #{activityId} and order_state='create'")
    UserOrder queryNoUsedPartakeOrder(UserOrder userOrder);

    @DBRouter
    @Update("update user_order set order_state = 'used' where user_id = #{userId} and activity_id = #{activityId} and order_state='create'")
    int updateUserOrderStateUsed(UserOrder userOrder);
}




