package com.example.springboot2022nocv.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot2022nocv.entity.BanJi;
import com.example.springboot2022nocv.entity.User;
import com.example.springboot2022nocv.entity.XueYuan;
import com.example.springboot2022nocv.service.BanJiService;
import com.example.springboot2022nocv.service.RoleService;
import com.example.springboot2022nocv.service.UserService;
import com.example.springboot2022nocv.service.XueYuanService;
import com.example.springboot2022nocv.vo.DataView;
import com.example.springboot2022nocv.vo.UserVo;
import com.sun.org.apache.bcel.internal.generic.DALOAD;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private BanJiService banJiService;
    @Autowired
    private XueYuanService xueYuanService;
    @Autowired
    private RoleService roleService;

    @RequestMapping("/toUser")
    public String toUser(){

        return "user/user";
    }

    @RequestMapping("/toChangePassword")
    public Object toChangePassword(){
        return "user/changepassword";
    }

    //修改密码（进行跳转）
    @RequestMapping("/updatePassword")
    @ResponseBody
    public Object updatePassword(User user){
        userService.updateById(user);
        DataView dataView = new DataView();
        dataView.setMsg("修改密码成功！");
        dataView.setCode(200);
        return dataView;
    }
    //修改用户功能
    @RequestMapping("/updateUser")
    @ResponseBody
    public DataView updateUser(User user){
        userService.updateById(user);
        DataView dataView = new DataView();
        dataView.setMsg("修改用户成功！");
        dataView.setCode(200);
        return dataView;
    }

    //分页带有所有用户数据，带有查询条件
    @RequestMapping("/loadAllUser")
    @ResponseBody
    public DataView getAllUser(UserVo userVo){
        /*第一种方法
        if (StringUtils.isNotBlank(userVo.getUsername())){
            userService.loadUserByLeftJoin(userVo.getUsername(),userVo.getPage(),userVo.getLimit());
        }
        //2.mapper
        //@select("select a.username,b.name from user as where a.username=#{} left join ban_ji as b on a.ban_ji_id = b.id limit #limit #{},#{}")*/

        //第二种方法
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        IPage<User> page = new Page<>(userVo.getPage(),userVo.getLimit());
        queryWrapper.like(StringUtils.isNotBlank(userVo.getUsername()),"username",userVo.getUsername());
        queryWrapper.eq(StringUtils.isNotBlank(userVo.getPhone()),"phone",userVo.getPhone());
        IPage<User> iPage = userService.page(page, queryWrapper);
        for (User user : iPage.getRecords()){
            //为班级名字进行赋值
            if (user.getBanJiId()!=null){
                //班级banJiService查库
                BanJi banJi = banJiService.getById(user.getBanJiId());
                user.setBanJiName(banJi.getName());
            }
            //为学院名字赋值
            if (user.getXueYuanId()!=null){
                XueYuan xueYuan = xueYuanService.getById(user.getXueYuanId());
                user.setXueYuanName(xueYuan.getName());
            }
            //为老师名字赋值
            if (user.getTeacherId()!=null){
                User teacher = userService.getById(user.getTeacherId());
                user.setTeacherName(teacher.getUsername());
            }
        }


        return new DataView(iPage.getTotal(),iPage.getRecords());
    }

    //新增用户功能逻辑
    @RequestMapping("/addUser")
    @ResponseBody
    public DataView addUser(User user){
        userService.save(user);
        DataView dataView = new DataView();
        dataView.setMsg("添加用户成功！");
        dataView.setCode(200);
        return dataView;
    }

    //删除用户功能
    @RequestMapping("/deleteUser/{id}")
    @ResponseBody
    public DataView deleteUser (@PathVariable("id") Integer id){
        userService.removeById(id);
        DataView dataView = new DataView();
        dataView.setMsg("删除用户成功！");
        dataView.setCode(200);
        return dataView;
    }

    //初始化下拉列表的数据【班级】（回显数据）
    @RequestMapping("/listAllBanJi")
    @ResponseBody
    public List<BanJi> listAllBanJi(){
        List<BanJi> list = banJiService.list();
        return list;
    }

    //初始化下拉列表的数据【学院】（回显数据）
    @RequestMapping("/listAllXueYuan")
    @ResponseBody
    public List<XueYuan> listAllXueYuan(){
        List<XueYuan> list = xueYuanService.list();
        return list;
    }
    //重置密码功能实现
    @RequestMapping("/resetPwd/{id}")
    @ResponseBody
    public DataView resetPwd(@PathVariable("id") Integer id){
        User user = new User();
        user.setId(id);
        user.setPassword("123456");
        userService.updateById(user);
        DataView dataView = new DataView();
        dataView.setMsg("用户重置密码成功！");
        dataView.setCode(200);
        return dataView;
    }

    /*
    点击角色分配的时候，初始化用户的角色
    打开分配角色的弹出层
    根据id查询所拥有的角色
     */
    @RequestMapping("/initRoleByUserId")
    @ResponseBody
    public DataView initRoleByUserId(Integer id){
        //1.查询所有角色
        List<Map<String, Object>> listMaps = roleService.listMaps();
        //2.查询当前登录用户所拥有的角色
        List<Integer> currentUserRoleIds = roleService.queryUserRoleById(id);
        //3.让前端变为选中状态（之前拥有的角色为选中状态）
        for (Map<String,Object> map : listMaps){
            Boolean LAY_CHECKED = false;
            //之前类型为string，强转为Integer类型
            Integer roleId = (Integer) map.get("id");
            for (Integer rid : currentUserRoleIds){
                //如果rid与之前的roleId相等的话，即设置为选中状态
                if (rid.equals(roleId)){
                    LAY_CHECKED= true;
                    break;
                }
            }
            map.put("LAY_CHECKED",LAY_CHECKED);
        }
        return new DataView(Long.valueOf(listMaps.size()),listMaps);

    }
    /*
    保存用户与角色之间的关系 1：n
    ids为角色id
    先要删除之前保存的用户与角色之间的关系
     */
    @RequestMapping("/saveUserRole")
    @ResponseBody
    public DataView saveUserRole(Integer uid,Integer[]ids){
        userService.saveUserRole(uid,ids);
        DataView dataView = new DataView();
        dataView.setCode(200);
        dataView.setMsg("用户的角色分配成功！");
        return dataView;
    }


}
