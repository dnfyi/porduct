package com.example.springboot2022nocv.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot2022nocv.dao.BanJiMapper;
import com.example.springboot2022nocv.entity.BanJi;
import com.example.springboot2022nocv.service.BanJiService;
import org.springframework.stereotype.Service;

@Service
public class BanJiServiceImpl extends ServiceImpl<BanJiMapper, BanJi> implements BanJiService {
}
