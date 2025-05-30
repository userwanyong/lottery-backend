package com.lottery.infrastructure.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import com.lottery.infrastructure.dao.po.UserCreditOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 永
* @description 针对表【user_credit_order_000(用户积分订单记录)】的数据库操作Mapper
*/
@Mapper
@DBRouterStrategy(splitTable = true)
public interface UserCreditOrderMapper extends BaseMapper<UserCreditOrder> {

}




