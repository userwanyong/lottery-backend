package com.lottery.domain.user.service.impl;


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
public class IUserServiceImpl implements IUserService {
    @Resource
    private IUserRepository repository;

    @Override
    public UserVO login(UserDTO userDTO) {
        if (userDTO.getUsername() == null || userDTO.getPassword() == null) {
            throw new AppException(ResponseCode.LOGIN_INFO_EMPTY.getCode(), ResponseCode.LOGIN_INFO_EMPTY.getMessage());
        }
        return repository.login(userDTO);
    }

    @Override
    public UserVO refreshToken(String refreshToken) {
        return repository.refreshToken(refreshToken);
    }

    @Override
    public void logout(Long userId) {
        repository.logout(userId);
    }
}
