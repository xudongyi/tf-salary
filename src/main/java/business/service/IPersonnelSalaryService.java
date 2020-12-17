package business.service;

import business.bean.PersonnelSalary;
import business.vo.PersonnelSalaryVO;
import business.vo.excel.ExcelDepartMonthDeptDetail;
import business.vo.excel.ExcelDepartMonthVo;
import business.vo.excel.MonthlyLaborCostByDeptVo;
import business.vo.excel.MonthlyLaborCostByTypeVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface IPersonnelSalaryService extends IService<PersonnelSalary> {

    IPage<PersonnelSalaryVO> getPersonnelSalaryList(PersonnelSalaryVO personnelSalaryVo,String site,Integer pageNo,Integer pageSize);

    List<PersonnelSalaryVO> getPersonnelSalaryList(Wrapper<PersonnelSalaryVO> queryWrapper,String site);

    Map<String, Object> getReportHeader(String site);

    Map<String, Object> getReportBodyList(String staDate,String endDate,String site);

    List<Map<String, Object>> getMonthlyLaborCost(String year, Float rate, String site);

    List<MonthlyLaborCostByDeptVo> getMonthlyLaborCostByDept(String year, Float rate,String site,String tabId);

    List<MonthlyLaborCostByTypeVo> getMonthlyLaborCostByType(String month, Float rate, String site, String tabId);

    List<MonthlyLaborCostByDeptVo> getTypeLaborCostByDate(String year, Float rate, String site, String tabId,String typeIds);

    List<ExcelDepartMonthVo> getMonthlyLaborCostByManufacturingDept(String year, Float rate,String site,String tabId);

}