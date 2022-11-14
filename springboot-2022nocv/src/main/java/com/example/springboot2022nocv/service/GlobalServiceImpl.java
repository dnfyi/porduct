package com.example.springboot2022nocv.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot2022nocv.dao.GlobalDataMapper;
import com.example.springboot2022nocv.dao.IndexMapper;
import com.example.springboot2022nocv.entity.NocvData;
import com.example.springboot2022nocv.entity.NocvGlobalData;
import org.springframework.stereotype.Service;

@Service
public class GlobalServiceImpl extends ServiceImpl<GlobalDataMapper, NocvGlobalData> implements GlobalService  {

}
