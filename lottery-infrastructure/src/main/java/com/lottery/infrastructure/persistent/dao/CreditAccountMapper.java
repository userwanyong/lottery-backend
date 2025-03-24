package com.lottery.infrastructure.persistent.dao;

import cn.bugstack.middleware.db.router.annotation.DBRouterStrategy;
import com.lottery.infrastructure.persistent.po.CreditAccount;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 永
* @description 针对表【credit_account(积分账户表)】的数据库操作Mapper
*/
@Mapper
public interface CreditAccountMapper extends BaseMapper<CreditAccount> {

    int update(CreditAccount creditAccount);
}




