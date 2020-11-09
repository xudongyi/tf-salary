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
    @Resource
    private SalaryReportConfigMapper salaryReportConfigMapper;

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
        String currentMonthSalary = personnelSalaryMapper.getSalaryByMonth(currentMonth).size()==0?"0":personnelSalaryMapper.getSalaryByMonth(currentMonth).get(0).get("NET_SALARY").toString();
        String lastMonthSalary = personnelSalaryMapper.getSalaryByMonth(lastMonthStr).size()==0?"0":personnelSalaryMapper.getSalaryByMonth(lastMonthStr).get(0).get("NET_SALARY").toString();
        String currentMonthImportNumber = personnelSalaryMapper.getImportNumberByMonth(currentMonth).size()==0?"0":personnelSalaryMapper.getImportNumberByMonth(currentMonth).get(0).get("IMPORT_NUMBER").toString();
        String lastMonthImportNumber = personnelSalaryMapper.getImportNumberByMonth(lastMonthStr).size()==0?"0":personnelSalaryMapper.getImportNumberByMonth(lastMonthStr).get(0).get("IMPORT_NUMBER").toString();
        String currentMonthVisitTimes = operateLogMapper.getVisitTimesByMonth(currentMonth).size()==0?"0":operateLogMapper.getVisitTimesByMonth(currentMonth).get(0).get("VISIT_TIMES").toString();
        String lastMonthVisitTimes = operateLogMapper.getVisitTimesByMonth(lastMonthStr).size()==0?"0":operateLogMapper.getVisitTimesByMonth(lastMonthStr).get(0).get("VISIT_TIMES").toString();
        String currentMonthNoteNumber = operateLogMapper.getNoteNumberByMonth(currentMonth).size()==0?"0":operateLogMapper.getNoteNumberByMonth(currentMonth).get(0).get("NOTE_NUMBER").toString();
        String lastMonthNoteNumber = operateLogMapper.getNoteNumberByMonth(lastMonthStr).size()==0?"0":operateLogMapper.getNoteNumberByMonth(currentMonth).get(0).get("NOTE_NUMBER").toString();
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
        List<Map<String,Object>> monthlySalaryInfoList = personnelSalaryMapper.getMonthlySalaryInfoByYear(year,rate);
        List<Map<String,Object>> monthlyWelfareInfoList = personnelWelfareMapper.getMonthlyWelfareInfoByYear(year);
        Map<String,Object> yearResultMap = new LinkedHashMap<String,Object>();
        yearResultMap.put("REMARK","合计");
        Integer salaryRsAll = 0;
        Float yfgzAll = 0f;
        Float gjjAll = 0f;
        Float ssxAll = 0f;
        Float jjAll = 0f;
        Float flAll = 0f;
        Float subtotalAll = 0f;
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
                }
            }
            for(int j=0;j<monthlyWelfareInfoList.size();j++){
                if(month.equals(monthlyWelfareInfoList.get(j).get("WELFARE_DATE"))){
                    salaryRs = Integer.parseInt(monthlyWelfareInfoList.get(j).get("RS").toString())>salaryRs?Integer.parseInt(monthlyWelfareInfoList.get(j).get("RS").toString()):salaryRs;
                    ssx = Float.parseFloat(monthlyWelfareInfoList.get(j).get("SSX").toString());
                    jj = Float.parseFloat(monthlyWelfareInfoList.get(j).get("JJ").toString());
                    fl = Float.parseFloat(monthlyWelfareInfoList.get(j).get("FL").toString());
                }
            }
            subtotal = Float.parseFloat(new BigDecimal(subtotal).add(new BigDecimal(yfgz)).add(new BigDecimal(gjj)).add(new BigDecimal(ssx)).add(new BigDecimal(jj)).add(new BigDecimal(fl)).toString());
            //一年合计
            salaryRsAll = salaryRsAll+salaryRs;
            yfgzAll = Float.parseFloat(new BigDecimal(yfgzAll).add(new BigDecimal(yfgz)).toString());
            gjjAll = Float.parseFloat(new BigDecimal(gjjAll).add(new BigDecimal(gjj)).toString());
            ssxAll = Float.parseFloat(new BigDecimal(ssxAll).add(new BigDecimal(ssx)).toString());
            jjAll = Float.parseFloat(new BigDecimal(jjAll).add(new BigDecimal(jj)).toString());
            flAll = Float.parseFloat(new BigDecimal(flAll).add(new BigDecimal(fl)).toString());
            subtotalAll = Float.parseFloat(new BigDecimal(subtotalAll).add(new BigDecimal(subtotal)).toString());
            monthResultMap.put("RS",salaryRs);
            monthResultMap.put("YFGZ",yfgz);
            monthResultMap.put("GJJ",gjj);
            monthResultMap.put("SSX",ssx);
            monthResultMap.put("JJ",jj);
            monthResultMap.put("FL",fl);
            monthResultMap.put("TOTAL",subtotal);
            resultList.add(monthResultMap);
        }
        yearResultMap.put("RS",salaryRsAll);
        yearResultMap.put("YFGZ",yfgzAll);
        yearResultMap.put("GJJ",gjjAll);
        yearResultMap.put("SSX",ssxAll);
        yearResultMap.put("JJ",jjAll);
        yearResultMap.put("FL",flAll);
        yearResultMap.put("TOTAL",subtotalAll);
        resultList.add(yearResultMap);
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
    public List<ExcelDepartMonthVo> getMonthlyLaborCostByManufacturingDept(String year, Float rate,String site,String tabId) {
        List<SalaryReportConfig> salaryReportConfigList=salaryReportConfigMapper.getSalaryReportConfig(site,tabId);
        List<Map<String,Object>> salaryDataList = personnelSalaryMapper.getMonthlyLaborCostByDept(year,rate,site,tabId);

        List<ExcelDepartMonthVo> excelDepartMonthVoList = new ArrayList<ExcelDepartMonthVo>();
        for(int i=1;i<=12;i++){
            String month = year+"-"+(String.valueOf(i).length()==1?"0"+String.valueOf(i):String.valueOf(i));
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
            for(ExcelDepartMonthDept excelDepartMonthDept:excelDepartMonthDeptList){
                List<ExcelDepartMonthDeptDetail> excelDepartMonthDeptDetailList = new ArrayList<ExcelDepartMonthDeptDetail>();
                for(SalaryReportConfig salaryReportConfig:salaryReportConfigList){
                    if(excelDepartMonthDept.getDeptName().equals(salaryReportConfig.getStage())){
                        ExcelDepartMonthDeptDetail excelDepartMonthDeptDetail = new ExcelDepartMonthDeptDetail();
                        excelDepartMonthDeptDetail.setSalaryDate(month);
                        excelDepartMonthDeptDetail.setDepartName(salaryReportConfig.getDepartName());
                        excelDepartMonthDeptDetailList.add(excelDepartMonthDeptDetail);
                    }
                }
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
