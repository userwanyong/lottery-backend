package com.lottery.infrastructure.dao;

import com.lottery.infrastructure.dao.po.CreditRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 永
* @description 针对表【user_record(用户积分订单记录)】的数据库操作Mapper
*/
@Mapper
public interface CreditRecordMapper extends BaseMapper<CreditRecord> {

}
