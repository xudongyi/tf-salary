package business.service.impl;

import business.bean.HrmResource;
import business.bean.PersonnelSalary;
import business.mapper.HrmResourceMapper;
import business.mapper.OperateLogMapper;
import business.mapper.PersonnelSalaryMapper;
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

    @Override
    public IPage<PersonnelSalaryVO> getPersonnelSalaryList(PersonnelSalaryVO personnelSalaryVo, Integer pageNo,Integer pageSize) {
        IPage<PersonnelSalaryVO> page = new Page<PersonnelSalaryVO>(pageNo, pageSize);
        QueryWrapper<PersonnelSalaryVO> sqlaryQueryWrapper = new QueryWrapper<>();
        QueryWrapper<HrmResource> hrmQueryWrapper = new QueryWrapper<>();
        //sqlaryQueryWrapper.lambda().orderByDesc(PersonnelSalary::getId);
        if (StringUtils.isNotBlank(personnelSalaryVo.getWorkcode())) {
            sqlaryQueryWrapper.eq("t1.workcode", personnelSalaryVo.getWorkcode());
        }
        if(StringUtils.isNotBlank(personnelSalaryVo.getDept())){
            hrmQueryWrapper.lambda().eq(HrmResource::getDepartmentid,personnelSalaryVo.getDept().split("_")[0]);
            List<HrmResource> hrmList = hrmResourceMapper.selectList(hrmQueryWrapper);
            String[] hrmDepartmentIdArr = new String[hrmList.size()];
            for(int i = 0; i < hrmList.size();i++){
                hrmDepartmentIdArr[i] = hrmList.get(i).getDepartmentid();
            }
            sqlaryQueryWrapper.in("t2.departmentid",hrmDepartmentIdArr);
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
