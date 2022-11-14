package com.example.springboot2022nocv.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot2022nocv.entity.Role;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {
    @Select("select mid from role_menu where rid = #{roleId}")
    List<Integer> queryMidByRid(Integer roleId);
    //1.分配菜单栏之前删除所有的rid数据，只有有数据就进行删除
    @Delete("delete from role_menu where rid = #{rid}")
    void deleteRoleById(Integer rid);
    //2.保存分配（其实就是角色与菜单的关系）
    @Insert("insert into role_menu(rid,mid) values (#{rid},#{mid})")
    void saveRoleMenu(Integer rid, Integer mid);

    //根据用户id查询所有的角色
    @Select("select rid from user_role where uid = #{id}")
    List<Integer> queryUserRoleById(Integer id);

    //1.先删除之前的用户与角色之间的关系
    @Delete("delete from user_role where uid = #{uid} ")
    void deleteRoleUserByUid(Integer uid);

    //2.保存分配的用户与角色之间的关系
    @Insert("insert into user_role(uid,rid) values(#{uid},#{rid})")
    void saveUserRole(Integer uid, Integer rid);
}
