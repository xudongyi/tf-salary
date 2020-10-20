package business.service.impl;

import business.bean.HrmResource;
import business.bean.ManufacturingDeptConfig;
import business.bean.PersonnelSalary;
import business.mapper.HrmResourceMapper;
import business.mapper.OperateLogMapper;
import business.mapper.PersonnelSalaryMapper;
import business.mapper.PersonnelWelfareMapper;
import business.service.IPersonnelSalaryService;
import business.vo.PersonnelSalaryVO;
import business.vo.PersonnelWelfareVO;
import business.vo.excel.ExcelDepartMonthDept;
import business.vo.excel.ExcelDepartMonthDeptDetail;
import business.vo.excel.ExcelDepartMonthVo;
import business.vo.excel.MonthlyLaborCostByDeptVo;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Slf4j
public class PersonnelSalaryServiceImpl extends ServiceImpl<PersonnelSalaryMapper, PersonnelSalary> implements IPersonnelSalaryService {

    @Resource
    private PersonnelSalaryMapper personnelSalaryMapper;
    @Resource
    private OperateLogMapper operateLogMapper;
    @Resource
    private PersonnelWelfareMapper personnelWelfareMapper;
    @Resource
    private ManufacturingDeptConfig manufacturingDeptConfig;

    @Override
    public IPage<PersonnelSalaryVO> getPersonnelSalaryList(PersonnelSalaryVO personnelSalaryVo, Integer pageNo,Integer pageSize) {
        IPage<PersonnelSalaryVO> page = new Page<PersonnelSalaryVO>(pageNo, pageSize);
        QueryWrapper<PersonnelSalaryVO> sqlaryQueryWrapper = new QueryWrapper<>();
        //sqlaryQueryWrapper.lambda().orderByDesc(PersonnelSalary::getId);
        if (StringUtils.isNotBlank(personnelSalaryVo.getWorkcode())) {
            sqlaryQueryWrapper.eq("t1.workcode", personnelSalaryVo.getWorkcode());
        }
        if(StringUtils.isNotBlank(personnelSalaryVo.getDept())){
            sqlaryQueryWrapper.eq("t2.depart_code", personnelSalaryVo.getDept().split("_")[0]);
        }
        if(StringUtils.isNotBlank(personnelSalaryVo.getSalarystamonth())){
            sqlaryQueryWrapper.ge("t1.salary_date", personnelSalaryVo.getSalarystamonth());
        }
        if(StringUtils.isNotBlank(personnelSalaryVo.getSalaryendmonth())){
            sqlaryQueryWrapper.le("t1.salary_date", personnelSalaryVo.getSalaryendmonth());
        }
        return personnelSalaryMapper.getPersonnelSalary(page, sqlaryQueryWrapper);
    }

    @Override
    public List<PersonnelSalaryVO> getPersonnelSalaryList(Wrapper<PersonnelSalaryVO> queryWrapper) {
        return personnelSalaryMapper.getPersonnelSalary(queryWrapper);
    }


    @Override
    public Map<String, Object> getReportHeader() {
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String currentMonth = sdf.format(currentDate);
        String lastMonthStr = getMonth("",-1);
        String currentMonthSalary = personnelSalaryMapper.getSalaryByMonth(currentMonth).get("NET_SALARY").toString();
        String lastMonthSalary = personnelSalaryMapper.getSalaryByMonth(lastMonthStr).get("NET_SALARY").toString();
        String currentMonthImportNumber = personnelSalaryMapper.getImportNumberByMonth(currentMonth).get("IMPORT_NUMBER").toString();
        String lastMonthImportNumber = personnelSalaryMapper.getImportNumberByMonth(lastMonthStr).get("IMPORT_NUMBER").toString();
        String currentMonthVisitTimes = operateLogMapper.getVisitTimesByMonth(currentMonth).get("VISIT_TIMES").toString();
        String lastMonthVisitTimes = operateLogMapper.getVisitTimesByMonth(lastMonthStr).get("VISIT_TIMES").toString();
        String currentMonthNoteNumber = operateLogMapper.getNoteNumberByMonth(currentMonth).get("NOTE_NUMBER").toString();
        String lastMonthNoteNumber = operateLogMapper.getNoteNumberByMonth(lastMonthStr).get("NOTE_NUMBER").toString();
        List<Map<String, Object>> salaryDepartmentRankList = personnelSalaryMapper.getSalaryRankByDepartment();
        List<Map<String, Object>> noteTimesDepartmentRankList = operateLogMapper.getNoteTimesRankByDepartment();
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("currentMonthSalary", currentMonthSalary);
        result.put("lastMonthSalary", lastMonthSalary);
        result.put("currentMonthImportNumber", currentMonthImportNumber);
        result.put("lastMonthImportNumber", lastMonthImportNumber);
        result.put("currentMonthVisitTimes", currentMonthVisitTimes);
        result.put("lastMonthVisitTimes", lastMonthVisitTimes);
        result.put("currentMonthNoteNumber", currentMonthNoteNumber);
        result.put("lastMonthNoteNumber", lastMonthNoteNumber);
        result.put("salaryDepartmentRankList", salaryDepartmentRankList);
        result.put("noteTimesDepartmentRankList", noteTimesDepartmentRankList);
        return result;
    }

