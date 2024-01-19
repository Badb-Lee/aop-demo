package com.example.play.controller;


import cn.hutool.json.JSONObject;
import com.alibaba.druid.util.StringUtils;
import com.example.play.annotation.AuthCheck;
import com.example.play.annotation.LogAnno;
import com.example.play.annotation.PassToken;
import com.example.play.common.RespBean;
import com.example.play.common.RespBeanEnum;
import com.example.play.constant.UserRole;
import com.example.play.enity.Log;
import com.example.play.enity.User;
import com.example.play.service.LogService;
import com.example.play.service.UserService;
import com.example.play.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;


@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LogService logService;

    @PutMapping("/add")
    @AuthCheck(roleName = UserRole.SUPER_ROLE)
    @LogAnno(operation = "添加用户")
    public RespBean addUser(@RequestBody User user){
        if(userService.findByUsername(user) != null){
            return RespBean.error(RespBeanEnum.ERROR,"用户名重复");
        }
        return userService.save(user) ? RespBean.success(user) : RespBean.error(RespBeanEnum.ERROR);
    }

    /**
     * 查询用户信息
     * @return
     */
    @GetMapping("/list")
    @AuthCheck(roleName = UserRole.ADMIN_ROLE)
    @LogAnno(operation = "查看用户列表")
    public RespBean list(){
        return RespBean.success(userService.list());
    }


    /**
     * 登录验证
     * @param user
     * @param response
     * @return
     */
    @PostMapping("/login")
    @PassToken
    public RespBean login(@RequestBody User user, HttpServletResponse response){
        JSONObject jsonObject = new JSONObject();
        //获取用户账号密码
        User userForBase = new User();
        userForBase.setId(userService.findByUsername(user).getId());
        userForBase.setUsername(userService.findByUsername(user).getUsername());
        userForBase.setPassword(userService.findByUsername(user).getPassword());

        //初始化日志
        Log logInfo = new Log();
        logInfo.setOperator(user.getUsername());
        logInfo.setOperateDate(new Date());
        logInfo.setOperateType("登录");
        //判断账号或密码是否正确
        if (!userForBase.getPassword().equals(user.getPassword())) {
            //保存日志
            logInfo.setOperateResult("error");
            logService.save(logInfo);
            return RespBean.error(RespBeanEnum.ERROR);
        } else {

            //获取token
            String token = TokenUtils.getToken(userForBase);
            jsonObject.put("token", token);
            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/");
            response.addCookie(cookie);

            //保存日志
            logInfo.setOperateResult("success");
            logService.save(logInfo);
            return RespBean.success(jsonObject);
        }
    }
}
