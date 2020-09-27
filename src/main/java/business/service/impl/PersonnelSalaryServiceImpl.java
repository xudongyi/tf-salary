package business.service.impl;

import business.bean.HrmResource;
import business.bean.PersonnelSalary;
import business.mapper.HrmResourceMapper;
import business.mapper.OperateLogMapper;
import business.mapper.PersonnelSalaryMapper;
import business.mapper.PersonnelWelfareMapper;
import business.service.IPersonnelSalaryService;
import business.vo.PersonnelSalaryVO;
import business.vo.PersonnelWelfareVO;
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
    private HrmResourceMapper hrmResourceMapper;
    @Resource
    private PersonnelWelfareMapper personnelWelfareMapper;

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
    public List<Map<String, Object>> getMonthlyLaborCost(String salaryYear, Float rate) {
        List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
        for(int i=1;i<=12;i++){
            String month = salaryYear+"-"+(String.valueOf(i).length()==1?"0"+String.valueOf(i):String.valueOf(i));
            Map<String,Object> salaryMonthDataMap = personnelSalaryMapper.getMonthlyLaborCost(month);
            Map<String,Object> welfareMonthDateMap = personnelWelfareMapper.getMonthlyLaborCost(month);
            Map<String,Object> monthResultMap = new HashMap<String,Object>();
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
            monthResultMap.put("MONTHTOTAL",eachMonthTotal);
            resultList.add(monthResultMap);
        }


        return resultList;
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
