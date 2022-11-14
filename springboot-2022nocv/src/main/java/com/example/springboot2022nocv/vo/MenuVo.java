package com.example.springboot2022nocv.vo;

import com.example.springboot2022nocv.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//带有分页
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuVo extends Menu {
    private Integer page;
    private Integer limit;
}
