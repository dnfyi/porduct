package com.example.springboot2022nocv.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot2022nocv.dao.RoleMapper;
import com.example.springboot2022nocv.entity.Role;
import com.example.springboot2022nocv.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public List<Integer> queryAllPermissionByRid(Integer roleId) {
        return roleMapper.queryMidByRid(roleId);
    }

    @Override
    public void deleteRoleByRid(Integer rid) {
        roleMapper.deleteRoleById(rid);
    }

    @Override
    public void saveRoleMenu(Integer rid, Integer mid) {
        roleMapper.saveRoleMenu(rid,mid);

    }

    @Override
    public List<Integer> queryUserRoleById(Integer id) {
        return roleMapper.queryUserRoleById(id);
    }
}
