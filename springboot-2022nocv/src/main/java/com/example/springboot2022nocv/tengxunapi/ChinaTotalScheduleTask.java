package com.example.springboot2022nocv.tengxunapi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.springboot2022nocv.entity.ChinaTotal;
import com.example.springboot2022nocv.entity.NocvData;
import com.example.springboot2022nocv.service.ChinaTotalService;
import com.example.springboot2022nocv.service.IndexService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//对应的是中国疫情地图上对应省份的确诊数量
@Component
public class ChinaTotalScheduleTask {
    @Autowired
    private ChinaTotalService chinaTotalService;
    @Autowired
    private IndexService indexService;

    //每小时全国疫情数据更新一次
    @Scheduled(fixedDelay = 100000)//100s执行一次
//    @Scheduled(cron = "0 0 8,9,10,12,13,18,20 * *?")//定时更新数据
    public void updateChinaTotalToDB() throws Exception {
        HttpUtils httpUtils = new HttpUtils();
        String string = httpUtils.getData();
        System.out.println("数据："+string);
        //1.所有格式的alibaba格式（将string格式转化为json）
        JSONObject jsonObject = JSONObject.parseObject(string);
        Object data = jsonObject.get("data");
        System.out.println("data:"+data);
        //2.拿到data（chinaTotal数据）
        JSONObject jsonObjectData = JSONObject.parseObject(data.toString());
        Object chinaTotal = jsonObjectData.get("chinaTotal");
        Object lastUpdateTime = jsonObjectData.get("overseaLastUpdateTime");
        System.out.println("chinaTotal:"+chinaTotal);
        //3.解析total
        JSONObject jsonObjectTotal = JSONObject.parseObject(chinaTotal.toString());
        Object total = jsonObjectTotal.get("total");
        System.out.println("total:"+total);
        //4.全国数据total
        JSONObject totalData = JSONObject.parseObject(total.toString());
        Object confirm = totalData.get("confirm");
        Object input = totalData.get("input");
        Object severe = totalData.get("severe");
        Object heal = totalData.get("heal");
        Object dead = totalData.get("dead");
        Object suspect = totalData.get("suspect");

        //5.将上述的变量名(实体）进行赋值
        ChinaTotal dataEntity = new ChinaTotal();
        dataEntity.setConfirm((Integer) confirm);
        dataEntity.setInput((Integer) input);
        dataEntity.setSevere((Integer) severe);
        dataEntity.setHeal((Integer) heal);
        dataEntity.setDead((Integer) dead);
        dataEntity.setSuspect((Integer) suspect);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dataEntity.setUpdateTime(format.parse(String.valueOf(lastUpdateTime)));//将时间转为string类型


        //6.插入数据库
        chinaTotalService.save(dataEntity);

        //中国各个省份的疫情数据自动刷新
        //拿到areaTree
        JSONArray areaTree = jsonObjectData.getJSONArray("areaTree");
        Object[] objects = areaTree.toArray();
        //遍历所有国家
       /* for (int i = 0; i < objects.length; i++) {
            JSONObject jsonObject1 = JSONObject.parseObject(objects[i].toString());
            Object name = jsonObject1.get("name");
            System.out.println(name);
        }*/

        //拿到中国疫情数据
        JSONObject jsonObject1 = JSONObject.parseObject(objects[2].toString());
        JSONArray children = jsonObject1.getJSONArray("children");
        Object[] objects1 = children.toArray();//object1为各个省份的名字

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<NocvData> list = new ArrayList<>();
        //遍历中国地区数据
       for (int i = 0; i < objects1.length; i++) {
            NocvData nocvData = new NocvData();
            JSONObject jsonObject2 = JSONObject.parseObject(objects1[i].toString());
            Object name = jsonObject2.get("name");//省份名字
            Object timePro = jsonObject2.get("lastUpdateTime");//省份更新数据时间
            Object total1 = jsonObject2.get("total");//确诊人数
            JSONObject jsonObject3 = JSONObject.parseObject(total1.toString());//total数据
            Object confirm1 = jsonObject3.get("confirm");//累计确诊数据
           //获取累计死亡数量 治愈人数
           Object heal1 = jsonObject3.get("heal");//累计治愈数据
           Object dead1 = jsonObject3.get("dead");//累计死亡数据
            //现存确诊(累计确诊-治愈人数-死亡人数）
           int xianconfirm = Integer.parseInt(confirm1.toString())-Integer.parseInt(heal1.toString())-Integer.parseInt(dead1.toString());

           //System.out.println("省份->"+name+": "+confirm1+"人");
           //将上面的属性进行赋值
            nocvData.setName(name.toString());
            nocvData.setValue(xianconfirm);
            if (timePro==null){
                nocvData.setUpdateTime(new Date());
            }else {
                nocvData.setUpdateTime(format1.parse(String.valueOf(timePro)));
            }
            list.add(nocvData);

       }
       //各个省份的数据插入数据库（实时更新疫情数据）
        indexService.saveBatch(list);


    }
}
