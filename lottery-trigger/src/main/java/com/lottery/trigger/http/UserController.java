package com.lottery.trigger.http;

import com.lottery.domain.user.model.dto.UserDTO;
import com.lottery.domain.user.model.vo.UserVO;
import com.lottery.domain.user.service.impl.IUserService;
import com.lottery.trigger.api.dto.req.RefreshTokenRequestDTO;
import com.lottery.trigger.api.dto.req.UserLoginRequestDTO;
import com.lottery.trigger.api.dto.res.UserLoginResponseDTO;
import com.lottery.types.enums.ResponseCode;
import com.lottery.types.model.BaseResponse;
import com.lottery.types.util.ThreadUtils;
import com.lottery.types.util.UserUtils;
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
        log.info("[UserController-login]请求参数：{}", request);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(request, userDTO);
        UserVO userVO = userService.login(userDTO);
        UserLoginResponseDTO responseDTO = new UserLoginResponseDTO();
        responseDTO.setId(userVO.getId());
        responseDTO.setUsername(userVO.getUsername());
        responseDTO.setAccessToken(userVO.getToken());
        responseDTO.setRefreshToken(userVO.getRefreshToken());
        responseDTO.setExpiresIn(userVO.getExpiresIn());
        log.info("[UserController-login]登录成功：{}", responseDTO);
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), responseDTO);
    }

    @PostMapping("/refresh")
    public BaseResponse<UserLoginResponseDTO> refresh(@RequestBody RefreshTokenRequestDTO request) {
        log.info("[UserController-refresh]刷新token请求");
        UserVO userVO = userService.refreshToken(request.getRefreshToken());
        UserLoginResponseDTO responseDTO = new UserLoginResponseDTO();
        responseDTO.setId(userVO.getId());
        responseDTO.setUsername(userVO.getUsername());
        responseDTO.setAccessToken(userVO.getToken());
        responseDTO.setRefreshToken(userVO.getRefreshToken());
        responseDTO.setExpiresIn(userVO.getExpiresIn());
        log.info("[UserController-refresh]token刷新成功");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), responseDTO);
    }

    @PostMapping("/logout")
    public BaseResponse<Void> logout() {
        log.info("[UserController-logout]退出登录请求");
        UserUtils userUtils = ThreadUtils.getUser();
        if (userUtils != null && userUtils.getId() != null) {
            userService.logout(userUtils.getId());
        }
        log.info("[UserController-logout]退出登录成功");
        return new BaseResponse<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }
}
