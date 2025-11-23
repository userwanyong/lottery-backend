package com.lottery.infrastructure.adapter.repository;


import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lottery.domain.user.model.dto.UserDTO;
import com.lottery.domain.user.model.vo.UserVO;
import com.lottery.domain.user.repository.IUserRepository;
import com.lottery.infrastructure.dao.UserMapper;
import com.lottery.infrastructure.dao.po.User;
import com.lottery.types.util.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @author 永
 */
@Slf4j
@Repository
public class UserRepositoryImpl implements IUserRepository {

    @Resource
    private UserMapper userMapper;
    @Override
    public UserVO login(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new RuntimeException("用户名不存在");
        }
        if (!BCrypt.checkpw(userDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setToken(JWTUtils.createJWT(user.getId(), user.getUsername(),user.getRole()));
        return userVO;
    }
}
