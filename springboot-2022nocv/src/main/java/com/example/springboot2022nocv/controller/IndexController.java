package com.example.springboot2022nocv.controller;

import com.example.springboot2022nocv.entity.ChinaTotal;
import com.example.springboot2022nocv.entity.LineTrend;
import com.example.springboot2022nocv.entity.NocvData;
import com.example.springboot2022nocv.service.ChinaTotalService;
import com.example.springboot2022nocv.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.*;

//主控页面【嵌套china地图页面】

@Controller
@CrossOrigin
public class IndexController {
//返回数据给前台
    @Autowired
    private IndexService indexService;
    @Autowired
    private ChinaTotalService chinaTotalService;

    /*
    查询chinatotal数据 显示插入数据库中最新的一条
     */
    @RequestMapping("/")
    public String index(Model model) throws ParseException {
        //1.找到id最大的一条数据
        Integer id = chinaTotalService.maxID();
        //2.根据id查找数据
        ChinaTotal chinaTotal = chinaTotalService.getById(id);
        //把chinatotal显示在前台
        model.addAttribute("chinaTotal",chinaTotal);
        return "index";
    }

    //跳转到china地图页面
    @RequestMapping("/toChina")
    public String toChina(Model model) throws ParseException {
        //1.找到id最大的一条数据
        Integer id = chinaTotalService.maxID();
        //2.根据id查找数据
        ChinaTotal chinaTotal = chinaTotalService.getById(id);
        //把chinatotal显示在前台
        model.addAttribute("chinaTotal",chinaTotal);
        return "china";
    }



    //发送请求
    @RequestMapping("/query")
    @ResponseBody
    //查数据库
    public List<NocvData> queryData() throws ParseException {
        //每天更新一次数据的使用场景（查询的是当天数据的总和）
        /*QueryWrapper<NocvData> queryWrapper = new QueryWrapper<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String format1 = format.format(new Date());
        queryWrapper.ge("update_time",format.parse(format1));//查询大于等于当前时间*/

        //倒叙查询最后34条数据
        List<NocvData> list = indexService.listOrderByLimit34();
        return list;
    }

    //跳转pie页面
    @RequestMapping("/toPie")
    public String toPie() {
        return "pie";

    }

    /**
     * 分组聚合
     * SQL：select count(*)from goods group by type;
     */
    @RequestMapping("/queryPie")
    @ResponseBody
    public List<NocvData> queryPieData() {
        List<NocvData> list = indexService.listOrderByLimit34();
        return list;
    }

    //跳转柱状图页面
    @RequestMapping("/toBar")
    public String toBar() {
        return "bar";
    }

    @RequestMapping("/queryBar")
    @ResponseBody
    public Map<String, List<Object>> queryBarData() {
        //1.所有城市数据：数值
        List<NocvData> list = indexService.listOrderByLimit34();
        //2.所有城市数据
        List<String> cityList = new ArrayList<>();
        for (NocvData data : list) {
            cityList.add(data.getName());
        }
        //3.所有疫情数值数据
        List<Integer> dataList = new ArrayList<>();
        for (NocvData data : list) {
            dataList.add(data.getValue());
        }
        //4.创建map容器
        Map map = new HashMap();
        map.put("cityList", cityList);
        map.put("dataList", dataList);
        return map;

    }
    //跳转折线页面
    @RequestMapping("/toLine")
    public String toLine() {
        return "line";
    }

    @RequestMapping("/queryLine")
    @ResponseBody
    public Map<String, List<Object>> queryLineData() {
        //1.查询近七天所有的数据
        List<LineTrend> list7Day = indexService.findSevenData();

        //2.封装所有的确诊人数
        List<Integer> confirmList = new ArrayList<>();
        //3.封装所有的隔离人数
        List<Integer> isolateList = new ArrayList<>();
        //4.封装所有的治愈人数
        List<Integer> cureList = new ArrayList<>();
        //5.封装所有的死亡人数
        List<Integer> deadList = new ArrayList<>();
        //6.封装所有的疑似人数
        List<Integer> similarList = new ArrayList<>();
        for (LineTrend data:list7Day) {
            confirmList.add(data.getConfirm());
            isolateList.add(data.getIsolation());
            cureList.add(data.getCure());
            deadList.add(data.getDead());
            similarList.add(data.getSimilar());
        }
        //7.返回数据的格式容器 map.put:添加元素
        Map map = new HashMap<>();
        map.put("confirmList",confirmList);
        map.put("isolateList",isolateList);
        map.put("cureList",cureList);
        map.put("deadList",deadList);
        map.put("similarList",similarList);

        return map;
    }

}

