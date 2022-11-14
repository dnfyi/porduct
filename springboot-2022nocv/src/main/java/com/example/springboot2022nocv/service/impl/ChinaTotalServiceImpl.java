package com.example.springboot2022nocv.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot2022nocv.dao.ChinaTotalMapper;
import com.example.springboot2022nocv.entity.ChinaTotal;
import com.example.springboot2022nocv.service.ChinaTotalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChinaTotalServiceImpl extends ServiceImpl<ChinaTotalMapper, ChinaTotal> implements ChinaTotalService {
    @Autowired
    private ChinaTotalMapper chinaTotalMapper;
    @Override
    public Integer maxID() {
        return chinaTotalMapper.maxID();
    }
}
