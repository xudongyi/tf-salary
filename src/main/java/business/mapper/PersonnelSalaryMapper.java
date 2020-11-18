package business.mapper;

import business.bean.PersonnelSalary;
import business.vo.PersonnelSalaryVO;
import business.vo.excel.ExcelDepartMonthDeptDetail;
import business.vo.excel.MonthlyLaborCostByDeptVo;
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
    IPage<PersonnelSalaryVO> getPersonnelSalary(IPage<PersonnelSalaryVO> page,@Param(Constants.WRAPPER) Wrapper<PersonnelSalaryVO> queryWrapper,String site);

    List<PersonnelSalaryVO> getPersonnelSalary(@Param(Constants.WRAPPER) Wrapper<PersonnelSalaryVO> queryWrapper);

    List<Map<String,Object>> getSalaryBetweenMonth(String staDate,String endDate);

    List<Map<String,Object>> getSalaryByMonth(String month);

    List<Map<String,Object>> getImportNumberByMonth(String month);

    List<Map<String,Object>> getSalaryRankByDepartment();

    Map<String, Object> getMonthlyLaborCost(String month);

    List<Map<String,Object>> getMonthlySalaryInfoByYear(String year,Float rate,String site);

    Map<String,Object> getWholeYearSalaryInfoByYear(String year,Float rate,String site);

    Map<String, Object> getYearlyLaborCost(String year);

    List<Map<String, Object>> getMonthlyLaborCostByDept(String year, Float rate,String site,String tabId);

    List<ExcelDepartMonthDeptDetail> getMonthlyLaborCostByManufacturingDept(String year, Float rate,String site,String tabId);

    List<ExcelDepartMonthDeptDetail> getMonthlyLaborCostByManufacturingStage(String year, Float rate,String site,String tabId);

    List<Map<String,Object>> getTypeLaborCostByDate(String year, Float rate, String site, String tabId,String typeIds);

    List<Map<String,Object>> getTypeLaborTotalCostByDate(String year, Float rate, String site, String tabId,String typeIds);

    List<ExcelDepartMonthDeptDetail> getMonthlyLaborCostByDeptCode(String year, Float rate,String code1,String code2,String code3,String code4,String code5,String code6,String code7,String code8);

    List<Map<String,Object>> getMonthlyLaborCostByType(String month, Float rate,String site,String tabId);


}
