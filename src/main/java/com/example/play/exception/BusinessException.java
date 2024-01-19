package com.example.play.exception;


import com.example.play.common.RespBeanEnum;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private final int code;

    public BusinessException(int code, String msg){
        super(msg);
        this.code = code;
    }

    public BusinessException(RespBeanEnum respBeanEnum){
        super(respBeanEnum.getMsg());
        this.code = respBeanEnum.getCode();
    }

    public BusinessException(RespBeanEnum respBeanEnum,String msg){
        super(msg);
        this.code = respBeanEnum.getCode();
    }






}
