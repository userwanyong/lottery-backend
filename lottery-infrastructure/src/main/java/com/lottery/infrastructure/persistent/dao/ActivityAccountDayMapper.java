package com.lottery.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import com.lottery.infrastructure.persistent.po.ActivityAccountDay;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author 永
* @description 针对表【activity_account_day(抽奖活动账户表-日次数)】的数据库操作Mapper
*/
@Mapper
public interface ActivityAccountDayMapper extends BaseMapper<ActivityAccountDay> {

    @DBRouter
    @Select("select * from activity_account_day where user_id = #{userId} and activity_id = #{activityId} and day = #{day}")
    ActivityAccountDay queryActivityAccountDayByUserId(ActivityAccountDay activityAccountDay);
}




