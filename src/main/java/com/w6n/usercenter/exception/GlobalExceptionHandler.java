package com.w6n.usercenter.exception;

import com.w6n.usercenter.common.BaseResponse;
import com.w6n.usercenter.common.ErrorCode;
import com.w6n.usercenter.common.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<Object>  businessExceptionHandler(BusinessException e){
        log.error("businessException:"+e.getMessage(),e);
        return ResultUtil.error(e.getCode(),e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<Object> runtimeExceptionHandler(RuntimeException e){
        log.error("runtimeException",e);
        return ResultUtil.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"");
    }

}
