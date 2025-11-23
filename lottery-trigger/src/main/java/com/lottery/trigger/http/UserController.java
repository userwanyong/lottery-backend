package com.lottery.trigger.http;

import com.lottery.domain.user.model.dto.UserDTO;
import com.lottery.domain.user.model.vo.UserVO;
import com.lottery.domain.user.service.impl.IUserService;
import com.lottery.trigger.api.dto.req.UserLoginRequestDTO;
import com.lottery.trigger.api.dto.res.UserLoginResponseDTO;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.model.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author 永
 */
@RestController
@RequestMapping("/user")
@CrossOrigin("*")
@Slf4j
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping("/login")
    public BaseResponse<UserLoginResponseDTO> login(@RequestBody UserLoginRequestDTO request) {
        log.info("[UserAdminController-login]请求参数：{}", request);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(request, userDTO);
        UserVO userVO = userService.login(userDTO);
        UserLoginResponseDTO userLoginResponseDTO = new UserLoginResponseDTO();
        BeanUtils.copyProperties(userVO, userLoginResponseDTO);
        log.info("[UserAdminController-login]登录成功：{}", userLoginResponseDTO);
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), userLoginResponseDTO);
    }
}
