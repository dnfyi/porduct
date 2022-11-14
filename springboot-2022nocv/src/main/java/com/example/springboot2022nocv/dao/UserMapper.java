package com.example.springboot2022nocv.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot2022nocv.entity.User;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserMapper extends BaseMapper<User> {

    @Select("select * from user where username= #{username} and password= #{password}")
    User login(String username, String password);

}
