package com.example.springboot2022nocv.controller;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot2022nocv.entity.NocvData;
import com.example.springboot2022nocv.entity.NocvGlobalData;
import com.example.springboot2022nocv.service.GlobalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class GlobalController {
    @Autowired
    private GlobalService globalService;
    //跳转页面
    @RequestMapping("/toGlobal")
    public String toGlobal(){
        return "global";

    }
    @RequestMapping("/queryGlobal")
    @ResponseBody
//    查数据库
    public List<NocvGlobalData> queryGlobal() {
        List<NocvGlobalData> list = globalService.list();
        return list;

    }

}
