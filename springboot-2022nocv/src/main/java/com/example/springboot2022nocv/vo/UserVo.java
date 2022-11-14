package com.example.springboot2022nocv.vo;

import com.example.springboot2022nocv.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo extends User {
    private Integer page;
    private Integer limit;
}
