package com.example.springboot2022nocv.vo;

import com.example.springboot2022nocv.entity.NocvData;
import com.example.springboot2022nocv.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleVo extends Role {
    private Integer page;
    private Integer limit;
}