    @Override
    public Map<String, Object> getReportBodyList(String staDate, String endDate) {
        if ((staDate.equals("") || staDate == null) && (endDate.equals("") || staDate == null)) {
            Date currentDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            staDate = getMonth("", -11);
            endDate = sdf.format(currentDate);
        } else if ((staDate.equals("") || staDate == null) && (!endDate.equals("") && staDate != null)) {
            staDate = getMonth(endDate, -11);
        } else if ((!staDate.equals("") && staDate != null) && (endDate.equals("") || staDate == null)) {
            endDate = getMonth(staDate, 11);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        List<Map<String, Object>> salaryList = personnelSalaryMapper.getSalaryBetweenMonth(staDate,endDate);
        List<Map<String, Object>> noteTimesList = operateLogMapper.getNoteTimesBetweenMonth(staDate,endDate);
        result.put("salaryList",salaryList);
        result.put("noteTimesList",noteTimesList);
        return result;
    }

    @Override
    public List<Map<String, Object>> getMonthlyLaborCost(String year, Float rate) {
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        for(int i=1;i<=12;i++){
                String month = year+"-"+(String.valueOf(i).length()==1?"0"+String.valueOf(i):String.valueOf(i));
            Map<String,Object> salaryMonthDataMap = personnelSalaryMapper.getMonthlyLaborCost(month);
            Map<String,Object> welfareMonthDateMap = personnelWelfareMapper.getMonthlyLaborCost(month);
            Map<String,Object> monthResultMap = new LinkedHashMap<String,Object>();
            monthResultMap.put("REMARK",i+"月");
            BigDecimal eachMonthTotal = new BigDecimal("0");
            if(salaryMonthDataMap!=null&&Integer.parseInt(salaryMonthDataMap.get("HRM_NUMBER").toString())!=0){
                monthResultMap.put("HN",salaryMonthDataMap.get("HRM_NUMBER"));
                monthResultMap.put("GP",salaryMonthDataMap.get("GROSS_PAY"));  //应发工资
                eachMonthTotal = eachMonthTotal.add(new BigDecimal(monthResultMap.get("GP").toString()));
                monthResultMap.put("IAF",new BigDecimal(salaryMonthDataMap.get("UNEMPLOY_INSURANCE").toString()).multiply(new BigDecimal(rate)).add(new BigDecimal(salaryMonthDataMap.get("HOUSEPOVIDENT_FUND").toString())).setScale(2,BigDecimal.ROUND_DOWN));  //失保
                eachMonthTotal = eachMonthTotal.add(new BigDecimal(monthResultMap.get("IAF").toString()));
            }else{
                monthResultMap.put("HN",0);
                monthResultMap.put("GP",0);
                monthResultMap.put("IAF",0);
            }
            if(welfareMonthDateMap!=null){
                monthResultMap.put("WAS",welfareMonthDateMap.get("WELFARE_AMOUNT_SALARIES"));
                eachMonthTotal = eachMonthTotal.add(new BigDecimal(monthResultMap.get("WAS").toString()));
                monthResultMap.put("WAB",welfareMonthDateMap.get("WELFARE_AMOUNT_BONUS"));
                eachMonthTotal = eachMonthTotal.add(new BigDecimal(monthResultMap.get("WAB").toString()));
                monthResultMap.put("WAW",welfareMonthDateMap.get("WELFARE_AMOUNT_WEAL"));
                eachMonthTotal = eachMonthTotal.add(new BigDecimal(monthResultMap.get("WAW").toString()));
            }else{
                monthResultMap.put("WAS",0);
                monthResultMap.put("WAB",0);
                monthResultMap.put("WAW",0);
            }
            monthResultMap.put("TOTAL",eachMonthTotal);
            resultList.add(monthResultMap);
        }

        Map<String,Object> salaryYearDataMap = personnelSalaryMapper.getYearlyLaborCost(year);
        Map<String,Object> welfareYearDateMap = personnelWelfareMapper.getYearlyLaborCost(year);
        BigDecimal eachYearTotal = new BigDecimal("0");
        Map<String,Object> yearResultMap = new LinkedHashMap<>();
        yearResultMap.put("REMARK","合计");
        if(salaryYearDataMap!=null&&Integer.parseInt(salaryYearDataMap.get("HRM_NUMBER").toString())!=0){
            yearResultMap.put("HN",salaryYearDataMap.get("HRM_NUMBER"));
            yearResultMap.put("GP",salaryYearDataMap.get("GROSS_PAY"));  //应发工资
            eachYearTotal = eachYearTotal.add(new BigDecimal(yearResultMap.get("GP").toString()));
            yearResultMap.put("IAF",new BigDecimal(salaryYearDataMap.get("UNEMPLOY_INSURANCE").toString()).multiply(new BigDecimal(rate)).add(new BigDecimal(salaryYearDataMap.get("HOUSEPOVIDENT_FUND").toString())).setScale(2,BigDecimal.ROUND_DOWN));  //失保
            eachYearTotal = eachYearTotal.add(new BigDecimal(yearResultMap.get("IAF").toString()));
        }else{
            yearResultMap.put("HN",0);
            yearResultMap.put("GP",0);
            yearResultMap.put("IAF",0);
        }
        if(welfareYearDateMap!=null){
            yearResultMap.put("WAS",welfareYearDateMap.get("WELFARE_AMOUNT_SALARIES"));
            eachYearTotal = eachYearTotal.add(new BigDecimal(yearResultMap.get("WAS").toString()));
            yearResultMap.put("WAB",welfareYearDateMap.get("WELFARE_AMOUNT_BONUS"));
            eachYearTotal = eachYearTotal.add(new BigDecimal(yearResultMap.get("WAB").toString()));
            yearResultMap.put("WAW",welfareYearDateMap.get("WELFARE_AMOUNT_WEAL"));
            eachYearTotal = eachYearTotal.add(new BigDecimal(yearResultMap.get("WAW").toString()));
        }else{
            yearResultMap.put("WAS",0);
            yearResultMap.put("WAB",0);
            yearResultMap.put("WAW",0);
        }
        yearResultMap.put("TOTAL",eachYearTotal);
        resultList.add(yearResultMap);
        return resultList;
    }

    @Override
    public IPage<MonthlyLaborCostByDeptVo> getMonthlyLaborCostByDept(String year, Float rate, Integer pageNo, Integer pageSize) {
        IPage page = new Page(pageNo, pageSize);
        return personnelSalaryMapper.getMonthlyLaborCostByDept(page,year,rate);
    }

    @Override
    public List<MonthlyLaborCostByDeptVo> getMonthlyLaborCostByDept(String year, Float rate) {
        return personnelSalaryMapper.getMonthlyLaborCostByDept(year,rate);
    }

    @Override
    public List<ExcelDepartMonthVo> getMonthlyLaborCostByManufacturingDept(String year, Float rate) {
        List<ExcelDepartMonthDeptDetail> queryList = personnelSalaryMapper.getMonthlyLaborCostByDeptCode(year, rate,
                manufacturingDeptConfig.getDeptA1AssemblyCodes(),
                manufacturingDeptConfig.getDeptA1TestingCodes(),
                manufacturingDeptConfig.getDeptD2AssemblyCodes(),
                manufacturingDeptConfig.getDeptD2TestingCodes(),
                manufacturingDeptConfig.getDeptA3AssemblyCodes(),
                manufacturingDeptConfig.getDeptA3TestingCodes(),
                manufacturingDeptConfig.getPackagingCodes(),
                manufacturingDeptConfig.getSurfaceCodes());

        List<ExcelDepartMonthVo> resultList = new ArrayList<ExcelDepartMonthVo>();

        ExcelDepartMonthVo excelDepartMonthVoHalfYear = new ExcelDepartMonthVo(); //1-6月数据汇总
        List<ExcelDepartMonthDept> excelDepartMonthDeptsHalfYear = new ArrayList<ExcelDepartMonthDept>();
        ExcelDepartMonthDept excelDepartMonthDeptA1HalfYear = new ExcelDepartMonthDept();
        ExcelDepartMonthDept excelDepartMonthDeptD2HalfYear = new ExcelDepartMonthDept();
        ExcelDepartMonthDept excelDepartMonthDeptA3HalfYear = new ExcelDepartMonthDept();
        ExcelDepartMonthDept excelDepartMonthDeptPKHalfYear = new ExcelDepartMonthDept();
        ExcelDepartMonthDept excelDepartMonthDeptSFHalfYear = new ExcelDepartMonthDept();
        excelDepartMonthDeptA1HalfYear.setDeptName("A1");
        excelDepartMonthDeptD2HalfYear.setDeptName("D2");
        excelDepartMonthDeptA3HalfYear.setDeptName("A3");
        excelDepartMonthDeptPKHalfYear.setDeptName("圆片级封装部");
        excelDepartMonthDeptSFHalfYear.setDeptName("表面处理中心");
        List<ExcelDepartMonthDeptDetail> excelDepartMonthDeptDetailsA1HalfYear = new ArrayList<ExcelDepartMonthDeptDetail>();
        List<ExcelDepartMonthDeptDetail> excelDepartMonthDeptDetailsD2HalfYear = new ArrayList<ExcelDepartMonthDeptDetail>();
        List<ExcelDepartMonthDeptDetail> excelDepartMonthDeptDetailsA3HalfYear = new ArrayList<ExcelDepartMonthDeptDetail>();
        List<ExcelDepartMonthDeptDetail> excelDepartMonthDeptDetailsPKHalfYear = new ArrayList<ExcelDepartMonthDeptDetail>();
        List<ExcelDepartMonthDeptDetail> excelDepartMonthDeptDetailsSFHalfYear = new ArrayList<ExcelDepartMonthDeptDetail>();
        ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailA1AssemblyHalfYear = new ExcelDepartMonthDeptDetail();
        ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailA1testingHalfYear = new ExcelDepartMonthDeptDetail();
        ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailA1TotalHalfYear = new ExcelDepartMonthDeptDetail();
        ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailD2AssemblyHalfYear = new ExcelDepartMonthDeptDetail();
        ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailD2testingHalfYear = new ExcelDepartMonthDeptDetail();
        ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailD2TotalHalfYear = new ExcelDepartMonthDeptDetail();
        ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailA3AssemblyHalfYear = new ExcelDepartMonthDeptDetail();
        ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailA3TestingHalfYear = new ExcelDepartMonthDeptDetail();
        ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailA3TotalHalfYear = new ExcelDepartMonthDeptDetail();
        ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailPackingHalfYear = new ExcelDepartMonthDeptDetail();
        ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailSurfaceHalfYear = new ExcelDepartMonthDeptDetail();
        excelDepartMonthDeptDetailA1AssemblyHalfYear.setSalaryDate("1-6月");
        excelDepartMonthDeptDetailA1AssemblyHalfYear.setDepartCode(manufacturingDeptConfig.getDeptA1AssemblyCodes());
        excelDepartMonthDeptDetailA1AssemblyHalfYear.setDepartName("组装一期");
        excelDepartMonthDeptDetailA1testingHalfYear.setSalaryDate("1-6月");
        excelDepartMonthDeptDetailA1testingHalfYear.setDepartCode(manufacturingDeptConfig.getDeptA1AssemblyCodes());
        excelDepartMonthDeptDetailA1testingHalfYear.setDepartName("测试一期");
        excelDepartMonthDeptDetailA1TotalHalfYear.setSalaryDate("1-6月");
        excelDepartMonthDeptDetailA1TotalHalfYear.setDepartCode(manufacturingDeptConfig.getDeptA1AssemblyCodes());
        excelDepartMonthDeptDetailA1TotalHalfYear.setDepartName("合计");
        excelDepartMonthDeptDetailD2AssemblyHalfYear.setSalaryDate("1-6月");
        excelDepartMonthDeptDetailD2AssemblyHalfYear.setDepartCode(manufacturingDeptConfig.getDeptA1AssemblyCodes());
        excelDepartMonthDeptDetailD2AssemblyHalfYear.setDepartName("组装一期");
        excelDepartMonthDeptDetailD2testingHalfYear.setSalaryDate("1-6月");
        excelDepartMonthDeptDetailD2testingHalfYear.setDepartCode(manufacturingDeptConfig.getDeptA1AssemblyCodes());
        excelDepartMonthDeptDetailD2testingHalfYear.setDepartName("测试二期");
        excelDepartMonthDeptDetailD2TotalHalfYear.setSalaryDate("1-6月");
        excelDepartMonthDeptDetailD2TotalHalfYear.setDepartCode(manufacturingDeptConfig.getDeptA1AssemblyCodes());
        excelDepartMonthDeptDetailD2TotalHalfYear.setDepartName("合计");
        excelDepartMonthDeptDetailA3AssemblyHalfYear.setSalaryDate("1-6月");
        excelDepartMonthDeptDetailA3AssemblyHalfYear.setDepartCode(manufacturingDeptConfig.getDeptA1AssemblyCodes());
        excelDepartMonthDeptDetailA3AssemblyHalfYear.setDepartName("组装三期");
        excelDepartMonthDeptDetailA3TestingHalfYear.setSalaryDate("1-6月");
        excelDepartMonthDeptDetailA3TestingHalfYear.setDepartCode(manufacturingDeptConfig.getDeptA1AssemblyCodes());
        excelDepartMonthDeptDetailA3TestingHalfYear.setDepartName("测试三期");
        excelDepartMonthDeptDetailA3TotalHalfYear.setSalaryDate("1-6月");
        excelDepartMonthDeptDetailA3TotalHalfYear.setDepartCode(manufacturingDeptConfig.getDeptA1AssemblyCodes());
        excelDepartMonthDeptDetailA3TotalHalfYear.setDepartName("合计");
        excelDepartMonthDeptDetailPackingHalfYear.setSalaryDate("1-6月");
        excelDepartMonthDeptDetailPackingHalfYear.setDepartCode(manufacturingDeptConfig.getDeptA1AssemblyCodes());
        excelDepartMonthDeptDetailPackingHalfYear.setDepartName("圆片级封装部");
        excelDepartMonthDeptDetailSurfaceHalfYear.setSalaryDate("1-6月");
        excelDepartMonthDeptDetailSurfaceHalfYear.setDepartCode(manufacturingDeptConfig.getDeptA1AssemblyCodes());
        excelDepartMonthDeptDetailSurfaceHalfYear.setDepartName("表面处理中心");

        ExcelDepartMonthVo excelDepartMonthVoWholeYear = new ExcelDepartMonthVo(); //1-12月数据汇总
        List<ExcelDepartMonthDept> excelDepartMonthDeptsWholeYear = new ArrayList<ExcelDepartMonthDept>();
        ExcelDepartMonthDept excelDepartMonthDeptA1WholeYear = new ExcelDepartMonthDept();
        ExcelDepartMonthDept excelDepartMonthDeptD2WholeYear = new ExcelDepartMonthDept();
        ExcelDepartMonthDept excelDepartMonthDeptA3WholeYear = new ExcelDepartMonthDept();
        ExcelDepartMonthDept excelDepartMonthDeptPKWholeYear = new ExcelDepartMonthDept();
        ExcelDepartMonthDept excelDepartMonthDeptSFWholeYear = new ExcelDepartMonthDept();
        excelDepartMonthDeptA1WholeYear.setDeptName("A1");
        excelDepartMonthDeptD2WholeYear.setDeptName("D2");
        excelDepartMonthDeptA3WholeYear.setDeptName("A3");
        excelDepartMonthDeptPKWholeYear.setDeptName("圆片级封装部");
        excelDepartMonthDeptSFWholeYear.setDeptName("表面处理中心");
        List<ExcelDepartMonthDeptDetail> excelDepartMonthDeptDetailsA1WholeYear = new ArrayList<ExcelDepartMonthDeptDetail>();
        List<ExcelDepartMonthDeptDetail> excelDepartMonthDeptDetailsD2WholeYear = new ArrayList<ExcelDepartMonthDeptDetail>();
        List<ExcelDepartMonthDeptDetail> excelDepartMonthDeptDetailsA3WholeYear = new ArrayList<ExcelDepartMonthDeptDetail>();
        List<ExcelDepartMonthDeptDetail> excelDepartMonthDeptDetailsPKWholeYear = new ArrayList<ExcelDepartMonthDeptDetail>();
        List<ExcelDepartMonthDeptDetail> excelDepartMonthDeptDetailsSFWholeYear = new ArrayList<ExcelDepartMonthDeptDetail>();
        ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailA1AssemblyWholeYear = new ExcelDepartMonthDeptDetail();
        ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailA1testingWholeYear = new ExcelDepartMonthDeptDetail();
        ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailA1TotalWholeYear = new ExcelDepartMonthDeptDetail();
        ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailD2AssemblyWholeYear = new ExcelDepartMonthDeptDetail();
        ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailD2testingWholeYear = new ExcelDepartMonthDeptDetail();
        ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailD2TotalWholeYear = new ExcelDepartMonthDeptDetail();
        ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailA3AssemblyWholeYear = new ExcelDepartMonthDeptDetail();
        ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailA3TestingWholeYear = new ExcelDepartMonthDeptDetail();
        ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailA3TotalWholeYear = new ExcelDepartMonthDeptDetail();
        ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailPackingWholeYear = new ExcelDepartMonthDeptDetail();
        ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailSurfaceWholeYear = new ExcelDepartMonthDeptDetail();
        excelDepartMonthDeptDetailA1AssemblyWholeYear.setSalaryDate("1-12月");
        excelDepartMonthDeptDetailA1AssemblyWholeYear.setDepartCode(manufacturingDeptConfig.getDeptA1AssemblyCodes());
        excelDepartMonthDeptDetailA1AssemblyWholeYear.setDepartName("组装一期");
        excelDepartMonthDeptDetailA1testingWholeYear.setSalaryDate("1-12月");
        excelDepartMonthDeptDetailA1testingWholeYear.setDepartCode(manufacturingDeptConfig.getDeptA1AssemblyCodes());
        excelDepartMonthDeptDetailA1testingWholeYear.setDepartName("测试一期");
        excelDepartMonthDeptDetailA1TotalWholeYear.setSalaryDate("1-12月");
        excelDepartMonthDeptDetailA1TotalWholeYear.setDepartCode(manufacturingDeptConfig.getDeptA1AssemblyCodes());
        excelDepartMonthDeptDetailA1TotalWholeYear.setDepartName("合计");
        excelDepartMonthDeptDetailD2AssemblyWholeYear.setSalaryDate("1-12月");
        excelDepartMonthDeptDetailD2AssemblyWholeYear.setDepartCode(manufacturingDeptConfig.getDeptA1AssemblyCodes());
        excelDepartMonthDeptDetailD2AssemblyWholeYear.setDepartName("组装二期");
        excelDepartMonthDeptDetailD2testingWholeYear.setSalaryDate("1-12月");
        excelDepartMonthDeptDetailD2testingWholeYear.setDepartCode(manufacturingDeptConfig.getDeptA1AssemblyCodes());
        excelDepartMonthDeptDetailD2testingWholeYear.setDepartName("测试二期");
        excelDepartMonthDeptDetailD2TotalWholeYear.setSalaryDate("1-12月");
        excelDepartMonthDeptDetailD2TotalWholeYear.setDepartCode(manufacturingDeptConfig.getDeptA1AssemblyCodes());
        excelDepartMonthDeptDetailD2TotalWholeYear.setDepartName("合计");
        excelDepartMonthDeptDetailA3AssemblyWholeYear.setSalaryDate("1-12月");
        excelDepartMonthDeptDetailA3AssemblyWholeYear.setDepartCode(manufacturingDeptConfig.getDeptA1AssemblyCodes());
        excelDepartMonthDeptDetailA3AssemblyWholeYear.setDepartName("组装三期");
        excelDepartMonthDeptDetailA3TestingWholeYear.setSalaryDate("1-12月");
        excelDepartMonthDeptDetailA3TestingWholeYear.setDepartCode(manufacturingDeptConfig.getDeptA1AssemblyCodes());
        excelDepartMonthDeptDetailA3TestingWholeYear.setDepartName("测试三期");
        excelDepartMonthDeptDetailA3TotalWholeYear.setSalaryDate("1-12月");
        excelDepartMonthDeptDetailA3TotalWholeYear.setDepartCode(manufacturingDeptConfig.getDeptA1AssemblyCodes());
        excelDepartMonthDeptDetailA3TotalWholeYear.setDepartName("合计");
        excelDepartMonthDeptDetailPackingWholeYear.setSalaryDate("1-12月");
        excelDepartMonthDeptDetailPackingWholeYear.setDepartCode(manufacturingDeptConfig.getDeptA1AssemblyCodes());
        excelDepartMonthDeptDetailPackingWholeYear.setDepartName("圆片级封装部");
        excelDepartMonthDeptDetailSurfaceWholeYear.setSalaryDate("1-12月");
        excelDepartMonthDeptDetailSurfaceWholeYear.setDepartCode(manufacturingDeptConfig.getDeptA1AssemblyCodes());
        excelDepartMonthDeptDetailSurfaceWholeYear.setDepartName("表面处理中心");

        excelDepartMonthVoHalfYear.setMonth("1-6月");
        excelDepartMonthVoWholeYear.setMonth("1-12月");
        for(int j=1;j<=12;j++){
            String month = year+"-"+(String.valueOf(j).length()==1?"0"+String.valueOf(j):String.valueOf(j));
            ExcelDepartMonthVo excelDepartMonthVo = new ExcelDepartMonthVo(); //月份
            excelDepartMonthVo.setMonth(month);
            List<ExcelDepartMonthDept> excelDepartMonthDepts = new ArrayList<>();
            ExcelDepartMonthDept excelDepartMonthDeptA1 = new ExcelDepartMonthDept();
            ExcelDepartMonthDept excelDepartMonthDeptD2 = new ExcelDepartMonthDept();
            ExcelDepartMonthDept excelDepartMonthDeptA3 = new ExcelDepartMonthDept();
            ExcelDepartMonthDept excelDepartMonthDeptPK = new ExcelDepartMonthDept();
            ExcelDepartMonthDept excelDepartMonthDeptSF = new ExcelDepartMonthDept();
            excelDepartMonthDeptA1.setDeptName("A1");
            excelDepartMonthDeptD2.setDeptName("D2");
            excelDepartMonthDeptA3.setDeptName("A3");
            excelDepartMonthDeptPK.setDeptName("圆片级封装部");
            excelDepartMonthDeptSF.setDeptName("表面处理中心");
            List<ExcelDepartMonthDeptDetail> excelDepartMonthDeptDetailsA1 = new ArrayList<ExcelDepartMonthDeptDetail>();
            List<ExcelDepartMonthDeptDetail> excelDepartMonthDeptDetailsD2 = new ArrayList<ExcelDepartMonthDeptDetail>();
            List<ExcelDepartMonthDeptDetail> excelDepartMonthDeptDetailsA3 = new ArrayList<ExcelDepartMonthDeptDetail>();
            List<ExcelDepartMonthDeptDetail> excelDepartMonthDeptDetailsPK = new ArrayList<ExcelDepartMonthDeptDetail>();
            List<ExcelDepartMonthDeptDetail> excelDepartMonthDeptDetailsSF = new ArrayList<ExcelDepartMonthDeptDetail>();
            ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailA1Total = new ExcelDepartMonthDeptDetail();
            ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailD2Total = new ExcelDepartMonthDeptDetail();
            ExcelDepartMonthDeptDetail excelDepartMonthDeptDetailA3Total = new ExcelDepartMonthDeptDetail();
            excelDepartMonthDeptDetailA1Total.setSalaryDate(month);
            excelDepartMonthDeptDetailD2Total.setSalaryDate(month);
            excelDepartMonthDeptDetailA3Total.setSalaryDate(month);
            excelDepartMonthDeptDetailA1Total.setDepartName("合计");
            excelDepartMonthDeptDetailD2Total.setDepartName("合计");
            excelDepartMonthDeptDetailA3Total.setDepartName("合计");
            for(int i=0;i<queryList.size();i++){
                if(queryList.get(i).getSalaryDate().equals(month)){
                    if(queryList.get(i).getDepartCode().equals(manufacturingDeptConfig.getDeptA1AssemblyCodes())){
                        excelDepartMonthDeptDetailsA1.add(queryList.get(i));
                        excelDepartMonthDeptDetailA1Total = doSum(excelDepartMonthDeptDetailA1Total,queryList.get(i));
                        excelDepartMonthDeptDetailA1AssemblyHalfYear  = doSum(excelDepartMonthDeptDetailA1AssemblyHalfYear,queryList.get(i));
                        excelDepartMonthDeptDetailA1TotalHalfYear = doSum(excelDepartMonthDeptDetailA1TotalHalfYear,queryList.get(i));
                        excelDepartMonthDeptDetailA1AssemblyWholeYear  = doSum(excelDepartMonthDeptDetailA1AssemblyWholeYear,queryList.get(i));
                        excelDepartMonthDeptDetailA1TotalWholeYear = doSum(excelDepartMonthDeptDetailA1TotalWholeYear ,queryList.get(i));
                    }else if(queryList.get(i).getDepartCode().equals(manufacturingDeptConfig.getDeptA1TestingCodes())){
                        excelDepartMonthDeptDetailsA1.add(queryList.get(i));
                        excelDepartMonthDeptDetailA1Total = doSum(excelDepartMonthDeptDetailA1Total,queryList.get(i));
                        excelDepartMonthDeptDetailA1testingHalfYear = doSum(excelDepartMonthDeptDetailA1testingHalfYear,queryList.get(i));
                        excelDepartMonthDeptDetailA1TotalHalfYear = doSum(excelDepartMonthDeptDetailA1TotalHalfYear ,queryList.get(i));
                        excelDepartMonthDeptDetailA1testingWholeYear = doSum(excelDepartMonthDeptDetailA1testingWholeYear,queryList.get(i));
                        excelDepartMonthDeptDetailA1TotalWholeYear = doSum(excelDepartMonthDeptDetailA1TotalWholeYear ,queryList.get(i));
                    }else if(queryList.get(i).getDepartCode().equals(manufacturingDeptConfig.getDeptD2AssemblyCodes())){
                        excelDepartMonthDeptDetailsD2.add(queryList.get(i));
                        excelDepartMonthDeptDetailD2Total = doSum(excelDepartMonthDeptDetailD2Total,queryList.get(i));
                        excelDepartMonthDeptDetailD2AssemblyHalfYear = doSum(excelDepartMonthDeptDetailD2AssemblyHalfYear,queryList.get(i));
                        excelDepartMonthDeptDetailD2TotalHalfYear = doSum(excelDepartMonthDeptDetailD2TotalHalfYear ,queryList.get(i));
                        excelDepartMonthDeptDetailD2AssemblyWholeYear = doSum(excelDepartMonthDeptDetailD2AssemblyWholeYear,queryList.get(i));
                        excelDepartMonthDeptDetailD2TotalWholeYear = doSum(excelDepartMonthDeptDetailD2TotalWholeYear ,queryList.get(i));
                    }else if(queryList.get(i).getDepartCode().equals(manufacturingDeptConfig.getDeptD2TestingCodes())){
                        excelDepartMonthDeptDetailsD2.add(queryList.get(i));
                        excelDepartMonthDeptDetailD2Total = doSum(excelDepartMonthDeptDetailD2Total,queryList.get(i));
                        excelDepartMonthDeptDetailD2testingHalfYear = doSum(excelDepartMonthDeptDetailD2testingHalfYear,queryList.get(i));
                        excelDepartMonthDeptDetailD2TotalHalfYear = doSum(excelDepartMonthDeptDetailD2TotalHalfYear ,queryList.get(i));
                        excelDepartMonthDeptDetailD2testingWholeYear = doSum(excelDepartMonthDeptDetailD2testingWholeYear,queryList.get(i));
                        excelDepartMonthDeptDetailD2TotalWholeYear = doSum(excelDepartMonthDeptDetailD2TotalWholeYear ,queryList.get(i));
                    }else if(queryList.get(i).getDepartCode().equals(manufacturingDeptConfig.getDeptA3AssemblyCodes())){
                        excelDepartMonthDeptDetailsA3.add(queryList.get(i));
                        excelDepartMonthDeptDetailA3Total = doSum(excelDepartMonthDeptDetailA3Total,queryList.get(i));
                        excelDepartMonthDeptDetailA3AssemblyHalfYear = doSum(excelDepartMonthDeptDetailA3AssemblyHalfYear ,queryList.get(i));
                        excelDepartMonthDeptDetailA3TotalHalfYear = doSum(excelDepartMonthDeptDetailA3TotalHalfYear ,queryList.get(i));
                        excelDepartMonthDeptDetailA3AssemblyWholeYear = doSum(excelDepartMonthDeptDetailA3AssemblyWholeYear ,queryList.get(i));
                        excelDepartMonthDeptDetailA3TotalWholeYear= doSum(excelDepartMonthDeptDetailA3TotalWholeYear ,queryList.get(i));
                    }else if(queryList.get(i).getDepartCode().equals(manufacturingDeptConfig.getDeptA3TestingCodes())){
                        excelDepartMonthDeptDetailsA3.add(queryList.get(i));
                        excelDepartMonthDeptDetailA3Total = doSum(excelDepartMonthDeptDetailA3Total,queryList.get(i));
                        excelDepartMonthDeptDetailA3TestingHalfYear  = doSum(excelDepartMonthDeptDetailA3TestingHalfYear ,queryList.get(i));
                        excelDepartMonthDeptDetailA3TotalHalfYear = doSum(excelDepartMonthDeptDetailA3TotalHalfYear ,queryList.get(i));
                        excelDepartMonthDeptDetailA3TestingWholeYear  = doSum(excelDepartMonthDeptDetailA3TestingWholeYear ,queryList.get(i));
                        excelDepartMonthDeptDetailA3TotalWholeYear = doSum(excelDepartMonthDeptDetailA3TotalWholeYear ,queryList.get(i));
                    }else if(queryList.get(i).getDepartCode().equals(manufacturingDeptConfig.getPackagingCodes())){
                        excelDepartMonthDeptDetailsPK.add(queryList.get(i));
                        excelDepartMonthDeptDetailPackingHalfYear  = doSum(excelDepartMonthDeptDetailPackingHalfYear  ,queryList.get(i));
                        excelDepartMonthDeptDetailPackingWholeYear  = doSum(excelDepartMonthDeptDetailPackingWholeYear  ,queryList.get(i));
                    }else if(queryList.get(i).getDepartCode().equals(manufacturingDeptConfig.getSurfaceCodes())){
                        excelDepartMonthDeptDetailsSF.add(queryList.get(i));
                        excelDepartMonthDeptDetailSurfaceHalfYear  = doSum(excelDepartMonthDeptDetailSurfaceHalfYear  ,queryList.get(i));
                        excelDepartMonthDeptDetailSurfaceWholeYear  = doSum(excelDepartMonthDeptDetailSurfaceWholeYear  ,queryList.get(i));
                    }
                }
            }
            excelDepartMonthDeptDetailsA1.add(excelDepartMonthDeptDetailA1Total);
            excelDepartMonthDeptDetailsD2.add(excelDepartMonthDeptDetailD2Total);
            excelDepartMonthDeptDetailsA3.add(excelDepartMonthDeptDetailA3Total);
            excelDepartMonthDeptA1.setExcelDepartMonthDeptDetails(excelDepartMonthDeptDetailsA1);
            excelDepartMonthDeptD2.setExcelDepartMonthDeptDetails(excelDepartMonthDeptDetailsD2);
            excelDepartMonthDeptA3.setExcelDepartMonthDeptDetails(excelDepartMonthDeptDetailsA3);
            excelDepartMonthDeptPK.setExcelDepartMonthDeptDetails(excelDepartMonthDeptDetailsPK);
            excelDepartMonthDeptSF.setExcelDepartMonthDeptDetails(excelDepartMonthDeptDetailsSF);
            excelDepartMonthDepts.add(excelDepartMonthDeptA1);
            excelDepartMonthDepts.add(excelDepartMonthDeptD2);
            excelDepartMonthDepts.add(excelDepartMonthDeptA3);
            excelDepartMonthDepts.add(excelDepartMonthDeptPK);
            excelDepartMonthDepts.add(excelDepartMonthDeptSF);
            excelDepartMonthVo.setExcelDepartMonthDepts(excelDepartMonthDepts);
            resultList.add(excelDepartMonthVo);
            if(j==6){
                excelDepartMonthDeptDetailsA1HalfYear.add(JSON.parseObject(JSON.toJSONString(excelDepartMonthDeptDetailA1AssemblyHalfYear),ExcelDepartMonthDeptDetail.class));
                excelDepartMonthDeptDetailsA1HalfYear.add(JSON.parseObject(JSON.toJSONString(excelDepartMonthDeptDetailA1testingHalfYear),ExcelDepartMonthDeptDetail.class));
                excelDepartMonthDeptDetailsA1HalfYear.add(JSON.parseObject(JSON.toJSONString(excelDepartMonthDeptDetailA1TotalHalfYear),ExcelDepartMonthDeptDetail.class));
                excelDepartMonthDeptDetailsD2HalfYear.add(JSON.parseObject(JSON.toJSONString(excelDepartMonthDeptDetailD2AssemblyHalfYear),ExcelDepartMonthDeptDetail.class));
                excelDepartMonthDeptDetailsD2HalfYear.add(JSON.parseObject(JSON.toJSONString(excelDepartMonthDeptDetailD2testingHalfYear),ExcelDepartMonthDeptDetail.class));
                excelDepartMonthDeptDetailsD2HalfYear.add(JSON.parseObject(JSON.toJSONString(excelDepartMonthDeptDetailD2TotalHalfYear),ExcelDepartMonthDeptDetail.class));
                excelDepartMonthDeptDetailsA3HalfYear.add(JSON.parseObject(JSON.toJSONString(excelDepartMonthDeptDetailA3AssemblyHalfYear),ExcelDepartMonthDeptDetail.class));
                excelDepartMonthDeptDetailsA3HalfYear.add(JSON.parseObject(JSON.toJSONString(excelDepartMonthDeptDetailA3TestingHalfYear),ExcelDepartMonthDeptDetail.class));
                excelDepartMonthDeptDetailsA3HalfYear.add(JSON.parseObject(JSON.toJSONString(excelDepartMonthDeptDetailA3TotalHalfYear),ExcelDepartMonthDeptDetail.class));
                excelDepartMonthDeptDetailsPKHalfYear.add(JSON.parseObject(JSON.toJSONString(excelDepartMonthDeptDetailPackingHalfYear),ExcelDepartMonthDeptDetail.class));
                excelDepartMonthDeptDetailsSFHalfYear.add(JSON.parseObject(JSON.toJSONString(excelDepartMonthDeptDetailSurfaceHalfYear),ExcelDepartMonthDeptDetail.class));
                excelDepartMonthDeptA1HalfYear.setExcelDepartMonthDeptDetails(excelDepartMonthDeptDetailsA1HalfYear);
                excelDepartMonthDeptD2HalfYear.setExcelDepartMonthDeptDetails(excelDepartMonthDeptDetailsD2HalfYear);
                excelDepartMonthDeptA3HalfYear.setExcelDepartMonthDeptDetails(excelDepartMonthDeptDetailsA3HalfYear);
                excelDepartMonthDeptPKHalfYear.setExcelDepartMonthDeptDetails(excelDepartMonthDeptDetailsPKHalfYear);
                excelDepartMonthDeptSFHalfYear.setExcelDepartMonthDeptDetails(excelDepartMonthDeptDetailsSFHalfYear);
                excelDepartMonthDeptsHalfYear.add(excelDepartMonthDeptA1HalfYear);
                excelDepartMonthDeptsHalfYear.add(excelDepartMonthDeptD2HalfYear);
                excelDepartMonthDeptsHalfYear.add(excelDepartMonthDeptA3HalfYear);
                excelDepartMonthDeptsHalfYear.add(excelDepartMonthDeptPKHalfYear);
                excelDepartMonthDeptsHalfYear.add(excelDepartMonthDeptSFHalfYear);
                excelDepartMonthVoHalfYear.setExcelDepartMonthDepts(excelDepartMonthDeptsHalfYear);
                resultList.add(excelDepartMonthVoHalfYear);
            }
            if(j==12){
                excelDepartMonthDeptDetailsA1WholeYear.add(excelDepartMonthDeptDetailA1AssemblyWholeYear);
                excelDepartMonthDeptDetailsA1WholeYear.add(excelDepartMonthDeptDetailA1testingWholeYear);
                excelDepartMonthDeptDetailsA1WholeYear.add(excelDepartMonthDeptDetailA1TotalWholeYear);
                excelDepartMonthDeptDetailsD2WholeYear.add(excelDepartMonthDeptDetailD2AssemblyWholeYear);
                excelDepartMonthDeptDetailsD2WholeYear.add(excelDepartMonthDeptDetailD2testingWholeYear);
                excelDepartMonthDeptDetailsD2WholeYear.add(excelDepartMonthDeptDetailD2TotalWholeYear);
                excelDepartMonthDeptDetailsA3WholeYear.add(excelDepartMonthDeptDetailA3AssemblyWholeYear);
                excelDepartMonthDeptDetailsA3WholeYear.add(excelDepartMonthDeptDetailA3TestingWholeYear);
                excelDepartMonthDeptDetailsA3WholeYear.add(excelDepartMonthDeptDetailA3TotalWholeYear);
                excelDepartMonthDeptDetailsPKWholeYear.add(excelDepartMonthDeptDetailPackingWholeYear);
                excelDepartMonthDeptDetailsSFWholeYear.add(excelDepartMonthDeptDetailSurfaceWholeYear);
                excelDepartMonthDeptA1WholeYear.setExcelDepartMonthDeptDetails(excelDepartMonthDeptDetailsA1WholeYear);
                excelDepartMonthDeptD2WholeYear.setExcelDepartMonthDeptDetails(excelDepartMonthDeptDetailsD2WholeYear);
                excelDepartMonthDeptA3WholeYear.setExcelDepartMonthDeptDetails(excelDepartMonthDeptDetailsA3WholeYear);
                excelDepartMonthDeptPKWholeYear.setExcelDepartMonthDeptDetails(excelDepartMonthDeptDetailsPKWholeYear);
                excelDepartMonthDeptSFWholeYear.setExcelDepartMonthDeptDetails(excelDepartMonthDeptDetailsSFWholeYear);
                excelDepartMonthDeptsWholeYear.add(excelDepartMonthDeptA1WholeYear);
                excelDepartMonthDeptsWholeYear.add(excelDepartMonthDeptD2WholeYear);
                excelDepartMonthDeptsWholeYear.add(excelDepartMonthDeptA3WholeYear);
                excelDepartMonthDeptsWholeYear.add(excelDepartMonthDeptPKWholeYear);
                excelDepartMonthDeptsWholeYear.add(excelDepartMonthDeptSFWholeYear);
                excelDepartMonthVoWholeYear.setExcelDepartMonthDepts(excelDepartMonthDeptsWholeYear);
                resultList.add(excelDepartMonthVoWholeYear);
            }
        }
        return resultList;
    }

    private ExcelDepartMonthDeptDetail doSum(ExcelDepartMonthDeptDetail excelDepartMonthDeptDetail1,ExcelDepartMonthDeptDetail excelDepartMonthDeptDetail2){
        excelDepartMonthDeptDetail1.setUserCount(excelDepartMonthDeptDetail1.getUserCount()==null?0+excelDepartMonthDeptDetail2.getUserCount():excelDepartMonthDeptDetail1.getUserCount()+excelDepartMonthDeptDetail2.getUserCount());
        excelDepartMonthDeptDetail1.setSalary(excelDepartMonthDeptDetail1.getSalary()==null?excelDepartMonthDeptDetail2.getSalary().add(new BigDecimal("0")):excelDepartMonthDeptDetail1.getSalary().add(excelDepartMonthDeptDetail2.getSalary()));
        excelDepartMonthDeptDetail1.setFlf(excelDepartMonthDeptDetail1.getFlf()==null?excelDepartMonthDeptDetail2.getFlf().add(new BigDecimal("0")):excelDepartMonthDeptDetail1.getFlf().add(excelDepartMonthDeptDetail2.getFlf()));
        excelDepartMonthDeptDetail1.setGjj(excelDepartMonthDeptDetail1.getGjj()==null?excelDepartMonthDeptDetail2.getGjj().add(new BigDecimal("0")):excelDepartMonthDeptDetail1.getGjj().add(excelDepartMonthDeptDetail2.getGjj()));
        excelDepartMonthDeptDetail1.setOtherSalary(excelDepartMonthDeptDetail1.getOtherSalary()==null?excelDepartMonthDeptDetail2.getOtherSalary().add(new BigDecimal("0")):excelDepartMonthDeptDetail1.getOtherSalary().add(excelDepartMonthDeptDetail2.getOtherSalary()));
        excelDepartMonthDeptDetail1.setYearTotal(excelDepartMonthDeptDetail1.getYearTotal()==null?excelDepartMonthDeptDetail2.getYearTotal().add(new BigDecimal("0")):excelDepartMonthDeptDetail1.getYearTotal().add(excelDepartMonthDeptDetail2.getYearTotal()));
        excelDepartMonthDeptDetail1.setTotal(excelDepartMonthDeptDetail1.getTotal()==null?excelDepartMonthDeptDetail2.getTotal().add(new BigDecimal("0")):excelDepartMonthDeptDetail1.getTotal().add(excelDepartMonthDeptDetail2.getTotal()));
        return excelDepartMonthDeptDetail1;
    }

    private String getMonth(String dateStr, Integer lastTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date date = null;
        try {
            date = dateStr.equals("") ? new Date():format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + lastTime);
        date = calendar.getTime();
        String accDate = format.format(date);
        return accDate;
    }
}
