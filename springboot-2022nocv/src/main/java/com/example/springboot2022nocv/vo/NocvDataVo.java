package com.example.springboot2022nocv.vo;

import com.example.springboot2022nocv.entity.NocvData;
import com.example.springboot2022nocv.service.IndexService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//分页数视图实现
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NocvDataVo extends NocvData {
    private Integer page;
    private Integer limit;

}
