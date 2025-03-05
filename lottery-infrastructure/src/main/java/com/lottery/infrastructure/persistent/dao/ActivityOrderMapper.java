package com.lottery.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import com.lottery.infrastructure.persistent.po.ActivityOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 永
 * @description 针对表【activity_order(抽奖活动单)】的数据库操作Mapper
 */
@Mapper
@DBRouterStrategy(splitTable = true) //执行 MyBaits 操作的时候，对 SQL 语句进行动态变更。
public interface ActivityOrderMapper extends BaseMapper<ActivityOrder> {

    @DBRouter(key = "userId") //指定对哪个SQL的操作进行路由,默认路由字段就是 userId
    @Insert("insert into activity_order (user_id, activity_id,sku, activity_name, strategy_id, order_id, order_time, total_count, day_count, month_count, state) values (#{userId}, #{activityId},#{sku}, #{activityName}, #{strategyId}, #{orderId}, #{orderTime}, #{totalCount}, #{dayCount}, #{monthCount}, #{state})")
    void myinsert(ActivityOrder activityOrder);

    @DBRouter
    @Select("select * from activity_order where user_id = #{userId}")
    List<ActivityOrder> queryRaffleActivityOrderByUserId(String userId);

}




