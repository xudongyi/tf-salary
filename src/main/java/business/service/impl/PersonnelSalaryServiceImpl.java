package business.service.impl;

import business.bean.PersonnelSalary;
import business.mapper.OperateLogMapper;
import business.mapper.PersonnelSalaryMapper;
import business.service.IPersonnelSalaryService;
import business.vo.PersonnelSalaryVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

    @Override
    public List<PersonnelSalaryVO> getPersonnelSalaryList(Wrapper<PersonnelSalaryVO> queryWrapper) {
        return personnelSalaryMapper.getPersonnelSalary(queryWrapper);
    }

    @Override
    public IPage<PersonnelSalaryVO> getPersonnelSalaryList(IPage<PersonnelSalaryVO> page, Wrapper<PersonnelSalaryVO> queryWrapper) {
        return personnelSalaryMapper.getPersonnelSalary(page, queryWrapper);
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

        result.put("salaryList",salaryList);
        return result;
    }

    public String getMonth(String dateStr, Integer lastTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date date = null;
        try {
            date = dateStr.equals("") ? format.parse(dateStr) : new Date();
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
