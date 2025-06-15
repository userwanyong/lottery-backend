package com.lottery.types.exception;


import com.lottery.types.enums.ResponseCode;
import com.lottery.types.model.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 永
 * 全局异常捕获
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public BaseResponse<?> handleAppException(AppException e) {
        log.error("业务异常：code:{}, message:{}",e.getCode() , e.getMessage());
        return new BaseResponse<>(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse<?> handleOtherException(Exception e) {
        log.error("系统异常：", e);
        return new BaseResponse<>(ResponseCode.UN_ERROR.getCode(), ResponseCode.UN_ERROR.getMessage());
    }
}

