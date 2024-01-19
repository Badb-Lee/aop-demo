package com.example.play.enity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;


@Data
public class Log implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(type = IdType.AUTO)
    private Integer id;

    private String operator;

    private String operateType;

    @DateTimeFormat(pattern = "yyyy-mm-dd hh:mm:ss")
    private Date operateDate;

    private String operateResult;

}
