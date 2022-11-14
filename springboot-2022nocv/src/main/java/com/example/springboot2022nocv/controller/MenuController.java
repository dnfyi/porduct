package com.example.springboot2022nocv.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot2022nocv.entity.Menu;
import com.example.springboot2022nocv.entity.User;
import com.example.springboot2022nocv.service.MenuService;
import com.example.springboot2022nocv.service.RoleService;
import com.example.springboot2022nocv.utils.TreeNode;
import com.example.springboot2022nocv.utils.TreeNodeBuilder;
import com.example.springboot2022nocv.vo.DataView;
import com.example.springboot2022nocv.vo.MenuVo;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @RequestMapping("/toMenu")
    public String toMenu(){
        return "menu/menu";

    }
    //加载所有的菜单
    @RequestMapping("/loadAllMenu")
    @ResponseBody
    public DataView loadAllMenu(MenuVo menuVo){
        IPage<Menu> page = new Page<>(menuVo.getPage(),menuVo.getLimit());
        //新建一个查询构造器
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        //带有分页模糊查询 先判断某字符串是否不为空且长度不为0且不由空白符(whitespace) 构成 从前台获取到title进行查询
        queryWrapper.like(!(menuVo.getTitle()==null),"title",menuVo.getTitle());
        queryWrapper.orderByAsc("ordernum");
        menuService.page(page,queryWrapper);
        DataView dataView = new  DataView(page.getTotal(),page.getRecords());
        return dataView;

    }
    //加载下拉菜单数据（编辑和新增菜单弹窗的父级菜单）和左侧数据dtree
    @RequestMapping("/loadMenuManagerLeftTreeJson")
    @ResponseBody
    public DataView loadMenuManagerLeftTreeJson(){
        //查询所有的菜单
        List<Menu> list = menuService.list();
        //使用有层级关系的菜单
        List<TreeNode> treeNodes = new ArrayList<>();
        for(Menu menu:list){
            Boolean open= menu.getOpen()==1?true:false;
            treeNodes.add(new TreeNode(menu.getId(), menu.getPid(), menu.getTitle(),open));
        }
        return new DataView(treeNodes);
    }

    /*添加菜单时自动添加上排序码(对应menuright.html的排序码接口）
    条件查询：倒叙排序，取一条数据，+1
     */
    @RequestMapping("/loadMenuMaxOrderNum")
    @ResponseBody
    public Map<String,Object> loadMenuMaxOrderNum(){
        Map<String,Object> map = new HashMap<>();
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("ordernum");
        IPage<Menu> page = new Page<>(1,1);
        List<Menu> list = menuService.page(page, queryWrapper).getRecords();
        map.put("value",list.get(0).getOrdernum()+1);
        return map;
    }
    //新增菜单
    @RequestMapping("/addMenu")
    @ResponseBody
    public DataView addMenu(Menu menu){
        DataView dataView = new DataView();
        menu.setType("menu");
        boolean save = menuService.save(menu);
        if(!save){
            dataView.setCode(100);
            dataView.setMsg("菜单插入失败！");
        }
        dataView.setMsg("菜单插入成功！");
        dataView.setCode(200);
        return dataView;

    }
    //更新菜单
    @RequestMapping("/updateMenu")
    @ResponseBody
    public DataView updateMenu(Menu menu){
        menuService.updateById(menu);
        DataView dataView = new DataView();
        dataView.setCode(200);
        dataView.setMsg("更新菜单成功！");
        return dataView;
    }
    /*
    判断有没有子类ID(只要pid≠0，都可以进行删除）
    没有子类id，可以删除
     */
    @RequestMapping("/checkMenuHasChildrenNode")
    @ResponseBody
    public Map<String,Object> checkChildrenNode(Menu menu){
        Map<String,Object> map = new HashMap<>();
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        //通过id来查询pid 如果pid=id的话，说明有数据，是父级菜单（看menu数据表）
        queryWrapper.eq("pid",menu.getId());
        List<Menu> list = menuService.list(queryWrapper);
        if (list.size()>0){
            //说明有孩子节点，不能进行删除
            map.put("value",true);
        }else {
            map.put("value",false);
        }
        return map;
    }
    //删除菜单逻辑
    @RequestMapping("/deleteMenu")
    @ResponseBody
    public DataView deleteMenu(Menu menu){
        menuService.removeById(menu.getId());
        DataView dataView = new DataView();
        dataView.setCode(200);
        dataView.setMsg("删除菜单成功！");
        return dataView;
    }
    //加载主页面index的菜单栏，带有层级关系
    //不用的用户的登录看到不同的菜单栏 带查询条件
    @RequestMapping("/loadIndexLeftMenuJson")
    @ResponseBody
    public DataView loadIndexLeftMenuJson(Menu menu, HttpSession session){
        //查询所有的菜单栏 按照条件进行查询【管理员（查询所有） 学生 老师（条件查询）】
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        List<Menu> list = null;
        //1.取出session中的用户id
        //1.1拿到当前的用户
        User user = (User) session.getAttribute("user");
        //1.2拿到当前用户的uid（根据数据库user_role可以知道当前用户的rid（角色）)
        Integer userId = user.getId();
        //2.根据用户Id查询角色id
        List<Integer> currentUserRoleIds = roleService.queryUserRoleById(userId);
        //3.去重
        Set<Integer> mids = new HashSet<>();
        for (Integer rid :currentUserRoleIds){
            //3.1.根据角色id查询菜单id
            List<Integer> permissionIds = roleService.queryAllPermissionByRid(rid);
            //3.2.菜单栏id和角色id去重
            mids.addAll(permissionIds);
        }
        //4.根据角色id查询菜单栏id
        if (mids.size()>0){
            queryWrapper.in("id",mids);
            list = menuService.list(queryWrapper);

        }

        //构造层级关系
        List<TreeNode> treeNodes = new ArrayList<>();
        for (Menu m:list){
            Integer id = m.getId();
            Integer pid = m.getPid();
            String title = m.getTitle();
            String icon = m.getIcon();
            String href = m.getHref();
            Boolean open = m.getOpen().equals(1)?true:false;
            treeNodes.add(new TreeNode(id,pid,title,icon,href,open));
        }
        //层次关系
        List<TreeNode> nodeList = TreeNodeBuilder.build(treeNodes, 0);
        return new DataView(nodeList);

    }



}
