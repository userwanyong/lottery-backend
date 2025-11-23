package com.lottery.domain.user.service.impl;


import cn.hutool.crypto.digest.BCrypt;
import com.lottery.domain.user.model.dto.UserDTO;
import com.lottery.domain.user.model.vo.UserVO;
import com.lottery.domain.user.repository.IUserRepository;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.exception.AppException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 永
 */
@Service
public class IUserServiceImpl implements IUserService{
    @Resource
    private IUserRepository repository;
    @Override
    public UserVO login(UserDTO userDTO) {
        // 校验
        String password = userDTO.getPassword();
        if (userDTO.getUsername() == null || password == null) {
            throw new AppException(ResponseCode.LOGIN_INFO_EMPTY.getCode(), ResponseCode.LOGIN_INFO_EMPTY.getMessage());
        }
        // 加密
//        userDTO.setPassword(BCrypt.hashpw(password));
        return repository.login(userDTO);
    }
}
