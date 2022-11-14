package com.example.springboot2022nocv.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot2022nocv.entity.ChinaTotal;

public interface ChinaTotalService extends IService<ChinaTotal> {
    Integer maxID();
}
