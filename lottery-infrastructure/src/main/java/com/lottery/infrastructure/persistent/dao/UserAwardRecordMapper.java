package com.lottery.infrastructure.persistent.dao;

import com.lottery.infrastructure.persistent.po.UserAwardRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 永
* @description 针对表【user_award_record(用户中奖记录表)】的数据库操作Mapper
*/
@Mapper
public interface UserAwardRecordMapper extends BaseMapper<UserAwardRecord> {

}




