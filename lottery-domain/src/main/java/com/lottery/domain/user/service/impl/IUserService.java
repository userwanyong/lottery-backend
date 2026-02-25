package com.lottery.domain.user.service.impl;


import com.lottery.domain.user.model.dto.UserDTO;
import com.lottery.domain.user.model.vo.UserVO;

/**
 * @author 永
 */
public interface IUserService {
    UserVO login(UserDTO userDTO);

    UserVO refreshToken(String refreshToken);

    void logout(Long userId);
}
