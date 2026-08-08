package com.lottery.infrastructure.dao;

import com.lottery.infrastructure.dao.po.UserOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
* @author 永
* @description 针对表【user_order(用户抽奖订单表)】的数据库操作Mapper
*/
@Mapper
public interface UserOrderMapper extends BaseMapper<UserOrder> {

    @Select("select * from user_order where user_id = #{userId} and activity_id = #{activityId} and order_state='create' limit 1")
    UserOrder queryNoUsedPartakeOrder(UserOrder userOrder);

    @Update("update user_order set order_state = 'used' where user_id = #{userId} and activity_id = #{activityId} and id=#{id} and order_state='create'")
    int updateUserOrderStateUsed(UserOrder userOrder);
}
