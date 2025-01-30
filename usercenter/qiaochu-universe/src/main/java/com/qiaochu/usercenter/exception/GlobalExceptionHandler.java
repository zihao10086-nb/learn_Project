package com.qiaochu.usercenter.exception;

import com.qiaochu.usercenter.common.BaseResponse;
import com.qiaochu.usercenter.common.ErrorCode;
import com.qiaochu.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e){
        log.error("runtimeException"+e.getMessage(),e);
        return ResultUtils.error(e.getCode(),e.getMessage(),e.getDescription());
    }
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse businessExceptionHandler(RuntimeException e){
        log.error("runtimeException",e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,"");
    }
}
