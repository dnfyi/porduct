package com.example.springboot2022nocv.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.LineCaptcha;
import com.example.springboot2022nocv.entity.User;
import com.example.springboot2022nocv.service.UserService;
import com.example.springboot2022nocv.vo.DataView;
import org.apache.catalina.security.SecurityUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";

    }
    //验证码的逻辑
    @RequestMapping("/login/getCode")
    public void getCode(HttpServletResponse response, HttpSession session) throws IOException {
        //1.验证对象 HuTool定义图形验证码的长和宽,验证码的位数，干扰线的条数
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(116, 36, 4, 10);
        //2.放入到session(setAttribute是把指定的属性设置为指定的值。如果不存在具有指定名称的属性，该方法将创建一个新属性)
        session.setAttribute("code",captcha.getCode());
        //3.输出
        ServletOutputStream outputStream = response.getOutputStream();
        captcha.write(outputStream);
        //4关闭输出流
        outputStream.close();

    }
    //具体的登录逻辑
    @RequestMapping("/login/login")
    @ResponseBody
    public DataView login(String username,String password,String code,HttpSession session){
        DataView dataView = new DataView();
        //1.首先判断验证对不对
        String sessionCode = (String) session.getAttribute("code");
        if(code!=null && sessionCode.equals(code)){
            //2.session普通登录
            //User user = userService.login(username,password);
            //shiro登录
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);//把前台传过来的token传进去
            subject.login(token);
            User user = (User) subject.getPrincipal();//拿到用户的信息

            //3.判断
            if (user!=null){
                dataView.setCode(200);
                dataView.setMsg("登录成功！");
                //4.放入session
                session.setAttribute("user",user);
                return dataView;
            }else {
                dataView.setCode(100);
                dataView.setMsg("用户名或者密码错误，登录失败！");
            }
        }
        dataView.setCode(100);
        dataView.setMsg("验证码错误！");
        return dataView;

    }
    //退出登录
    @RequestMapping("/login/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "login";

    }
}
