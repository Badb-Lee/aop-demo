package com.example.play.aop;


import com.alibaba.druid.util.StringUtils;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.play.annotation.AuthCheck;
import com.example.play.annotation.LogAnno;
import com.example.play.annotation.PassToken;
import com.example.play.common.RespBean;
import com.example.play.common.RespBeanEnum;
import com.example.play.constant.UserRole;
import com.example.play.enity.Log;
import com.example.play.enity.User;
import com.example.play.exception.BusinessException;
import com.example.play.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
@Order(1)
public class LoginInterceptor {

    @Autowired
    UserService userService;


    //所有controller层
    @Around("execution(* com.example.play.controller.*.*(..))")
    public Object checkToken(ProceedingJoinPoint point) throws Throwable{

        MethodSignature signature = (MethodSignature)point.getSignature();
        Method method = signature.getMethod();
        //如果有PassToken注解，则不需要进行验证
        if(method.isAnnotationPresent(PassToken.class)){
            PassToken passToken = method.getAnnotation(PassToken.class);
            if(passToken.required()){
                Object res = point.proceed();
                return res;
            }
        }

        //获取请求头
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //从http中获取token
        String token = request.getHeader("token");
        if (token == null) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR,"无token，请重新登录");
        }
        // 获取 token 中的 user id
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR,"无token，请重新登录");
        }
        //获取用户
        User user = userService.getById(userId);
        if (user == null) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR,"用户不存在，请重新登录");
        }
        // 验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR,"token验证失败");
        }

        //如果包含权限注解
        if(method.isAnnotationPresent(AuthCheck.class)){
            AuthCheck annotation = method.getAnnotation(AuthCheck.class);

            //如果注释的值是空的
            if(StringUtils.isEmpty(annotation.roleName())){
                throw new BusinessException(RespBeanEnum.ERROR,"无权限");
            }
            //如果注解的权限值是超级管理员
            if(annotation.roleName().equals(UserRole.SUPER_ROLE)){
                if(user.getRoleName().equals(UserRole.ADMIN_ROLE) || user.getRoleName().equals(UserRole.DEFAULT_ROLE)){
                    throw new BusinessException(RespBeanEnum.ERROR,"权限不够");
                }
            }
            //如果注解的权限值是普通管理员
            else if (annotation.roleName().equals(UserRole.ADMIN_ROLE)) {
                if(user.getRoleName().equals(UserRole.DEFAULT_ROLE)){
                    throw new BusinessException(RespBeanEnum.ERROR,"权限不够");
                }
            }
        }

        Object res = point.proceed();
        log.info(res.toString());
        return res;

    }
}
