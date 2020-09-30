package business.controller;

import business.common.api.vo.Result;
import business.jwt.LoginIgnore;
import business.service.IAuthUserService;
import business.service.IPersonnelSalaryService;
import business.service.IPersonnelWelfareService;
import business.util.FileUtils;
import business.vo.PersonnelSalaryVO;
import business.vo.excel.DepartMonthVO;
import business.vo.excel.ExcelExportStyle;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("salaryReport")
@Slf4j
public class SalaryReportController {
    @Resource
    IPersonnelSalaryService iPersonnelSalaryService;
    @Resource
    IPersonnelWelfareService iPersonnelWelfareService;
    @Resource
    IAuthUserService iAuthUserService;

    /**
     * 统计报表
     *
     * @param year
     * @return
     */
    @RequestMapping(value = "/getMonthlyLaborCost", method = RequestMethod.POST)
    public Result<?> getMonthlyLaborCost(@RequestParam("year") String year, @RequestParam("rate") Float rate) {

        return Result.ok(iPersonnelSalaryService.getMonthlyLaborCost(year, rate));
    }


    @LoginIgnore
    @GetMapping(value = "/exportMonthlyLaborCost")
    public void exportMonthlyLaborCost(HttpServletResponse response, @RequestParam("year") String year, @RequestParam("rate") Float rate) {
        List<Map<String, Object>> result = iPersonnelSalaryService.getMonthlyLaborCost(year, rate);
        List<ExcelExportEntity> entityList = new ArrayList<>();
        entityList.add(new ExcelExportEntity("用户ID", "id", 15));
        entityList.add(new ExcelExportEntity("用户名", "name", 15));
        entityList.add(new ExcelExportEntity("用户年龄", "age", 15));
        FileUtils.exportExcel(result, "人工成本.xls", response);
    }


    /**
     * 直接导出(无需模板)
     * 注:此方式存在一些不足之处，在对性能、excel要求比较严格时不推荐使用
     *
     * @author JustryDeng
     * @date 2018/12/5 11:44
     */
    @LoginIgnore
    @GetMapping(value = "/directExportExcel")
    public void directExportExcel(HttpServletResponse response, @RequestParam("year") String year, @RequestParam("rate") Float rate) {
        List<Map<String, Object>> resultData = iPersonnelSalaryService.getMonthlyLaborCost(year, rate);
        List<Map<String, Object>> rowDataList = new ArrayList<>();
        // Map作为每一行的数据容器，List作为行的容器
        Map<String, Object> aRowMap = new HashMap<>();
        for (int i = 0; i < resultData.size(); i++) {
            // 同一列对应的cell,在从Map里面取值时，会共用同一个key
            // 因此ExcelExportEntity的个数要保持和列数做多的行 的map.size()大小一致

            for (String key : resultData.get(i).keySet()) {
                aRowMap.put(key, resultData.get(i).get(key));
            }
            rowDataList.add(aRowMap);
        }
        // excel总体设置
        ExportParams exportParams = new ExportParams();
        // 不需要标题
        exportParams.setCreateHeadRows(true);
        //表头设置
        List<ExcelExportEntity> colList = new ArrayList<>();
        ExcelExportEntity colEntity = new ExcelExportEntity("月份", "REMARK");
        colEntity.setWidth(15D);
        colList.add(colEntity);
        colEntity = new ExcelExportEntity("人数", "HN");
        colEntity.setWidth(15D);
        colList.add(colEntity);
        colEntity = new ExcelExportEntity("应发工资", "GP");
        colEntity.setWidth(15D);
        colList.add(colEntity);
        colEntity = new ExcelExportEntity("福利费", "IAF");
        colEntity.setWidth(15D);
        colList.add(colEntity);
        colEntity = new ExcelExportEntity("保险公积金", "WAS");
        colEntity.setWidth(15D);
        colList.add(colEntity);
        colEntity = new ExcelExportEntity("13、14月工资", "WAB");
        colEntity.setWidth(15D);
        colList.add(colEntity);
        colEntity = new ExcelExportEntity("年终奖", "WAW");
        colEntity.setWidth(15D);
        colList.add(colEntity);
        colEntity = new ExcelExportEntity("合计", "TOTAL");
        colEntity.setWidth(15D);
        colList.add(colEntity);
        // 指定sheet名字
        exportParams.setSheetName("每月人工成本");
        // 生成workbook 并导出
        try {
            Workbook workbook = ExcelExportUtil.exportExcel(exportParams, colList, resultData);
            CellStyle titleStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            titleStyle.setFont(font);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("每月人工成本.xls", "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 直接导出(无需模板)
     * 注:此方式存在一些不足之处，在对性能、excel要求比较严格时不推荐使用
     * @author JustryDeng
     * @date 2018/12/5 11:44
     */
    @LoginIgnore
    @GetMapping(value = "/departExportExcel")
    public void departExportExcel(HttpServletResponse response, @RequestParam("year") String year, @RequestParam("rate") Float rate) {
        //与简单导出一致
        List<DepartMonthVO> listTemplates = new ArrayList<>();
        for(int i = 0;i<10;i++){
            DepartMonthVO departMonthVO = new DepartMonthVO();
            departMonthVO.setDepartMentName("部门"+i);
            departMonthVO.setBx1(new BigDecimal(i));
            departMonthVO.setOther1(new BigDecimal(i));
            departMonthVO.setFl1(new BigDecimal(i));
            departMonthVO.setPeople1(new BigDecimal(i));
            departMonthVO.setSalary1(new BigDecimal(i));
            departMonthVO.setTotal1(new BigDecimal(i));
            departMonthVO.setYear1(new BigDecimal(i));

            departMonthVO.setBx2(new BigDecimal(i));
            departMonthVO.setOther2(new BigDecimal(i));
            departMonthVO.setFl2(new BigDecimal(i));
            departMonthVO.setPeople2(new BigDecimal(i));
            departMonthVO.setSalary2(new BigDecimal(i));
            departMonthVO.setTotal2(new BigDecimal(i));
            departMonthVO.setYear2(new BigDecimal(i));
            listTemplates.add(departMonthVO);
        }
        //导出
        // excel总体设置
        ExportParams exportParams = new ExportParams();
        exportParams.setSheetName("sheet1");
        exportParams.setStyle(ExcelExportStyle.class);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, DepartMonthVO.class, listTemplates);
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("部门每月人工成本.xls", "UTF-8"));
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}