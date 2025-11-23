package com.lottery.types.exception;


import com.lottery.types.enums.ResponseCode;
import com.lottery.types.model.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DuplicateKeyException;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 永
 * 全局异常捕获
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public BaseResponse<Void> handleException(PermissionException e) {
        log.warn("用户权限不足");
        return new BaseResponse<>(ResponseCode.PERMISSION_DENIED.getCode(), ResponseCode.PERMISSION_DENIED.getMessage(), null);
    }

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

    @ExceptionHandler(DuplicateKeyException.class)
    public BaseResponse<?> handleSqlException(DuplicateKeyException e) {
        // 使用正则表达式提取重复的字段值
        String message = e.getMessage();
        String duplicateValue = null;
        Pattern pattern = Pattern.compile("Duplicate entry '(.*?)' for key");
        Matcher matcher = null;
        if (message != null) {
            matcher = pattern.matcher(message);
        }
        if (matcher != null && matcher.find()) {
            // 提取出重复的值
            duplicateValue = matcher.group(1);
        }
        String errorMessage = String.format("索引 {%s} 已存在", duplicateValue);
        log.warn(errorMessage);
        return new BaseResponse<>(ResponseCode.INDEX_DUP.getCode(), errorMessage);
    }
}

