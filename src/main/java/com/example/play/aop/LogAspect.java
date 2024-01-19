package com.example.play.aop;


import com.auth0.jwt.JWT;
import com.example.play.annotation.AuthCheck;
import com.example.play.annotation.LogAnno;
import com.example.play.common.RespBeanEnum;
import com.example.play.enity.Log;
import com.example.play.enity.User;
import com.example.play.exception.BusinessException;
import com.example.play.service.LogService;
import com.example.play.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Component
@Order(2)
@Slf4j
public class LogAspect {

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    @Around("@annotation(com.example.play.annotation.LogAnno)")
    public Object doLog(ProceedingJoinPoint point) throws Throwable{
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        //获取请求头
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //从http中获取token
        String token = request.getHeader("token");
        String userId = JWT.decode(token).getAudience().get(0);
        //获取用户
        User user = userService.getById(userId);

        //每个方法上都有LogAnno注解，所以直接把日志注解写在这里了
        Log logInfo = new Log();
        LogAnno annotation = method.getAnnotation(LogAnno.class);
        String operation = annotation.operation();
        logInfo.setOperator(user.getUsername());
        logInfo.setOperateType(operation);
        Object res = null;
        try {
            res = point.proceed();
            logInfo.setOperateResult("success");
        }catch (Exception e){
            log.error(e.getMessage());
            logInfo.setOperateResult("error");
            throw new BusinessException(RespBeanEnum.ERROR,"系统错误");
        }finally {
            logInfo.setOperateDate(new Date());
            logService.save(logInfo);
        }

        return res;


    }


}
