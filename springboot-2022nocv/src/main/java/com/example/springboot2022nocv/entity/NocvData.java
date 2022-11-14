package com.example.springboot2022nocv.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.thymeleaf.spring5.util.SpringRequestUtils;

import java.util.Date;

//data：对属性进行赋值
@Data
@TableName("nocv_data")
public class NocvData {
    @TableId(value = "id",type= IdType.AUTO)
    private Integer id;
    private String name;
    private Integer value;
    private Date updateTime;
}
