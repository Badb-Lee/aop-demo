package com.example.play.exception;


import com.example.play.common.RespBean;
import com.example.play.common.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public RespBean businessExceptionHandler(BusinessException e){
        return RespBean.error(RespBeanEnum.ERROR,e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public RespBean runtimeExceptionHandler(RuntimeException e){
        log.error(e.getMessage());
        return RespBean.error(RespBeanEnum.ERROR,"系统错误");
    }
}
