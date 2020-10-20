package business.service;

import business.bean.PersonnelSalary;
import business.vo.PersonnelSalaryVO;
import business.vo.excel.ExcelDepartMonthDeptDetail;
import business.vo.excel.ExcelDepartMonthVo;
import business.vo.excel.MonthlyLaborCostByDeptVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface IPersonnelSalaryService extends IService<PersonnelSalary> {

    IPage<PersonnelSalaryVO> getPersonnelSalaryList(PersonnelSalaryVO personnelSalaryVo,Integer pageNo,Integer pageSize);

    List<PersonnelSalaryVO> getPersonnelSalaryList(Wrapper<PersonnelSalaryVO> queryWrapper);

    Map<String, Object> getReportHeader();

    Map<String, Object> getReportBodyList(String staDate,String endDate);

    List<Map<String, Object>> getMonthlyLaborCost(String year,Float rate);

    IPage<MonthlyLaborCostByDeptVo> getMonthlyLaborCostByDept(String year, Float rate, Integer pageNo, Integer pageSize);

    List<MonthlyLaborCostByDeptVo> getMonthlyLaborCostByDept(String year, Float rate);

    List<ExcelDepartMonthVo> getMonthlyLaborCostByManufacturingDept(String year, Float rate);

}