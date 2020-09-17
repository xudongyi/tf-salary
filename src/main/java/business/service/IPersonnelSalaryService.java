package business.service;

import business.bean.PersonnelSalary;
import business.vo.PersonnelSalaryVO;
import business.vo.ReportVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface IPersonnelSalaryService extends IService<PersonnelSalary> {

    IPage<PersonnelSalaryVO> getPersonnelSalaryList(IPage<PersonnelSalaryVO> page, Wrapper<PersonnelSalaryVO> queryWrapper);

    List<PersonnelSalaryVO> getPersonnelSalaryList(Wrapper<PersonnelSalaryVO> queryWrapper);

    Map<String, Object> getReportHeader();

    Map<String, Object> getReportBodyList(String staDate,String endDate);

}