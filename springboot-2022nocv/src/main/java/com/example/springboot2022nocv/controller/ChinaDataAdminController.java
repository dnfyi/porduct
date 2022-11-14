package com.example.springboot2022nocv.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot2022nocv.entity.NocvData;
import com.example.springboot2022nocv.service.IndexService;
import com.example.springboot2022nocv.vo.DataView;
import com.example.springboot2022nocv.vo.NocvDataVo;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ChinaDataAdminController {
    @Autowired
    private IndexService indexService;
    //跳转页面
    @RequestMapping("/toChinaManager")
    public String toChinaData(){
        return "admin/chinadatamanger";
    }

    //查询所有数据 模糊查询 带有分页（即是显示nocv_data数据库里面的所有数据）
    @RequestMapping("/listDataByPage")
    @ResponseBody
    public DataView listDataByPage(NocvDataVo nocvDataVo){
        //1.创建分页对象 当前页 每页限制大小
        IPage<NocvData> page = new Page<>(nocvDataVo.getPage(), nocvDataVo.getLimit());
        //2.创建模糊查询条件
        QueryWrapper<NocvData> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!(nocvDataVo.getName()==null),"name",nocvDataVo.getName());
        //3.疫情确诊数据最多的排在最上面
        queryWrapper.orderByDesc("value");
        //4.查询数据库
        indexService.page(page,queryWrapper);
        //5.返回数据封装
        DataView dataView = new DataView(page.getTotal(),page.getRecords());
        return dataView;

    }

    @RequestMapping("/china/deleteById")
    @ResponseBody
    //根据id进行删除
    public DataView deleteById(Integer id){
        indexService.removeById(id);
        DataView dataView = new DataView();
        dataView.setCode(200);
        dataView.setMsg("删除中国疫情数据成功！");
        return dataView;
    }

    //新增或者修改（修改需要根据id进行判断 id：nocvdata，有值进行修改，没有值进行新增）数据请求后台代码（中国疫情数据管理）
    @RequestMapping("/china/addOrUpdateChina")
    @ResponseBody
    public DataView addOrUpdateChina(NocvData nocvData){
        boolean save = indexService.saveOrUpdate(nocvData);
        DataView dataView = new DataView();
        if (save){
            dataView.setCode(200);
            dataView.setMsg("新增中国疫情数据成功！");
            return dataView;
        }
        dataView.setCode(100);
        dataView.setMsg("新增中国疫情数据失败！");
        return dataView;
    }
    /**excel的拖拽或者点击上传文件
     * 1.前台页面发送一个请求，上传文件mutilpartFile HTTP
     * 2.Controller 上传文件mutipartFile 作为参数
     * 3.POI 解析文件，里面数据一行一行全部解析出来
     * 4.每一条数据插入数据库
     */
    @RequestMapping("/excelImportChina")
    @ResponseBody
    public DataView excelImportChina(@RequestParam("file") MultipartFile file) throws Exception {
        DataView dataView = new DataView();
        //1.文件不能为空
        if (file.isEmpty()){
            dataView.setMsg("文件为空，不能上传！");
        }

        //2.POI获取excel解析数据
        XSSFWorkbook wb = new XSSFWorkbook(file.getInputStream());//创建工作簿
        XSSFSheet sheet = wb.getSheetAt(0);//创建工作表

        //3.定义一个程序集合 接收文件中的数据
        List<NocvData> list = new ArrayList<>();
        XSSFRow row = null;//创建行

        //4.解析数据，装到集合里面
        for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
            //4.1定义实体
            NocvData nocvData = new NocvData();
            //4.2遍历每一行数据 放到实体类里面
            row = sheet.getRow(i);
            //4.3 解析数据
            nocvData.setName(row.getCell(0).getStringCellValue());//获取表头名字
            nocvData.setValue((int) row.getCell(1).getNumericCellValue());//获取表中的数据
            //5.添加list集合
            list.add(nocvData);
        }
        //6.插入数据库
        indexService.saveBatch(list);
        dataView.setCode(200);
        dataView.setMsg("excel文件已经插入成功！");
        return dataView;
    }

    //导出Excel数据 中国疫情数据
    /*
    1.查询数据库
    2.建立Excel对象 封装数据
    3.建立输出流，输出文件
     */
    @RequestMapping("/excelOutPortChina")
    @ResponseBody
    public void excelOutPortChina(HttpServletResponse response){
        //1.查询数据库（查询所有符合条件的数据）
        List<NocvData> list = indexService.list();
        //2.建立Excel对象 封装数据
        response.setCharacterEncoding("UTF-8");
        //2.1创建Excel对象
        HSSFWorkbook wb = new HSSFWorkbook();
        //2.2 创建sheet对象
        HSSFSheet sheet = wb.createSheet("中国疫情数据sheet1");
        //2.3创建表头名字
        HSSFRow hssfRow = sheet.createRow(0);
        hssfRow.createCell(0).setCellValue("城市名称");
        hssfRow.createCell(1).setCellValue("确诊数量");
        //3.遍历数据，封装Excel对象
        for (NocvData data : list) {
            HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
            dataRow.createCell(0).setCellValue(data.getName());//创建单元格，获取城市名字
            dataRow.createCell(1).setCellValue(data.getValue());//创建单元格，获取对应的取值
        }
        //4.建立输出流 输出浏览器文件
        OutputStream os = null;
        //4.1.设置Excel的名称 输出类型编码
        try {
            response.setContentType("application/octet-stream;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String("中国疫情数据表".getBytes(), "iso-8859-1") + ".xls");
            //4.2用输出流写到excel
            os = response.getOutputStream();
            wb.write(os);
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
