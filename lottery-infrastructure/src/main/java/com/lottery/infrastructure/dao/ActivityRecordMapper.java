package com.lottery.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lottery.infrastructure.dao.po.ActivityRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 永
 * @description 针对表【activity_record(抽奖活动单)】的数据库操作Mapper
 */
@Mapper
public interface ActivityRecordMapper extends BaseMapper<ActivityRecord> {

}
