package com.example.springboot2022nocv.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot2022nocv.entity.LineTrend;
import com.example.springboot2022nocv.entity.NocvData;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface IndexMapper extends BaseMapper<NocvData> {
    /**
     * 接口只有方法，没有写业务逻辑
     * 1.实现类，写自己的业务逻辑
     * 2.xml mybatisplus 一种实现
     * 3.注解形式 @select
     */
    @Select("select * from line_trend order by create_time desc limit 7")
    List<LineTrend> findSevenData();

    @Select("select * from nocv_data order by id desc limit 34")
    List<NocvData> listOrderByIdLimit34();

}
