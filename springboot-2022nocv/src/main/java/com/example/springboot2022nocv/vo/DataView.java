package com.example.springboot2022nocv.vo;

import com.example.springboot2022nocv.service.IndexService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//视图返回（table表）
@Data
//全参构造函数
@AllArgsConstructor
//无参构造函数
@NoArgsConstructor
public class DataView {
    private Integer code = 0;
    public String msg="";
    private Long count = 0L;
    private Object data;

    public DataView (Long count,Object data){
        this.count=count;
        this.data=data;
    }
    public DataView(Object data){
        this.data=data;
    }





}
