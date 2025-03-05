package com.lottery.infrastructure.persistent.dao;

import com.lottery.infrastructure.persistent.po.ActivityAccountFlow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 永
* @description 针对表【activity_account_flow(抽奖活动账户流水表)】的数据库操作Mapper
*/
@Mapper
public interface ActivityAccountFlowMapper extends BaseMapper<ActivityAccountFlow> {

}




