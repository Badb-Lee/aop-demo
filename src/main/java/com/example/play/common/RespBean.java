package com.example.play.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespBean {
    private Integer code;
    private String msg;
    private Object data;

    public static RespBean success(){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(),RespBeanEnum.SUCCESS.getMsg(),null);
    }

    public static RespBean success(Object data){
        return new RespBean(RespBeanEnum.SUCCESS.getCode(), RespBeanEnum.SUCCESS.getMsg(), data);
    }

    public static RespBean error(Object data){
        return new RespBean(RespBeanEnum.ERROR.getCode(),RespBeanEnum.ERROR.getMsg(),data);
    }

    public static RespBean error(RespBeanEnum respBeanEnum,Object data){
        return new RespBean(respBeanEnum.getCode(), respBeanEnum.getMsg(), data);
    }
}
