package com.example.play.service.ServiceImpl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.play.enity.User;
import com.example.play.mapper.UserMapper;
import com.example.play.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;


    @Override
    public int deleteByIds(Long[] ids) {
        return 0;
    }

    @Override
    public int addUser(User user) {
        return 0;
    }

    public User findByUsername(User user){
        return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername,user.getUsername()));
    }

    public User findUserById(String userId) {
        return userMapper.selectById(userId);
    }

}
