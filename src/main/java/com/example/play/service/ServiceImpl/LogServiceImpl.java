package com.example.play.service.ServiceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.play.enity.Log;
import com.example.play.mapper.LogMapper;
import com.example.play.service.LogService;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {
}
