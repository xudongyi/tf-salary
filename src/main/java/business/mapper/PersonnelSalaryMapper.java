package business.mapper;

import business.bean.PersonnelSalary;
import business.vo.PersonnelSalaryVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface PersonnelSalaryMapper extends BaseMapper<PersonnelSalary> {

    /**
     * 薪资查询关联人员姓名
     */
    IPage<PersonnelSalaryVO> getPersonnelSalary(IPage<PersonnelSalaryVO> page,@Param(Constants.WRAPPER) Wrapper<PersonnelSalaryVO> queryWrapper);

    List<PersonnelSalaryVO> getPersonnelSalary(@Param(Constants.WRAPPER) Wrapper<PersonnelSalaryVO> queryWrapper);

    List<Map<String,Object>> getSalaryBetweenMonth(String staDate,String endDate);

    Map<String,Object> getSalaryByMonth(String month);

    Map<String,Object> getImportNumberByMonth(String month);

    List<Map<String,Object>> getSalaryRankByDepartment();

    Map<String, Object> getMonthlyLaborCost(String month);

    Map<String, Object> getYearlyLaborCost(String year);

}
