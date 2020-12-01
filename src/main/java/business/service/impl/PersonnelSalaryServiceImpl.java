package business.service.impl;

import business.bean.ManufacturingDeptConfig;
import business.bean.PersonnelSalary;
import business.bean.SalaryReportConfig;
import business.mapper.OperateLogMapper;
import business.mapper.PersonnelSalaryMapper;
import business.mapper.PersonnelWelfareMapper;
import business.mapper.SalaryReportConfigMapper;
import business.service.IPersonnelSalaryService;
import business.vo.PersonnelSalaryVO;
import business.vo.excel.*;
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
    @Resource
    private SalaryReportConfigMapper salaryReportConfigMapper;

    @Override
    public IPage<PersonnelSalaryVO> getPersonnelSalaryList(PersonnelSalaryVO personnelSalaryVo, String site, Integer pageNo,Integer pageSize) {
        IPage<PersonnelSalaryVO> page = new Page<PersonnelSalaryVO>(pageNo, pageSize);
        QueryWrapper<PersonnelSalaryVO> sqlaryQueryWrapper = new QueryWrapper<>();
        //sqlaryQueryWrapper.lambda().orderByDesc(PersonnelSalary::getId);
        if (StringUtils.isNotBlank(personnelSalaryVo.getWorkcode())) {
            sqlaryQueryWrapper.eq("t1.workcode", personnelSalaryVo.getWorkcode());
        }
        if(StringUtils.isNotBlank(personnelSalaryVo.getDept())){
            sqlaryQueryWrapper.eq("t2.DEPARTID", personnelSalaryVo.getDept().split("_")[0]);
        }
        if(StringUtils.isNotBlank(personnelSalaryVo.getSalarystamonth())){
            sqlaryQueryWrapper.ge("t1.salary_date", personnelSalaryVo.getSalarystamonth());
        }
        if(StringUtils.isNotBlank(personnelSalaryVo.getSalaryendmonth())){
            sqlaryQueryWrapper.le("t1.salary_date", personnelSalaryVo.getSalaryendmonth());
        }
        return personnelSalaryMapper.getPersonnelSalary(page, sqlaryQueryWrapper,site);
    }

    @Override
    public List<PersonnelSalaryVO> getPersonnelSalaryList(Wrapper<PersonnelSalaryVO> queryWrapper) {
        return personnelSalaryMapper.getPersonnelSalary(queryWrapper);
    }


    @Override
    public Map<String, Object> getReportHeader(String site) {
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String currentMonth = sdf.format(currentDate);
        String lastMonthStr = getMonth("",-1);
        String currentMonthSalary = personnelSalaryMapper.getSalaryByMonth(currentMonth,site).size()==0?"0":personnelSalaryMapper.getSalaryByMonth(currentMonth,site).get(0).get("GROSS_PAY").toString();
        String lastMonthSalary = personnelSalaryMapper.getSalaryByMonth(lastMonthStr,site).size()==0?"0":personnelSalaryMapper.getSalaryByMonth(lastMonthStr,site).get(0).get("GROSS_PAY").toString();
        String currentMonthImportNumber = personnelSalaryMapper.getImportNumberByMonth(currentMonth,site).size()==0?"0":personnelSalaryMapper.getImportNumberByMonth(currentMonth,site).get(0).get("IMPORT_NUMBER").toString();
        String lastMonthImportNumber = personnelSalaryMapper.getImportNumberByMonth(lastMonthStr,site).size()==0?"0":personnelSalaryMapper.getImportNumberByMonth(lastMonthStr,site).get(0).get("IMPORT_NUMBER").toString();
        String currentMonthVisitTimes = operateLogMapper.getVisitTimesByMonth(currentMonth,site).size()==0?"0":operateLogMapper.getVisitTimesByMonth(currentMonth,site).get(0).get("VISIT_TIMES").toString();
        String lastMonthVisitTimes = operateLogMapper.getVisitTimesByMonth(lastMonthStr,site).size()==0?"0":operateLogMapper.getVisitTimesByMonth(lastMonthStr,site).get(0).get("VISIT_TIMES").toString();
        String currentMonthNoteNumber = operateLogMapper.getNoteNumberByMonth(currentMonth,site).size()==0?"0":operateLogMapper.getNoteNumberByMonth(currentMonth,site).get(0).get("NOTE_NUMBER").toString();
        String lastMonthNoteNumber = operateLogMapper.getNoteNumberByMonth(lastMonthStr,site).size()==0?"0":operateLogMapper.getNoteNumberByMonth(currentMonth,site).get(0).get("NOTE_NUMBER").toString();
        List<Map<String, Object>> salaryDepartmentRankList = personnelSalaryMapper.getSalaryRankByDepartment(currentMonth,site);
        List<Map<String, Object>> noteTimesDepartmentRankList = operateLogMapper.getNoteTimesRankByDepartment(currentMonth,site);
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
    public Map<String, Object> getReportBodyList(String staDate, String endDate,String site) {
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
        List<Map<String, Object>> salaryList = personnelSalaryMapper.getSalaryBetweenMonth(staDate,endDate,site);
        List<Map<String, Object>> noteTimesList = operateLogMapper.getNoteTimesBetweenMonth(staDate,endDate,site);
        result.put("salaryList",salaryList);
        result.put("noteTimesList",noteTimesList);
        return result;
    }

    @Override
    public List<Map<String, Object>> getMonthlyLaborCost(String year, Float rate, String site) {
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> monthlySalaryInfoList = personnelSalaryMapper.getMonthlySalaryInfoByYear(year,rate,site);
        Map<String,Object> wholeYearSalaryInfo = personnelSalaryMapper.getWholeYearSalaryInfoByYear(year,rate,site);
        for(int i=1;i<=12;i++){
            Map<String,Object> monthResultMap = new LinkedHashMap<String,Object>();
            String month = year+"-"+(String.valueOf(i).length()==1?"0"+String.valueOf(i):String.valueOf(i));
            monthResultMap.put("REMARK",i+"月");
            Integer salaryRs = 0;
            Float yfgz = 0f;
            Float gjj = 0f;
            Float ssx = 0f;
            Float jj = 0f;
            Float fl = 0f;
            Float subtotal = 0f;
            for(int j=0;j<monthlySalaryInfoList.size();j++){
                if(month.equals(monthlySalaryInfoList.get(j).get("SALARY_DATE"))){
                    salaryRs = Integer.parseInt(monthlySalaryInfoList.get(j).get("RS").toString());
                    yfgz = Float.parseFloat(monthlySalaryInfoList.get(j).get("YFGZ").toString());
                    gjj = Float.parseFloat(monthlySalaryInfoList.get(j).get("GJJ").toString());
                    ssx = Float.parseFloat(monthlySalaryInfoList.get(j).get("SSX").toString());
                    jj = Float.parseFloat(monthlySalaryInfoList.get(j).get("JJ").toString());
                    fl = Float.parseFloat(monthlySalaryInfoList.get(j).get("FL").toString());
                    subtotal = Float.parseFloat(monthlySalaryInfoList.get(j).get("TOTAL").toString());
                }
            }
            monthResultMap.put("RS",salaryRs);
            monthResultMap.put("YFGZ",yfgz);
            monthResultMap.put("GJJ",gjj);
            monthResultMap.put("SSX",ssx);
            monthResultMap.put("JJ",jj);
            monthResultMap.put("FL",fl);
            monthResultMap.put("TOTAL",subtotal);
            resultList.add(monthResultMap);
        }
        resultList.add(wholeYearSalaryInfo);
        return resultList;
    }

    @Override
    public List<MonthlyLaborCostByDeptVo> getMonthlyLaborCostByDept(String year, Float rate,String site,String tabId) {
        List<MonthlyLaborCostByDeptVo> monthlyLaborCostByDeptVoList = new ArrayList<MonthlyLaborCostByDeptVo>();
        List<SalaryReportConfig> salaryReportConfigList=salaryReportConfigMapper.getSalaryReportConfig(site,tabId);
        List<Map<String,Object>> resultList = personnelSalaryMapper.getMonthlyLaborCostByDept(year,rate,site,tabId);

        for(SalaryReportConfig salaryReportConfig:salaryReportConfigList){
            MonthlyLaborCostByDeptVo monthlyLaborCostByDeptVo = new MonthlyLaborCostByDeptVo();
            monthlyLaborCostByDeptVo.setDepartName(salaryReportConfig.getDepartName());
            for(Map<String,Object> result:resultList){
                if(result.get("SALARY_DATE").equals(year+"-01")&&salaryReportConfig.getId().equals(result.get("MAINID"))){
                    monthlyLaborCostByDeptVo.setJanHrmNumber(Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setJanGrossPay(new BigDecimal(result.get("GROSS_PAY").toString()));
                    monthlyLaborCostByDeptVo.setJanWelfareAmountWeal(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByDeptVo.setJanIaf(new BigDecimal(result.get("GJJ").toString()));
                    monthlyLaborCostByDeptVo.setJanWelfareAmountSalaries(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByDeptVo.setJanWelfareAmountBonus(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByDeptVo.setJanSubtotal(new BigDecimal(result.get("TOTAL").toString()));

                    //半年
                    monthlyLaborCostByDeptVo.setHalfHrmNumber(monthlyLaborCostByDeptVo.getHalfHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setHalfGrossPay(monthlyLaborCostByDeptVo.getHalfGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountWeal(monthlyLaborCostByDeptVo.getHalfWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setHalfIaf(monthlyLaborCostByDeptVo.getHalfIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountSalaries(monthlyLaborCostByDeptVo.getHalfWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountBonus(monthlyLaborCostByDeptVo.getHalfWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setHalfSubtotal(monthlyLaborCostByDeptVo.getHalfSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                    //整年
                    monthlyLaborCostByDeptVo.setWholeHrmNumber(monthlyLaborCostByDeptVo.getWholeHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setWholeGrossPay(monthlyLaborCostByDeptVo.getWholeGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountWeal(monthlyLaborCostByDeptVo.getWholeWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setWholeIaf(monthlyLaborCostByDeptVo.getWholeIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountSalaries(monthlyLaborCostByDeptVo.getWholeWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountBonus(monthlyLaborCostByDeptVo.getWholeWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setWholeSubtotal(monthlyLaborCostByDeptVo.getWholeSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                }else if(result.get("SALARY_DATE").equals(year+"-02")&&salaryReportConfig.getId().equals(result.get("MAINID"))){
                    monthlyLaborCostByDeptVo.setFebHrmNumber(Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setFebGrossPay(new BigDecimal(result.get("GROSS_PAY").toString()));
                    monthlyLaborCostByDeptVo.setFebWelfareAmountWeal(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByDeptVo.setFebIaf(new BigDecimal(result.get("GJJ").toString()));
                    monthlyLaborCostByDeptVo.setFebWelfareAmountSalaries(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByDeptVo.setFebWelfareAmountBonus(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByDeptVo.setFebSubtotal(new BigDecimal(result.get("TOTAL").toString()));

                    //半年
                    monthlyLaborCostByDeptVo.setHalfHrmNumber(monthlyLaborCostByDeptVo.getHalfHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setHalfGrossPay(monthlyLaborCostByDeptVo.getHalfGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountWeal(monthlyLaborCostByDeptVo.getHalfWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setHalfIaf(monthlyLaborCostByDeptVo.getHalfIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountSalaries(monthlyLaborCostByDeptVo.getHalfWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountBonus(monthlyLaborCostByDeptVo.getHalfWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setHalfSubtotal(monthlyLaborCostByDeptVo.getHalfSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                    //整年
                    monthlyLaborCostByDeptVo.setWholeHrmNumber(monthlyLaborCostByDeptVo.getWholeHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setWholeGrossPay(monthlyLaborCostByDeptVo.getWholeGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountWeal(monthlyLaborCostByDeptVo.getWholeWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setWholeIaf(monthlyLaborCostByDeptVo.getWholeIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountSalaries(monthlyLaborCostByDeptVo.getWholeWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountBonus(monthlyLaborCostByDeptVo.getWholeWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setWholeSubtotal(monthlyLaborCostByDeptVo.getWholeSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                }else if(result.get("SALARY_DATE").equals(year+"-03")&&salaryReportConfig.getId().equals(result.get("MAINID"))){
                    monthlyLaborCostByDeptVo.setMarHrmNumber(Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setMarGrossPay(new BigDecimal(result.get("GROSS_PAY").toString()));
                    monthlyLaborCostByDeptVo.setMarWelfareAmountWeal(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByDeptVo.setMarIaf(new BigDecimal(result.get("GJJ").toString()));
                    monthlyLaborCostByDeptVo.setMarWelfareAmountSalaries(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByDeptVo.setMarWelfareAmountBonus(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByDeptVo.setMarSubtotal(new BigDecimal(result.get("TOTAL").toString()));

                    //半年
                    monthlyLaborCostByDeptVo.setHalfHrmNumber(monthlyLaborCostByDeptVo.getHalfHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setHalfGrossPay(monthlyLaborCostByDeptVo.getHalfGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountWeal(monthlyLaborCostByDeptVo.getHalfWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setHalfIaf(monthlyLaborCostByDeptVo.getHalfIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountSalaries(monthlyLaborCostByDeptVo.getHalfWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountBonus(monthlyLaborCostByDeptVo.getHalfWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setHalfSubtotal(monthlyLaborCostByDeptVo.getHalfSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                    //整年
                    monthlyLaborCostByDeptVo.setWholeHrmNumber(monthlyLaborCostByDeptVo.getWholeHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setWholeGrossPay(monthlyLaborCostByDeptVo.getWholeGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountWeal(monthlyLaborCostByDeptVo.getWholeWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setWholeIaf(monthlyLaborCostByDeptVo.getWholeIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountSalaries(monthlyLaborCostByDeptVo.getWholeWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountBonus(monthlyLaborCostByDeptVo.getWholeWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setWholeSubtotal(monthlyLaborCostByDeptVo.getWholeSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                }else if(result.get("SALARY_DATE").equals(year+"-04")&&salaryReportConfig.getId().equals(result.get("MAINID"))){
                    monthlyLaborCostByDeptVo.setAprHrmNumber(Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setAprGrossPay(new BigDecimal(result.get("GROSS_PAY").toString()));
                    monthlyLaborCostByDeptVo.setAprWelfareAmountWeal(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByDeptVo.setAprIaf(new BigDecimal(result.get("GJJ").toString()));
                    monthlyLaborCostByDeptVo.setAprWelfareAmountSalaries(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByDeptVo.setAprWelfareAmountBonus(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByDeptVo.setAprSubtotal(new BigDecimal(result.get("TOTAL").toString()));

                    //半年
                    monthlyLaborCostByDeptVo.setHalfHrmNumber(monthlyLaborCostByDeptVo.getHalfHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setHalfGrossPay(monthlyLaborCostByDeptVo.getHalfGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountWeal(monthlyLaborCostByDeptVo.getHalfWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setHalfIaf(monthlyLaborCostByDeptVo.getHalfIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountSalaries(monthlyLaborCostByDeptVo.getHalfWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountBonus(monthlyLaborCostByDeptVo.getHalfWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setHalfSubtotal(monthlyLaborCostByDeptVo.getHalfSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                    //整年
                    monthlyLaborCostByDeptVo.setWholeHrmNumber(monthlyLaborCostByDeptVo.getWholeHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setWholeGrossPay(monthlyLaborCostByDeptVo.getWholeGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountWeal(monthlyLaborCostByDeptVo.getWholeWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setWholeIaf(monthlyLaborCostByDeptVo.getWholeIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountSalaries(monthlyLaborCostByDeptVo.getWholeWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountBonus(monthlyLaborCostByDeptVo.getWholeWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setWholeSubtotal(monthlyLaborCostByDeptVo.getWholeSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                }else if(result.get("SALARY_DATE").equals(year+"-05")&&salaryReportConfig.getId().equals(result.get("MAINID"))){
                    monthlyLaborCostByDeptVo.setMayHrmNumber(Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setMayGrossPay(new BigDecimal(result.get("GROSS_PAY").toString()));
                    monthlyLaborCostByDeptVo.setMayWelfareAmountWeal(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByDeptVo.setMayIaf(new BigDecimal(result.get("GJJ").toString()));
                    monthlyLaborCostByDeptVo.setMayWelfareAmountSalaries(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByDeptVo.setMayWelfareAmountBonus(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByDeptVo.setMaySubtotal(new BigDecimal(result.get("TOTAL").toString()));

                    //半年
                    monthlyLaborCostByDeptVo.setHalfHrmNumber(monthlyLaborCostByDeptVo.getHalfHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setHalfGrossPay(monthlyLaborCostByDeptVo.getHalfGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountWeal(monthlyLaborCostByDeptVo.getHalfWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setHalfIaf(monthlyLaborCostByDeptVo.getHalfIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountSalaries(monthlyLaborCostByDeptVo.getHalfWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountBonus(monthlyLaborCostByDeptVo.getHalfWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setHalfSubtotal(monthlyLaborCostByDeptVo.getHalfSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                    //整年
                    monthlyLaborCostByDeptVo.setWholeHrmNumber(monthlyLaborCostByDeptVo.getWholeHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setWholeGrossPay(monthlyLaborCostByDeptVo.getWholeGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountWeal(monthlyLaborCostByDeptVo.getWholeWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setWholeIaf(monthlyLaborCostByDeptVo.getWholeIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountSalaries(monthlyLaborCostByDeptVo.getWholeWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountBonus(monthlyLaborCostByDeptVo.getWholeWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setWholeSubtotal(monthlyLaborCostByDeptVo.getWholeSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                }else if(result.get("SALARY_DATE").equals(year+"-06")&&salaryReportConfig.getId().equals(result.get("MAINID"))){
                    monthlyLaborCostByDeptVo.setJunHrmNumber(Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setJunGrossPay(new BigDecimal(result.get("GROSS_PAY").toString()));
                    monthlyLaborCostByDeptVo.setJunWelfareAmountWeal(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByDeptVo.setJunIaf(new BigDecimal(result.get("GJJ").toString()));
                    monthlyLaborCostByDeptVo.setJunWelfareAmountSalaries(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByDeptVo.setJunWelfareAmountBonus(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByDeptVo.setJunSubtotal(new BigDecimal(result.get("TOTAL").toString()));

                    //半年
                    monthlyLaborCostByDeptVo.setHalfHrmNumber(monthlyLaborCostByDeptVo.getHalfHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setHalfGrossPay(monthlyLaborCostByDeptVo.getHalfGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountWeal(monthlyLaborCostByDeptVo.getHalfWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setHalfIaf(monthlyLaborCostByDeptVo.getHalfIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountSalaries(monthlyLaborCostByDeptVo.getHalfWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountBonus(monthlyLaborCostByDeptVo.getHalfWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setHalfSubtotal(monthlyLaborCostByDeptVo.getHalfSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                    //整年
                    monthlyLaborCostByDeptVo.setWholeHrmNumber(monthlyLaborCostByDeptVo.getWholeHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setWholeGrossPay(monthlyLaborCostByDeptVo.getWholeGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountWeal(monthlyLaborCostByDeptVo.getWholeWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setWholeIaf(monthlyLaborCostByDeptVo.getWholeIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountSalaries(monthlyLaborCostByDeptVo.getWholeWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountBonus(monthlyLaborCostByDeptVo.getWholeWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setWholeSubtotal(monthlyLaborCostByDeptVo.getWholeSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                }else if(result.get("SALARY_DATE").equals(year+"-07")&&salaryReportConfig.getId().equals(result.get("MAINID"))){
                    monthlyLaborCostByDeptVo.setJulHrmNumber(Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setJulGrossPay(new BigDecimal(result.get("GROSS_PAY").toString()));
                    monthlyLaborCostByDeptVo.setJulWelfareAmountWeal(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByDeptVo.setJulIaf(new BigDecimal(result.get("GJJ").toString()));
                    monthlyLaborCostByDeptVo.setJulWelfareAmountSalaries(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByDeptVo.setJulWelfareAmountBonus(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByDeptVo.setJulSubtotal(new BigDecimal(result.get("TOTAL").toString()));

                    //整年
                    monthlyLaborCostByDeptVo.setWholeHrmNumber(monthlyLaborCostByDeptVo.getWholeHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setWholeGrossPay(monthlyLaborCostByDeptVo.getWholeGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountWeal(monthlyLaborCostByDeptVo.getWholeWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setWholeIaf(monthlyLaborCostByDeptVo.getWholeIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountSalaries(monthlyLaborCostByDeptVo.getWholeWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountBonus(monthlyLaborCostByDeptVo.getWholeWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setWholeSubtotal(monthlyLaborCostByDeptVo.getWholeSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                }else if(result.get("SALARY_DATE").equals(year+"-08")&&salaryReportConfig.getId().equals(result.get("MAINID"))){
                    monthlyLaborCostByDeptVo.setAugHrmNumber(Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setAugGrossPay(new BigDecimal(result.get("GROSS_PAY").toString()));
                    monthlyLaborCostByDeptVo.setAugWelfareAmountWeal(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByDeptVo.setAugIaf(new BigDecimal(result.get("GJJ").toString()));
                    monthlyLaborCostByDeptVo.setAugWelfareAmountSalaries(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByDeptVo.setAugWelfareAmountBonus(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByDeptVo.setAugSubtotal(new BigDecimal(result.get("TOTAL").toString()));

                    //整年
                    monthlyLaborCostByDeptVo.setWholeHrmNumber(monthlyLaborCostByDeptVo.getWholeHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setWholeGrossPay(monthlyLaborCostByDeptVo.getWholeGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountWeal(monthlyLaborCostByDeptVo.getWholeWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setWholeIaf(monthlyLaborCostByDeptVo.getWholeIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountSalaries(monthlyLaborCostByDeptVo.getWholeWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountBonus(monthlyLaborCostByDeptVo.getWholeWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setWholeSubtotal(monthlyLaborCostByDeptVo.getWholeSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                }else if(result.get("SALARY_DATE").equals(year+"-09")&&salaryReportConfig.getId().equals(result.get("MAINID"))){
                    monthlyLaborCostByDeptVo.setSepHrmNumber(Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setSepGrossPay(new BigDecimal(result.get("GROSS_PAY").toString()));
                    monthlyLaborCostByDeptVo.setSepWelfareAmountWeal(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByDeptVo.setSepIaf(new BigDecimal(result.get("GJJ").toString()));
                    monthlyLaborCostByDeptVo.setSepWelfareAmountSalaries(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByDeptVo.setSepWelfareAmountBonus(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByDeptVo.setSepSubtotal(new BigDecimal(result.get("TOTAL").toString()));

                    //整年
                    monthlyLaborCostByDeptVo.setWholeHrmNumber(monthlyLaborCostByDeptVo.getWholeHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setWholeGrossPay(monthlyLaborCostByDeptVo.getWholeGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountWeal(monthlyLaborCostByDeptVo.getWholeWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setWholeIaf(monthlyLaborCostByDeptVo.getWholeIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountSalaries(monthlyLaborCostByDeptVo.getWholeWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountBonus(monthlyLaborCostByDeptVo.getWholeWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setWholeSubtotal(monthlyLaborCostByDeptVo.getWholeSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                }else if(result.get("SALARY_DATE").equals(year+"-10")&&salaryReportConfig.getId().equals(result.get("MAINID"))){
                    monthlyLaborCostByDeptVo.setOctHrmNumber(Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setOctGrossPay(new BigDecimal(result.get("GROSS_PAY").toString()));
                    monthlyLaborCostByDeptVo.setOctWelfareAmountWeal(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByDeptVo.setOctIaf(new BigDecimal(result.get("GJJ").toString()));
                    monthlyLaborCostByDeptVo.setOctWelfareAmountSalaries(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByDeptVo.setOctWelfareAmountBonus(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByDeptVo.setOctSubtotal(new BigDecimal(result.get("TOTAL").toString()));

                    //整年
                    monthlyLaborCostByDeptVo.setWholeHrmNumber(monthlyLaborCostByDeptVo.getWholeHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setWholeGrossPay(monthlyLaborCostByDeptVo.getWholeGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountWeal(monthlyLaborCostByDeptVo.getWholeWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setWholeIaf(monthlyLaborCostByDeptVo.getWholeIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountSalaries(monthlyLaborCostByDeptVo.getWholeWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountBonus(monthlyLaborCostByDeptVo.getWholeWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setWholeSubtotal(monthlyLaborCostByDeptVo.getWholeSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                }else if(result.get("SALARY_DATE").equals(year+"-11")&&salaryReportConfig.getId().equals(result.get("MAINID"))){
                    monthlyLaborCostByDeptVo.setNovHrmNumber(Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setNovGrossPay(new BigDecimal(result.get("GROSS_PAY").toString()));
                    monthlyLaborCostByDeptVo.setNovWelfareAmountWeal(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByDeptVo.setNovIaf(new BigDecimal(result.get("GJJ").toString()));
                    monthlyLaborCostByDeptVo.setNovWelfareAmountSalaries(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByDeptVo.setNovWelfareAmountBonus(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByDeptVo.setNovSubtotal(new BigDecimal(result.get("TOTAL").toString()));

                    //整年
                    monthlyLaborCostByDeptVo.setWholeHrmNumber(monthlyLaborCostByDeptVo.getWholeHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setWholeGrossPay(monthlyLaborCostByDeptVo.getWholeGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountWeal(monthlyLaborCostByDeptVo.getWholeWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setWholeIaf(monthlyLaborCostByDeptVo.getWholeIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountSalaries(monthlyLaborCostByDeptVo.getWholeWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountBonus(monthlyLaborCostByDeptVo.getWholeWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setWholeSubtotal(monthlyLaborCostByDeptVo.getWholeSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                }else if(result.get("SALARY_DATE").equals(year+"-12")&&salaryReportConfig.getId().equals(result.get("MAINID"))){
                    monthlyLaborCostByDeptVo.setDecHrmNumber(Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setDecGrossPay(new BigDecimal(result.get("GROSS_PAY").toString()));
                    monthlyLaborCostByDeptVo.setDecWelfareAmountWeal(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByDeptVo.setDecIaf(new BigDecimal(result.get("GJJ").toString()));
                    monthlyLaborCostByDeptVo.setDecWelfareAmountSalaries(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByDeptVo.setDecWelfareAmountBonus(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByDeptVo.setDecSubtotal(new BigDecimal(result.get("TOTAL").toString()));

                    //整年
                    monthlyLaborCostByDeptVo.setWholeHrmNumber(monthlyLaborCostByDeptVo.getWholeHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setWholeGrossPay(monthlyLaborCostByDeptVo.getWholeGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountWeal(monthlyLaborCostByDeptVo.getWholeWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setWholeIaf(monthlyLaborCostByDeptVo.getWholeIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountSalaries(monthlyLaborCostByDeptVo.getWholeWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountBonus(monthlyLaborCostByDeptVo.getWholeWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setWholeSubtotal(monthlyLaborCostByDeptVo.getWholeSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                }
            }
            monthlyLaborCostByDeptVoList.add(monthlyLaborCostByDeptVo);
        }
        return monthlyLaborCostByDeptVoList;
    }

    @Override
    public List<MonthlyLaborCostByTypeVo> getMonthlyLaborCostByType(String month, Float rate, String site, String tabId) {
        List<MonthlyLaborCostByTypeVo> monthlyLaborCostByTypeVoList = new ArrayList<MonthlyLaborCostByTypeVo>();
        //获取部门列
        //01 工程技术人员
        //02 业务技术管理人员
        //03 生产作业人员
        //04 生产保障人员
        //05 后勤服务人员
        //06 质量检验人员
        //07 未定

        List<SalaryReportConfig> salaryReportConfigList = salaryReportConfigMapper.getSalaryReportConfig(site,tabId);
        List<Map<String,Object>> salaryDataInfoList = personnelSalaryMapper.getMonthlyLaborCostByType(month,rate,site,tabId);
        for(SalaryReportConfig salaryReportConfig:salaryReportConfigList){
            MonthlyLaborCostByTypeVo monthlyLaborCostByTypeVo = new MonthlyLaborCostByTypeVo();
            monthlyLaborCostByTypeVo.setDepartName(salaryReportConfig.getDepartName());
            for(Map<String,Object> salaryDataInfo:salaryDataInfoList){
                if(salaryDataInfo.get("TYPE_ID").toString().equals("01")&&salaryDataInfo.get("MAINID").equals(salaryReportConfig.getId())){
                    monthlyLaborCostByTypeVo.setEtHrmNumber(Integer.parseInt(salaryDataInfo.get("WORKCODE").toString()));
                    monthlyLaborCostByTypeVo.setEtGrossPay(new BigDecimal(salaryDataInfo.get("GROSS_PAY").toString()));
                    monthlyLaborCostByTypeVo.setEtAcFund(new BigDecimal(salaryDataInfo.get("HOUSEPOVIDENT_FUND").toString()));
                    monthlyLaborCostByTypeVo.setEtIaf(new BigDecimal(salaryDataInfo.get("GJJ").toString()));
                    monthlyLaborCostByTypeVo.setEtLoi(new BigDecimal(salaryDataInfo.get("UNEMPLOY_INSURANCE").toString()));
                    monthlyLaborCostByTypeVo.setEtWelfareAmountSalaries(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByTypeVo.setEtWelfareAmountBonus(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByTypeVo.setEtWelfareAmountWeal(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByTypeVo.setEtSubtotal(new BigDecimal(salaryDataInfo.get("TOTAL").toString()));

                    monthlyLaborCostByTypeVo.setMtTotalHrmNumber(monthlyLaborCostByTypeVo.getMtTotalHrmNumber()+Integer.parseInt(salaryDataInfo.get("WORKCODE").toString()));
                    monthlyLaborCostByTypeVo.setMtTotalGrossPay(monthlyLaborCostByTypeVo.getMtTotalGrossPay().add(new BigDecimal(salaryDataInfo.get("GROSS_PAY").toString())));
                    monthlyLaborCostByTypeVo.setMtTotalAcFund(monthlyLaborCostByTypeVo.getMtTotalAcFund().add(new BigDecimal(salaryDataInfo.get("HOUSEPOVIDENT_FUND").toString())));
                    monthlyLaborCostByTypeVo.setMtTotalIaf(monthlyLaborCostByTypeVo.getMtTotalIaf().add(new BigDecimal(salaryDataInfo.get("GJJ").toString())));
                    monthlyLaborCostByTypeVo.setMtTotalLoi(monthlyLaborCostByTypeVo.getMtTotalLoi().add(new BigDecimal(salaryDataInfo.get("UNEMPLOY_INSURANCE").toString())));
                    monthlyLaborCostByTypeVo.setMtTotalWelfareAmountSalaries(monthlyLaborCostByTypeVo.getMtTotalWelfareAmountSalaries().add(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByTypeVo.setMtTotalWelfareAmountBonus(monthlyLaborCostByTypeVo.getMtTotalWelfareAmountBonus().add(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByTypeVo.setMtTotalWelfareAmountWeal(monthlyLaborCostByTypeVo.getMtTotalWelfareAmountWeal().add(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByTypeVo.setMtTotalSubtotal(monthlyLaborCostByTypeVo.getMtTotalSubtotal().add(new BigDecimal(salaryDataInfo.get("TOTAL").toString())));

                    monthlyLaborCostByTypeVo.setTotalHrmNumber(monthlyLaborCostByTypeVo.getTotalHrmNumber()+Integer.parseInt(salaryDataInfo.get("WORKCODE").toString()));
                    monthlyLaborCostByTypeVo.setTotalGrossPay(monthlyLaborCostByTypeVo.getTotalGrossPay().add(new BigDecimal(salaryDataInfo.get("GROSS_PAY").toString())));
                    monthlyLaborCostByTypeVo.setTotalAcFund(monthlyLaborCostByTypeVo.getTotalAcFund().add(new BigDecimal(salaryDataInfo.get("HOUSEPOVIDENT_FUND").toString())));
                    monthlyLaborCostByTypeVo.setTotalIaf(monthlyLaborCostByTypeVo.getTotalIaf().add(new BigDecimal(salaryDataInfo.get("GJJ").toString())));
                    monthlyLaborCostByTypeVo.setTotalLoi(monthlyLaborCostByTypeVo.getTotalLoi().add(new BigDecimal(salaryDataInfo.get("UNEMPLOY_INSURANCE").toString())));
                    monthlyLaborCostByTypeVo.setTotalWelfareAmountSalaries(monthlyLaborCostByTypeVo.getTotalWelfareAmountSalaries().add(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByTypeVo.setTotalWelfareAmountBonus(monthlyLaborCostByTypeVo.getTotalWelfareAmountBonus().add(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByTypeVo.setTotalWelfareAmountWeal(monthlyLaborCostByTypeVo.getTotalWelfareAmountWeal().add(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByTypeVo.setTotalSubtotal(monthlyLaborCostByTypeVo.getTotalSubtotal().add(new BigDecimal(salaryDataInfo.get("TOTAL").toString())));
                }else if(salaryDataInfo.get("TYPE_ID").toString().equals("02")&&salaryDataInfo.get("MAINID").equals(salaryReportConfig.getId())){
                    monthlyLaborCostByTypeVo.setMtHrmNumber(Integer.parseInt(salaryDataInfo.get("WORKCODE").toString()));
                    monthlyLaborCostByTypeVo.setMtGrossPay(new BigDecimal(salaryDataInfo.get("GROSS_PAY").toString()));
                    monthlyLaborCostByTypeVo.setMtAcFund(new BigDecimal(salaryDataInfo.get("HOUSEPOVIDENT_FUND").toString()));
                    monthlyLaborCostByTypeVo.setMtIaf(new BigDecimal(salaryDataInfo.get("GJJ").toString()));
                    monthlyLaborCostByTypeVo.setMtLoi(new BigDecimal(salaryDataInfo.get("UNEMPLOY_INSURANCE").toString()));
                    monthlyLaborCostByTypeVo.setMtWelfareAmountSalaries(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByTypeVo.setMtWelfareAmountBonus(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByTypeVo.setMtWelfareAmountWeal(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByTypeVo.setMtSubtotal(new BigDecimal(salaryDataInfo.get("TOTAL").toString()));

                    monthlyLaborCostByTypeVo.setMtTotalHrmNumber(monthlyLaborCostByTypeVo.getMtTotalHrmNumber()+Integer.parseInt(salaryDataInfo.get("WORKCODE").toString()));
                    monthlyLaborCostByTypeVo.setMtTotalGrossPay(monthlyLaborCostByTypeVo.getMtTotalGrossPay().add(new BigDecimal(salaryDataInfo.get("GROSS_PAY").toString())));
                    monthlyLaborCostByTypeVo.setMtTotalAcFund(monthlyLaborCostByTypeVo.getMtTotalAcFund().add(new BigDecimal(salaryDataInfo.get("HOUSEPOVIDENT_FUND").toString())));
                    monthlyLaborCostByTypeVo.setMtTotalIaf(monthlyLaborCostByTypeVo.getMtTotalIaf().add(new BigDecimal(salaryDataInfo.get("GJJ").toString())));
                    monthlyLaborCostByTypeVo.setMtTotalLoi(monthlyLaborCostByTypeVo.getMtTotalLoi().add(new BigDecimal(salaryDataInfo.get("UNEMPLOY_INSURANCE").toString())));
                    monthlyLaborCostByTypeVo.setMtTotalWelfareAmountSalaries(monthlyLaborCostByTypeVo.getMtTotalWelfareAmountSalaries().add(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByTypeVo.setMtTotalWelfareAmountBonus(monthlyLaborCostByTypeVo.getMtTotalWelfareAmountBonus().add(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByTypeVo.setMtTotalWelfareAmountWeal(monthlyLaborCostByTypeVo.getMtTotalWelfareAmountWeal().add(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByTypeVo.setMtTotalSubtotal(monthlyLaborCostByTypeVo.getMtTotalSubtotal().add(new BigDecimal(salaryDataInfo.get("TOTAL").toString())));

                    monthlyLaborCostByTypeVo.setTotalHrmNumber(monthlyLaborCostByTypeVo.getTotalHrmNumber()+Integer.parseInt(salaryDataInfo.get("WORKCODE").toString()));
                    monthlyLaborCostByTypeVo.setTotalGrossPay(monthlyLaborCostByTypeVo.getTotalGrossPay().add(new BigDecimal(salaryDataInfo.get("GROSS_PAY").toString())));
                    monthlyLaborCostByTypeVo.setTotalAcFund(monthlyLaborCostByTypeVo.getTotalAcFund().add(new BigDecimal(salaryDataInfo.get("HOUSEPOVIDENT_FUND").toString())));
                    monthlyLaborCostByTypeVo.setTotalIaf(monthlyLaborCostByTypeVo.getTotalIaf().add(new BigDecimal(salaryDataInfo.get("GJJ").toString())));
                    monthlyLaborCostByTypeVo.setTotalLoi(monthlyLaborCostByTypeVo.getTotalLoi().add(new BigDecimal(salaryDataInfo.get("UNEMPLOY_INSURANCE").toString())));
                    monthlyLaborCostByTypeVo.setTotalWelfareAmountSalaries(monthlyLaborCostByTypeVo.getTotalWelfareAmountSalaries().add(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByTypeVo.setTotalWelfareAmountBonus(monthlyLaborCostByTypeVo.getTotalWelfareAmountBonus().add(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByTypeVo.setTotalWelfareAmountWeal(monthlyLaborCostByTypeVo.getTotalWelfareAmountWeal().add(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByTypeVo.setTotalSubtotal(monthlyLaborCostByTypeVo.getTotalSubtotal().add(new BigDecimal(salaryDataInfo.get("TOTAL").toString())));
                }else if(salaryDataInfo.get("TYPE_ID").toString().equals("03")&&salaryDataInfo.get("MAINID").equals(salaryReportConfig.getId())){
                    monthlyLaborCostByTypeVo.setProdHrmNumber(Integer.parseInt(salaryDataInfo.get("WORKCODE").toString()));
                    monthlyLaborCostByTypeVo.setProdGrossPay(new BigDecimal(salaryDataInfo.get("GROSS_PAY").toString()));
                    monthlyLaborCostByTypeVo.setProdAcFund(new BigDecimal(salaryDataInfo.get("HOUSEPOVIDENT_FUND").toString()));
                    monthlyLaborCostByTypeVo.setProdIaf(new BigDecimal(salaryDataInfo.get("GJJ").toString()));
                    monthlyLaborCostByTypeVo.setProdLoi(new BigDecimal(salaryDataInfo.get("UNEMPLOY_INSURANCE").toString()));
                    monthlyLaborCostByTypeVo.setProdWelfareAmountSalaries(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByTypeVo.setProdWelfareAmountBonus(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByTypeVo.setProdWelfareAmountWeal(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByTypeVo.setProdSubtotal(new BigDecimal(salaryDataInfo.get("TOTAL").toString()));

                    monthlyLaborCostByTypeVo.setTotalHrmNumber(monthlyLaborCostByTypeVo.getTotalHrmNumber()+Integer.parseInt(salaryDataInfo.get("WORKCODE").toString()));
                    monthlyLaborCostByTypeVo.setTotalGrossPay(monthlyLaborCostByTypeVo.getTotalGrossPay().add(new BigDecimal(salaryDataInfo.get("GROSS_PAY").toString())));
                    monthlyLaborCostByTypeVo.setTotalAcFund(monthlyLaborCostByTypeVo.getTotalAcFund().add(new BigDecimal(salaryDataInfo.get("HOUSEPOVIDENT_FUND").toString())));
                    monthlyLaborCostByTypeVo.setTotalIaf(monthlyLaborCostByTypeVo.getTotalIaf().add(new BigDecimal(salaryDataInfo.get("GJJ").toString())));
                    monthlyLaborCostByTypeVo.setTotalLoi(monthlyLaborCostByTypeVo.getTotalLoi().add(new BigDecimal(salaryDataInfo.get("UNEMPLOY_INSURANCE").toString())));
                    monthlyLaborCostByTypeVo.setTotalWelfareAmountSalaries(monthlyLaborCostByTypeVo.getTotalWelfareAmountSalaries().add(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByTypeVo.setTotalWelfareAmountBonus(monthlyLaborCostByTypeVo.getTotalWelfareAmountBonus().add(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByTypeVo.setTotalWelfareAmountWeal(monthlyLaborCostByTypeVo.getTotalWelfareAmountWeal().add(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByTypeVo.setTotalSubtotal(monthlyLaborCostByTypeVo.getTotalSubtotal().add(new BigDecimal(salaryDataInfo.get("TOTAL").toString())));
                }else if(salaryDataInfo.get("TYPE_ID").toString().equals("04")&&salaryDataInfo.get("MAINID").equals(salaryReportConfig.getId())){
                    monthlyLaborCostByTypeVo.setPsHrmNumber(Integer.parseInt(salaryDataInfo.get("WORKCODE").toString()));
                    monthlyLaborCostByTypeVo.setPsGrossPay(new BigDecimal(salaryDataInfo.get("GROSS_PAY").toString()));
                    monthlyLaborCostByTypeVo.setPsAcFund(new BigDecimal(salaryDataInfo.get("HOUSEPOVIDENT_FUND").toString()));
                    monthlyLaborCostByTypeVo.setPsIaf(new BigDecimal(salaryDataInfo.get("GJJ").toString()));
                    monthlyLaborCostByTypeVo.setPsLoi(new BigDecimal(salaryDataInfo.get("UNEMPLOY_INSURANCE").toString()));
                    monthlyLaborCostByTypeVo.setPsWelfareAmountSalaries(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByTypeVo.setPsWelfareAmountBonus(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByTypeVo.setPsWelfareAmountWeal(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByTypeVo.setPsSubtotal(new BigDecimal(salaryDataInfo.get("TOTAL").toString()));

                    monthlyLaborCostByTypeVo.setTotalHrmNumber(monthlyLaborCostByTypeVo.getTotalHrmNumber()+Integer.parseInt(salaryDataInfo.get("WORKCODE").toString()));
                    monthlyLaborCostByTypeVo.setTotalGrossPay(monthlyLaborCostByTypeVo.getTotalGrossPay().add(new BigDecimal(salaryDataInfo.get("GROSS_PAY").toString())));
                    monthlyLaborCostByTypeVo.setTotalAcFund(monthlyLaborCostByTypeVo.getTotalAcFund().add(new BigDecimal(salaryDataInfo.get("HOUSEPOVIDENT_FUND").toString())));
                    monthlyLaborCostByTypeVo.setTotalIaf(monthlyLaborCostByTypeVo.getTotalIaf().add(new BigDecimal(salaryDataInfo.get("GJJ").toString())));
                    monthlyLaborCostByTypeVo.setTotalLoi(monthlyLaborCostByTypeVo.getTotalLoi().add(new BigDecimal(salaryDataInfo.get("UNEMPLOY_INSURANCE").toString())));
                    monthlyLaborCostByTypeVo.setTotalWelfareAmountSalaries(monthlyLaborCostByTypeVo.getTotalWelfareAmountSalaries().add(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByTypeVo.setTotalWelfareAmountBonus(monthlyLaborCostByTypeVo.getTotalWelfareAmountBonus().add(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByTypeVo.setTotalWelfareAmountWeal(monthlyLaborCostByTypeVo.getTotalWelfareAmountWeal().add(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByTypeVo.setTotalSubtotal(monthlyLaborCostByTypeVo.getTotalSubtotal().add(new BigDecimal(salaryDataInfo.get("TOTAL").toString())));
                }else if(salaryDataInfo.get("TYPE_ID").toString().equals("05")&&salaryDataInfo.get("MAINID").equals(salaryReportConfig.getId())){
                    monthlyLaborCostByTypeVo.setLogisHrmNumber(Integer.parseInt(salaryDataInfo.get("WORKCODE").toString()));
                    monthlyLaborCostByTypeVo.setLogisGrossPay(new BigDecimal(salaryDataInfo.get("GROSS_PAY").toString()));
                    monthlyLaborCostByTypeVo.setLogisAcFund(new BigDecimal(salaryDataInfo.get("HOUSEPOVIDENT_FUND").toString()));
                    monthlyLaborCostByTypeVo.setLogisIaf(new BigDecimal(salaryDataInfo.get("GJJ").toString()));
                    monthlyLaborCostByTypeVo.setLogisLoi(new BigDecimal(salaryDataInfo.get("UNEMPLOY_INSURANCE").toString()));
                    monthlyLaborCostByTypeVo.setLogisWelfareAmountSalaries(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByTypeVo.setLogisWelfareAmountBonus(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByTypeVo.setLogisWelfareAmountWeal(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByTypeVo.setLogisSubtotal(new BigDecimal(salaryDataInfo.get("TOTAL").toString()));

                    monthlyLaborCostByTypeVo.setTotalHrmNumber(monthlyLaborCostByTypeVo.getTotalHrmNumber()+Integer.parseInt(salaryDataInfo.get("WORKCODE").toString()));
                    monthlyLaborCostByTypeVo.setTotalGrossPay(monthlyLaborCostByTypeVo.getTotalGrossPay().add(new BigDecimal(salaryDataInfo.get("GROSS_PAY").toString())));
                    monthlyLaborCostByTypeVo.setTotalAcFund(monthlyLaborCostByTypeVo.getTotalAcFund().add(new BigDecimal(salaryDataInfo.get("HOUSEPOVIDENT_FUND").toString())));
                    monthlyLaborCostByTypeVo.setTotalIaf(monthlyLaborCostByTypeVo.getTotalIaf().add(new BigDecimal(salaryDataInfo.get("GJJ").toString())));
                    monthlyLaborCostByTypeVo.setTotalLoi(monthlyLaborCostByTypeVo.getTotalLoi().add(new BigDecimal(salaryDataInfo.get("UNEMPLOY_INSURANCE").toString())));
                    monthlyLaborCostByTypeVo.setTotalWelfareAmountSalaries(monthlyLaborCostByTypeVo.getTotalWelfareAmountSalaries().add(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByTypeVo.setTotalWelfareAmountBonus(monthlyLaborCostByTypeVo.getTotalWelfareAmountBonus().add(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByTypeVo.setTotalWelfareAmountWeal(monthlyLaborCostByTypeVo.getTotalWelfareAmountWeal().add(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByTypeVo.setTotalSubtotal(monthlyLaborCostByTypeVo.getTotalSubtotal().add(new BigDecimal(salaryDataInfo.get("TOTAL").toString())));
                }else if(salaryDataInfo.get("TYPE_ID").toString().equals("06")&&salaryDataInfo.get("MAINID").equals(salaryReportConfig.getId())){
                    monthlyLaborCostByTypeVo.setQiHrmNumber(Integer.parseInt(salaryDataInfo.get("WORKCODE").toString()));
                    monthlyLaborCostByTypeVo.setQiGrossPay(new BigDecimal(salaryDataInfo.get("GROSS_PAY").toString()));
                    monthlyLaborCostByTypeVo.setQiAcFund(new BigDecimal(salaryDataInfo.get("HOUSEPOVIDENT_FUND").toString()));
                    monthlyLaborCostByTypeVo.setQiIaf(new BigDecimal(salaryDataInfo.get("GJJ").toString()));
                    monthlyLaborCostByTypeVo.setQiLoi(new BigDecimal(salaryDataInfo.get("UNEMPLOY_INSURANCE").toString()));
                    monthlyLaborCostByTypeVo.setQiWelfareAmountSalaries(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByTypeVo.setQiWelfareAmountBonus(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByTypeVo.setQiWelfareAmountWeal(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByTypeVo.setQiSubtotal(new BigDecimal(salaryDataInfo.get("TOTAL").toString()));

                    monthlyLaborCostByTypeVo.setTotalHrmNumber(monthlyLaborCostByTypeVo.getTotalHrmNumber()+Integer.parseInt(salaryDataInfo.get("WORKCODE").toString()));
                    monthlyLaborCostByTypeVo.setTotalGrossPay(monthlyLaborCostByTypeVo.getTotalGrossPay().add(new BigDecimal(salaryDataInfo.get("GROSS_PAY").toString())));
                    monthlyLaborCostByTypeVo.setTotalAcFund(monthlyLaborCostByTypeVo.getTotalAcFund().add(new BigDecimal(salaryDataInfo.get("HOUSEPOVIDENT_FUND").toString())));
                    monthlyLaborCostByTypeVo.setTotalIaf(monthlyLaborCostByTypeVo.getTotalIaf().add(new BigDecimal(salaryDataInfo.get("GJJ").toString())));
                    monthlyLaborCostByTypeVo.setTotalLoi(monthlyLaborCostByTypeVo.getTotalLoi().add(new BigDecimal(salaryDataInfo.get("UNEMPLOY_INSURANCE").toString())));
                    monthlyLaborCostByTypeVo.setTotalWelfareAmountSalaries(monthlyLaborCostByTypeVo.getTotalWelfareAmountSalaries().add(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByTypeVo.setTotalWelfareAmountBonus(monthlyLaborCostByTypeVo.getTotalWelfareAmountBonus().add(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByTypeVo.setTotalWelfareAmountWeal(monthlyLaborCostByTypeVo.getTotalWelfareAmountWeal().add(new BigDecimal(salaryDataInfo.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByTypeVo.setTotalSubtotal(monthlyLaborCostByTypeVo.getTotalSubtotal().add(new BigDecimal(salaryDataInfo.get("TOTAL").toString())));
                }
            }
            monthlyLaborCostByTypeVoList.add(monthlyLaborCostByTypeVo);
        }
        return monthlyLaborCostByTypeVoList;
    }

    @Override
    public List<MonthlyLaborCostByDeptVo> getTypeLaborCostByDate(String year, Float rate, String site, String tabId,String typeIds) {
        List<MonthlyLaborCostByDeptVo> resultDataList = new ArrayList<MonthlyLaborCostByDeptVo>();
        List<SalaryReportConfig> salaryReportConfigList=salaryReportConfigMapper.getSalaryReportConfig(site,tabId);
        SalaryReportConfig totalConfig = new SalaryReportConfig();
        totalConfig.setId(new BigDecimal("-1"));
        totalConfig.setDepartName("合计");
        salaryReportConfigList.add(totalConfig);
        List<Map<String,Object>> resultByDeptList = personnelSalaryMapper.getTypeLaborCostByDate(year,rate,site,tabId,typeIds);
        List<Map<String,Object>> resultTotalList = personnelSalaryMapper.getTypeLaborTotalCostByDate(year,rate,site,tabId,typeIds);
        resultByDeptList.addAll(resultTotalList);

        for(SalaryReportConfig salaryReportConfig:salaryReportConfigList){
            MonthlyLaborCostByDeptVo monthlyLaborCostByDeptVo = new MonthlyLaborCostByDeptVo();
            monthlyLaborCostByDeptVo.setDepartName(salaryReportConfig.getDepartName());
            for(Map<String,Object> result:resultByDeptList){
                if(result.get("SALARY_DATE").equals(year+"-01")&&salaryReportConfig.getId().equals(result.get("MAINID"))){
                    monthlyLaborCostByDeptVo.setJanHrmNumber(Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setJanGrossPay(new BigDecimal(result.get("GROSS_PAY").toString()));
                    monthlyLaborCostByDeptVo.setJanWelfareAmountWeal(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByDeptVo.setJanIaf(new BigDecimal(result.get("GJJ").toString()));
                    monthlyLaborCostByDeptVo.setJanWelfareAmountSalaries(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByDeptVo.setJanWelfareAmountBonus(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByDeptVo.setJanSubtotal(new BigDecimal(result.get("TOTAL").toString()));
                    //半年
                    monthlyLaborCostByDeptVo.setHalfHrmNumber(monthlyLaborCostByDeptVo.getHalfHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setHalfGrossPay(monthlyLaborCostByDeptVo.getHalfGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountWeal(monthlyLaborCostByDeptVo.getHalfWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setHalfIaf(monthlyLaborCostByDeptVo.getHalfIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountSalaries(monthlyLaborCostByDeptVo.getHalfWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountBonus(monthlyLaborCostByDeptVo.getHalfWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setHalfSubtotal(monthlyLaborCostByDeptVo.getHalfSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                    //整年
                    monthlyLaborCostByDeptVo.setWholeHrmNumber(monthlyLaborCostByDeptVo.getWholeHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setWholeGrossPay(monthlyLaborCostByDeptVo.getWholeGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountWeal(monthlyLaborCostByDeptVo.getWholeWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setWholeIaf(monthlyLaborCostByDeptVo.getWholeIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountSalaries(monthlyLaborCostByDeptVo.getWholeWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountBonus(monthlyLaborCostByDeptVo.getWholeWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setWholeSubtotal(monthlyLaborCostByDeptVo.getWholeSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                }else if(result.get("SALARY_DATE").equals(year+"-02")&&salaryReportConfig.getId().equals(result.get("MAINID"))){
                    monthlyLaborCostByDeptVo.setFebHrmNumber(Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setFebGrossPay(new BigDecimal(result.get("GROSS_PAY").toString()));
                    monthlyLaborCostByDeptVo.setFebWelfareAmountWeal(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByDeptVo.setFebIaf(new BigDecimal(result.get("GJJ").toString()));
                    monthlyLaborCostByDeptVo.setFebWelfareAmountSalaries(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByDeptVo.setFebWelfareAmountBonus(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByDeptVo.setFebSubtotal(new BigDecimal(result.get("TOTAL").toString()));
                    //半年
                    monthlyLaborCostByDeptVo.setHalfHrmNumber(monthlyLaborCostByDeptVo.getHalfHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setHalfGrossPay(monthlyLaborCostByDeptVo.getHalfGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountWeal(monthlyLaborCostByDeptVo.getHalfWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setHalfIaf(monthlyLaborCostByDeptVo.getHalfIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountSalaries(monthlyLaborCostByDeptVo.getHalfWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountBonus(monthlyLaborCostByDeptVo.getHalfWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setHalfSubtotal(monthlyLaborCostByDeptVo.getHalfSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                    //整年
                    monthlyLaborCostByDeptVo.setWholeHrmNumber(monthlyLaborCostByDeptVo.getWholeHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setWholeGrossPay(monthlyLaborCostByDeptVo.getWholeGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountWeal(monthlyLaborCostByDeptVo.getWholeWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setWholeIaf(monthlyLaborCostByDeptVo.getWholeIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountSalaries(monthlyLaborCostByDeptVo.getWholeWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountBonus(monthlyLaborCostByDeptVo.getWholeWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setWholeSubtotal(monthlyLaborCostByDeptVo.getWholeSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                }else if(result.get("SALARY_DATE").equals(year+"-03")&&salaryReportConfig.getId().equals(result.get("MAINID"))){
                    monthlyLaborCostByDeptVo.setMarHrmNumber(Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setMarGrossPay(new BigDecimal(result.get("GROSS_PAY").toString()));
                    monthlyLaborCostByDeptVo.setMarWelfareAmountWeal(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByDeptVo.setMarIaf(new BigDecimal(result.get("GJJ").toString()));
                    monthlyLaborCostByDeptVo.setMarWelfareAmountSalaries(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByDeptVo.setMarWelfareAmountBonus(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByDeptVo.setMarSubtotal(new BigDecimal(result.get("TOTAL").toString()));
                    //半年
                    monthlyLaborCostByDeptVo.setHalfHrmNumber(monthlyLaborCostByDeptVo.getHalfHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setHalfGrossPay(monthlyLaborCostByDeptVo.getHalfGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountWeal(monthlyLaborCostByDeptVo.getHalfWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setHalfIaf(monthlyLaborCostByDeptVo.getHalfIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountSalaries(monthlyLaborCostByDeptVo.getHalfWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountBonus(monthlyLaborCostByDeptVo.getHalfWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setHalfSubtotal(monthlyLaborCostByDeptVo.getHalfSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                    //整年
                    monthlyLaborCostByDeptVo.setWholeHrmNumber(monthlyLaborCostByDeptVo.getWholeHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setWholeGrossPay(monthlyLaborCostByDeptVo.getWholeGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountWeal(monthlyLaborCostByDeptVo.getWholeWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setWholeIaf(monthlyLaborCostByDeptVo.getWholeIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountSalaries(monthlyLaborCostByDeptVo.getWholeWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountBonus(monthlyLaborCostByDeptVo.getWholeWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setWholeSubtotal(monthlyLaborCostByDeptVo.getWholeSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                }else if(result.get("SALARY_DATE").equals(year+"-04")&&salaryReportConfig.getId().equals(result.get("MAINID"))){
                    monthlyLaborCostByDeptVo.setAprHrmNumber(Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setAprGrossPay(new BigDecimal(result.get("GROSS_PAY").toString()));
                    monthlyLaborCostByDeptVo.setAprWelfareAmountWeal(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByDeptVo.setAprIaf(new BigDecimal(result.get("GJJ").toString()));
                    monthlyLaborCostByDeptVo.setAprWelfareAmountSalaries(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByDeptVo.setAprWelfareAmountBonus(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByDeptVo.setAprSubtotal(new BigDecimal(result.get("TOTAL").toString()));
                    //半年
                    monthlyLaborCostByDeptVo.setHalfHrmNumber(monthlyLaborCostByDeptVo.getHalfHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setHalfGrossPay(monthlyLaborCostByDeptVo.getHalfGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountWeal(monthlyLaborCostByDeptVo.getHalfWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setHalfIaf(monthlyLaborCostByDeptVo.getHalfIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountSalaries(monthlyLaborCostByDeptVo.getHalfWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountBonus(monthlyLaborCostByDeptVo.getHalfWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setHalfSubtotal(monthlyLaborCostByDeptVo.getHalfSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                    //整年
                    monthlyLaborCostByDeptVo.setWholeHrmNumber(monthlyLaborCostByDeptVo.getWholeHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setWholeGrossPay(monthlyLaborCostByDeptVo.getWholeGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountWeal(monthlyLaborCostByDeptVo.getWholeWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setWholeIaf(monthlyLaborCostByDeptVo.getWholeIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountSalaries(monthlyLaborCostByDeptVo.getWholeWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountBonus(monthlyLaborCostByDeptVo.getWholeWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setWholeSubtotal(monthlyLaborCostByDeptVo.getWholeSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                }else if(result.get("SALARY_DATE").equals(year+"-05")&&salaryReportConfig.getId().equals(result.get("MAINID"))){
                    monthlyLaborCostByDeptVo.setMayHrmNumber(Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setMayGrossPay(new BigDecimal(result.get("GROSS_PAY").toString()));
                    monthlyLaborCostByDeptVo.setMayWelfareAmountWeal(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByDeptVo.setMayIaf(new BigDecimal(result.get("GJJ").toString()));
                    monthlyLaborCostByDeptVo.setMayWelfareAmountSalaries(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByDeptVo.setMayWelfareAmountBonus(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByDeptVo.setMaySubtotal(new BigDecimal(result.get("TOTAL").toString()));
                    //半年
                    monthlyLaborCostByDeptVo.setHalfHrmNumber(monthlyLaborCostByDeptVo.getHalfHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setHalfGrossPay(monthlyLaborCostByDeptVo.getHalfGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountWeal(monthlyLaborCostByDeptVo.getHalfWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setHalfIaf(monthlyLaborCostByDeptVo.getHalfIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountSalaries(monthlyLaborCostByDeptVo.getHalfWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountBonus(monthlyLaborCostByDeptVo.getHalfWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setHalfSubtotal(monthlyLaborCostByDeptVo.getHalfSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                    //整年
                    monthlyLaborCostByDeptVo.setWholeHrmNumber(monthlyLaborCostByDeptVo.getWholeHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setWholeGrossPay(monthlyLaborCostByDeptVo.getWholeGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountWeal(monthlyLaborCostByDeptVo.getWholeWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setWholeIaf(monthlyLaborCostByDeptVo.getWholeIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountSalaries(monthlyLaborCostByDeptVo.getWholeWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountBonus(monthlyLaborCostByDeptVo.getWholeWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setWholeSubtotal(monthlyLaborCostByDeptVo.getWholeSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                }else if(result.get("SALARY_DATE").equals(year+"-06")&&salaryReportConfig.getId().equals(result.get("MAINID"))){
                    monthlyLaborCostByDeptVo.setJunHrmNumber(Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setJunGrossPay(new BigDecimal(result.get("GROSS_PAY").toString()));
                    monthlyLaborCostByDeptVo.setJunWelfareAmountWeal(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByDeptVo.setJunIaf(new BigDecimal(result.get("GJJ").toString()));
                    monthlyLaborCostByDeptVo.setJunWelfareAmountSalaries(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByDeptVo.setJunWelfareAmountBonus(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByDeptVo.setJunSubtotal(new BigDecimal(result.get("TOTAL").toString()));
                    //半年
                    monthlyLaborCostByDeptVo.setHalfHrmNumber(monthlyLaborCostByDeptVo.getHalfHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setHalfGrossPay(monthlyLaborCostByDeptVo.getHalfGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountWeal(monthlyLaborCostByDeptVo.getHalfWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setHalfIaf(monthlyLaborCostByDeptVo.getHalfIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountSalaries(monthlyLaborCostByDeptVo.getHalfWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setHalfWelfareAmountBonus(monthlyLaborCostByDeptVo.getHalfWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setHalfSubtotal(monthlyLaborCostByDeptVo.getHalfSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                    //整年
                    monthlyLaborCostByDeptVo.setWholeHrmNumber(monthlyLaborCostByDeptVo.getWholeHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setWholeGrossPay(monthlyLaborCostByDeptVo.getWholeGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountWeal(monthlyLaborCostByDeptVo.getWholeWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setWholeIaf(monthlyLaborCostByDeptVo.getWholeIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountSalaries(monthlyLaborCostByDeptVo.getWholeWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountBonus(monthlyLaborCostByDeptVo.getWholeWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setWholeSubtotal(monthlyLaborCostByDeptVo.getWholeSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                }else if(result.get("SALARY_DATE").equals(year+"-07")&&salaryReportConfig.getId().equals(result.get("MAINID"))){
                    monthlyLaborCostByDeptVo.setJulHrmNumber(Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setJulGrossPay(new BigDecimal(result.get("GROSS_PAY").toString()));
                    monthlyLaborCostByDeptVo.setJulWelfareAmountWeal(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByDeptVo.setJulIaf(new BigDecimal(result.get("GJJ").toString()));
                    monthlyLaborCostByDeptVo.setJulWelfareAmountSalaries(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByDeptVo.setJulWelfareAmountBonus(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByDeptVo.setJulSubtotal(new BigDecimal(result.get("TOTAL").toString()));
                    //整年
                    monthlyLaborCostByDeptVo.setWholeHrmNumber(monthlyLaborCostByDeptVo.getWholeHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setWholeGrossPay(monthlyLaborCostByDeptVo.getWholeGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountWeal(monthlyLaborCostByDeptVo.getWholeWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setWholeIaf(monthlyLaborCostByDeptVo.getWholeIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountSalaries(monthlyLaborCostByDeptVo.getWholeWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountBonus(monthlyLaborCostByDeptVo.getWholeWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setWholeSubtotal(monthlyLaborCostByDeptVo.getWholeSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                }else if(result.get("SALARY_DATE").equals(year+"-08")&&salaryReportConfig.getId().equals(result.get("MAINID"))){
                    monthlyLaborCostByDeptVo.setAugHrmNumber(Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setAugGrossPay(new BigDecimal(result.get("GROSS_PAY").toString()));
                    monthlyLaborCostByDeptVo.setAugWelfareAmountWeal(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByDeptVo.setAugIaf(new BigDecimal(result.get("GJJ").toString()));
                    monthlyLaborCostByDeptVo.setAugWelfareAmountSalaries(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByDeptVo.setAugWelfareAmountBonus(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByDeptVo.setAugSubtotal(new BigDecimal(result.get("TOTAL").toString()));

                    //整年
                    monthlyLaborCostByDeptVo.setWholeHrmNumber(monthlyLaborCostByDeptVo.getWholeHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setWholeGrossPay(monthlyLaborCostByDeptVo.getWholeGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountWeal(monthlyLaborCostByDeptVo.getWholeWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setWholeIaf(monthlyLaborCostByDeptVo.getWholeIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountSalaries(monthlyLaborCostByDeptVo.getWholeWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountBonus(monthlyLaborCostByDeptVo.getWholeWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setWholeSubtotal(monthlyLaborCostByDeptVo.getWholeSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                }else if(result.get("SALARY_DATE").equals(year+"-09")&&salaryReportConfig.getId().equals(result.get("MAINID"))){
                    monthlyLaborCostByDeptVo.setSepHrmNumber(Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setSepGrossPay(new BigDecimal(result.get("GROSS_PAY").toString()));
                    monthlyLaborCostByDeptVo.setSepWelfareAmountWeal(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByDeptVo.setSepIaf(new BigDecimal(result.get("GJJ").toString()));
                    monthlyLaborCostByDeptVo.setSepWelfareAmountSalaries(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByDeptVo.setSepWelfareAmountBonus(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByDeptVo.setSepSubtotal(new BigDecimal(result.get("TOTAL").toString()));

                    //整年
                    monthlyLaborCostByDeptVo.setWholeHrmNumber(monthlyLaborCostByDeptVo.getWholeHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setWholeGrossPay(monthlyLaborCostByDeptVo.getWholeGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountWeal(monthlyLaborCostByDeptVo.getWholeWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setWholeIaf(monthlyLaborCostByDeptVo.getWholeIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountSalaries(monthlyLaborCostByDeptVo.getWholeWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountBonus(monthlyLaborCostByDeptVo.getWholeWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setWholeSubtotal(monthlyLaborCostByDeptVo.getWholeSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                }else if(result.get("SALARY_DATE").equals(year+"-10")&&salaryReportConfig.getId().equals(result.get("MAINID"))){
                    monthlyLaborCostByDeptVo.setOctHrmNumber(Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setOctGrossPay(new BigDecimal(result.get("GROSS_PAY").toString()));
                    monthlyLaborCostByDeptVo.setOctWelfareAmountWeal(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByDeptVo.setOctIaf(new BigDecimal(result.get("GJJ").toString()));
                    monthlyLaborCostByDeptVo.setOctWelfareAmountSalaries(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByDeptVo.setOctWelfareAmountBonus(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByDeptVo.setOctSubtotal(new BigDecimal(result.get("TOTAL").toString()));

                    //整年
                    monthlyLaborCostByDeptVo.setWholeHrmNumber(monthlyLaborCostByDeptVo.getWholeHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setWholeGrossPay(monthlyLaborCostByDeptVo.getWholeGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountWeal(monthlyLaborCostByDeptVo.getWholeWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setWholeIaf(monthlyLaborCostByDeptVo.getWholeIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountSalaries(monthlyLaborCostByDeptVo.getWholeWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountBonus(monthlyLaborCostByDeptVo.getWholeWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setWholeSubtotal(monthlyLaborCostByDeptVo.getWholeSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                }else if(result.get("SALARY_DATE").equals(year+"-11")&&salaryReportConfig.getId().equals(result.get("MAINID"))){
                    monthlyLaborCostByDeptVo.setNovHrmNumber(Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setNovGrossPay(new BigDecimal(result.get("GROSS_PAY").toString()));
                    monthlyLaborCostByDeptVo.setNovWelfareAmountWeal(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByDeptVo.setNovIaf(new BigDecimal(result.get("GJJ").toString()));
                    monthlyLaborCostByDeptVo.setNovWelfareAmountSalaries(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByDeptVo.setNovWelfareAmountBonus(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByDeptVo.setNovSubtotal(new BigDecimal(result.get("TOTAL").toString()));

                    //整年
                    monthlyLaborCostByDeptVo.setWholeHrmNumber(monthlyLaborCostByDeptVo.getWholeHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setWholeGrossPay(monthlyLaborCostByDeptVo.getWholeGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountWeal(monthlyLaborCostByDeptVo.getWholeWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setWholeIaf(monthlyLaborCostByDeptVo.getWholeIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountSalaries(monthlyLaborCostByDeptVo.getWholeWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountBonus(monthlyLaborCostByDeptVo.getWholeWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setWholeSubtotal(monthlyLaborCostByDeptVo.getWholeSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                }else if(result.get("SALARY_DATE").equals(year+"-12")&&salaryReportConfig.getId().equals(result.get("MAINID"))){
                    monthlyLaborCostByDeptVo.setDecHrmNumber(Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setDecGrossPay(new BigDecimal(result.get("GROSS_PAY").toString()));
                    monthlyLaborCostByDeptVo.setDecWelfareAmountWeal(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString()));
                    monthlyLaborCostByDeptVo.setDecIaf(new BigDecimal(result.get("GJJ").toString()));
                    monthlyLaborCostByDeptVo.setDecWelfareAmountSalaries(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString()));
                    monthlyLaborCostByDeptVo.setDecWelfareAmountBonus(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString()));
                    monthlyLaborCostByDeptVo.setDecSubtotal(new BigDecimal(result.get("TOTAL").toString()));

                    //整年
                    monthlyLaborCostByDeptVo.setWholeHrmNumber(monthlyLaborCostByDeptVo.getWholeHrmNumber()+Integer.parseInt(result.get("WORKCODE").toString()));
                    monthlyLaborCostByDeptVo.setWholeGrossPay(monthlyLaborCostByDeptVo.getWholeGrossPay().add(new BigDecimal(result.get("GROSS_PAY").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountWeal(monthlyLaborCostByDeptVo.getWholeWelfareAmountWeal().add(new BigDecimal(result.get("WELFARE_AMOUNT_WEAL").toString())));
                    monthlyLaborCostByDeptVo.setWholeIaf(monthlyLaborCostByDeptVo.getWholeIaf().add(new BigDecimal(result.get("GJJ").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountSalaries(monthlyLaborCostByDeptVo.getWholeWelfareAmountSalaries().add(new BigDecimal(result.get("WELFARE_AMOUNT_SALARIES").toString())));
                    monthlyLaborCostByDeptVo.setWholeWelfareAmountBonus(monthlyLaborCostByDeptVo.getWholeWelfareAmountBonus().add(new BigDecimal(result.get("WELFARE_AMOUNT_BONUS").toString())));
                    monthlyLaborCostByDeptVo.setWholeSubtotal(monthlyLaborCostByDeptVo.getWholeSubtotal().add(new BigDecimal(result.get("TOTAL").toString())));
                }
            }
            resultDataList.add(monthlyLaborCostByDeptVo);
        }
        return resultDataList;
    }

    @Override
    public List<ExcelDepartMonthVo> getMonthlyLaborCostByManufacturingDept(String year, Float rate,String site,String tabId) {
        List<ExcelDepartMonthDeptDetail> salaryDataList = personnelSalaryMapper.getMonthlyLaborCostByManufacturingDept(year,rate,site,tabId);
        List<ExcelDepartMonthDeptDetail> salarySubTotalDataList = personnelSalaryMapper.getMonthlyLaborCostByManufacturingStage(year,rate,site,tabId);
        List<SalaryReportConfig> salaryReportConfigList=salaryReportConfigMapper.getSalaryReportConfig(site,tabId);
        List<Map<String,Object>> stageGroupList= salaryReportConfigMapper.getStageGroup(site,tabId);
        List<ExcelDepartMonthVo> excelDepartMonthVoList = new ArrayList<ExcelDepartMonthVo>();

        for(int i=1;i<=14;i++){
            String month;
            if(i<=6){
              month = year+"-"+(String.valueOf(i).length()==1?"0"+String.valueOf(i):String.valueOf(i));
            }else if(i==7){
                month = "1-6月";
            }else if(i==14){
                month = "1-12月";
            }else{
                month = year+"-"+(String.valueOf(i-1).length()==1?"0"+String.valueOf(i-1):String.valueOf(i-1));
            }
            ExcelDepartMonthVo excelDepartMonthVo = new ExcelDepartMonthVo();
            //月份
            excelDepartMonthVo.setMonth(month);
            //月份子条目（分期）
            List<ExcelDepartMonthDept> excelDepartMonthDeptList= new ArrayList<ExcelDepartMonthDept>();
            //分期(开始进行分期处理)
            for(SalaryReportConfig salaryReportConfig:salaryReportConfigList){
                Boolean hasStage = false;
                for(ExcelDepartMonthDept e:excelDepartMonthDeptList){
                    if(e.getDeptName().equals(salaryReportConfig.getStage())){
                        //分期已存在
                        hasStage=true;
                    }
                }
                if(!hasStage){
                    ExcelDepartMonthDept excelDepartMonthDept = new ExcelDepartMonthDept();
                    excelDepartMonthDept.setDeptName(salaryReportConfig.getStage());
                    excelDepartMonthDeptList.add(excelDepartMonthDept);
                }
            }

            //遍历分期处理部门
            for(ExcelDepartMonthDept excelDepartMonthDept:excelDepartMonthDeptList){  //遍历当前月的分期
                List<ExcelDepartMonthDeptDetail> excelDepartMonthDeptDetailList = new ArrayList<ExcelDepartMonthDeptDetail>();
                for(SalaryReportConfig salaryReportConfig:salaryReportConfigList){  //遍历部门
                    if(excelDepartMonthDept.getDeptName().equals(salaryReportConfig.getStage())){
                        ExcelDepartMonthDeptDetail newExcelDepartMonthDeptDetail = new ExcelDepartMonthDeptDetail();
                        newExcelDepartMonthDeptDetail.setParDepartName(salaryReportConfig.getStage());
                        newExcelDepartMonthDeptDetail.setDepartId(salaryReportConfig.getId().toString());
                        newExcelDepartMonthDeptDetail.setDepartName(salaryReportConfig.getDepartName());
                        newExcelDepartMonthDeptDetail.setSalaryDate(month);
                        for(ExcelDepartMonthDeptDetail salaryData:salaryDataList){
                            if(salaryData.getSalaryDate().equals(month)&&salaryData.getDepartId().equals(salaryReportConfig.getId().toString())){
                                newExcelDepartMonthDeptDetail = salaryData;
                            }
                        }
                        excelDepartMonthDeptDetailList.add(newExcelDepartMonthDeptDetail);
                    }
                }




                for(Map<String,Object> stageGroup:stageGroupList){
                    //处理合计
                    if(excelDepartMonthDept.getDeptName().equals(stageGroup.get("STAGE").toString())&&Integer.parseInt(stageGroup.get("STAGENUMBER").toString())>1){
                        ExcelDepartMonthDeptDetail newExcelDepartMonthDeptDetail = new ExcelDepartMonthDeptDetail();
                        newExcelDepartMonthDeptDetail.setParDepartName(excelDepartMonthDept.getDeptName());
                        newExcelDepartMonthDeptDetail.setDepartName("合计");
                        newExcelDepartMonthDeptDetail.setSalaryDate(month);
                        for(ExcelDepartMonthDeptDetail salarySubTotalData:salarySubTotalDataList){
                            if(salarySubTotalData.getSalaryDate().equals(month)&&salarySubTotalData.getParDepartName().equals(excelDepartMonthDept.getDeptName())){
                                newExcelDepartMonthDeptDetail = salarySubTotalData;
                            }
                        }
                        excelDepartMonthDeptDetailList.add(newExcelDepartMonthDeptDetail);
                    }
                }
                excelDepartMonthDept.setExcelDepartMonthDeptDetails(excelDepartMonthDeptDetailList);
            }
            excelDepartMonthVo.setExcelDepartMonthDepts(excelDepartMonthDeptList);
            excelDepartMonthVoList.add(excelDepartMonthVo);
        }
        return excelDepartMonthVoList;
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
