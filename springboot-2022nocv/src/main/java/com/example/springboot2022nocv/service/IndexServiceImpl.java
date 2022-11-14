package com.example.springboot2022nocv.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot2022nocv.dao.IndexMapper;
import com.example.springboot2022nocv.entity.LineTrend;
import com.example.springboot2022nocv.entity.NocvData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexServiceImpl extends ServiceImpl<IndexMapper, NocvData> implements IndexService {
    @Autowired
    private IndexMapper indexMapper;

    @Override
    public List<LineTrend> findSevenData() {
        List<LineTrend> list = indexMapper.findSevenData();
        return list;
    }
}
