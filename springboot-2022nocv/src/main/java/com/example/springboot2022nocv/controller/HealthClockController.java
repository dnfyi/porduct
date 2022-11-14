package com.example.springboot2022nocv.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot2022nocv.entity.HealthClock;
import com.example.springboot2022nocv.service.HealthClockService;
import com.example.springboot2022nocv.vo.DataView;
import com.example.springboot2022nocv.vo.HealthClockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HealthClockController {
    @Autowired
    private HealthClockService healthClockService;
//跳转页面
    @RequestMapping("/toHealthClock")
    public  String toHealthClock(){
        return "admin/healthclock";
    }

    //健康打卡页面 查询所有打卡记录
    @RequestMapping("/listHealthClock")
    @ResponseBody
    public DataView listHealthClock(HealthClockVo healthClockVo){
        //查询所有带有模糊查询条件 带有分页
        IPage<HealthClock> page = new Page<>(healthClockVo.getPage(),healthClockVo.getLimit());
        QueryWrapper<HealthClock> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(healthClockVo.getUsername()!=null,"username",healthClockVo.getUsername());//用户名字 模糊查询
        queryWrapper.eq(healthClockVo.getPhone()!=null,"phone",healthClockVo.getPhone());//手机号码 等值查询
        healthClockService.page(page,queryWrapper);
        return new DataView(page.getTotal(),page.getRecords());
    }
    //添加或者修改健康打卡记录数据
    @RequestMapping("/addOrUpdateHealthClock")
    @ResponseBody
    public DataView addOrUpdateHealthClock(HealthClock healthClock){
        boolean save = healthClockService.saveOrUpdate(healthClock);
        DataView dataView = new DataView();
        if (save){
            dataView.setCode(200);
            dataView.setMsg("添加成功！");
            return dataView;
        }
        dataView.setCode(100);
        dataView.setMsg("添加失败！");
        return dataView;
    }
    //删除功能
    @RequestMapping("/deleteHealthClockById")
    @ResponseBody
    public DataView deleteHealthClockById(Integer id){
        healthClockService.removeById(id);
        DataView dataView = new DataView();
        dataView.setCode(200);
        dataView.setMsg("删除数据成功！");
        return dataView;
    }
}
