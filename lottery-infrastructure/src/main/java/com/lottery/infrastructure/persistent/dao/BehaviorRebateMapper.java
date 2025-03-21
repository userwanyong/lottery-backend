package com.lottery.infrastructure.persistent.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lottery.infrastructure.persistent.po.BehaviorRebate;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 永
 * @description 针对表【behavior_rebate(返利活动配置表)】的数据库操作Mapper
 */
@Mapper
public interface BehaviorRebateMapper extends BaseMapper<BehaviorRebate> {

}




