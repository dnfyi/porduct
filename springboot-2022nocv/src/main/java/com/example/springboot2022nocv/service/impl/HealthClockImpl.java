package com.example.springboot2022nocv.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot2022nocv.dao.HealthClockMapper;
import com.example.springboot2022nocv.entity.HealthClock;
import com.example.springboot2022nocv.service.HealthClockService;
import org.springframework.stereotype.Service;

@Service
public class HealthClockImpl extends ServiceImpl<HealthClockMapper, HealthClock> implements HealthClockService {
}
