package com.lottery.infrastructure.dao;

import com.lottery.infrastructure.dao.po.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 永
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2025-11-22 15:31:16
* @Entity com.lottery.infrastructure.dao.po.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




