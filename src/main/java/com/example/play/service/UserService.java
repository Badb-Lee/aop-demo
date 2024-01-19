package com.example.play.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.play.enity.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService extends IService<User> {

    int deleteByIds(Long[] ids);

    int addUser(User user);

    User findByUsername(User user);

    User findUserById(String userId);
}
