package com.example.springboot2022nocv.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot2022nocv.entity.LineTrend;
import com.example.springboot2022nocv.entity.NocvData;

import java.util.List;

public interface IndexService extends IService<NocvData> {
    List<LineTrend> findSevenData();

    List<NocvData> listOrderByLimit34();

}
