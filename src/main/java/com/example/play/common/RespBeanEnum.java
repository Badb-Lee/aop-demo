package com.example.play.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RespBeanEnum {
    SUCCESS(200,"success"),

    ERROR(500,"error"),

    LOGIN_ERROR(50001,"LOGIN_ERROR");


    private final int code;

    private final String msg;


}
