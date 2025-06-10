package com.lottery.infrastructure.es;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lottery.infrastructure.dao.po.UserCreditOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 永
 * @description es查询
 */
@Mapper
public interface EsUserCreditOrderMapper extends BaseMapper<UserCreditOrder> {

}




