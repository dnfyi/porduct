package com.example.springboot2022nocv.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot2022nocv.entity.User;

import javax.servlet.http.HttpSession;

public interface UserService extends IService<User> {
    User login(String username, String password);

    void saveUserRole(Integer uid, Integer[] ids);

}
