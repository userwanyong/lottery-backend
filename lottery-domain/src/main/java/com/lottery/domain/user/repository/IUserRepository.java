package com.lottery.domain.user.repository;


import com.lottery.domain.user.model.dto.UserDTO;
import com.lottery.domain.user.model.vo.UserVO;

/**
 * @author 永
 */
public interface IUserRepository {
    UserVO login(UserDTO userDTO);
}
