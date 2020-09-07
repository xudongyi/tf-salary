package business.service;

import business.bean.PersonnelSalary;
import business.vo.PersonnelSalaryVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IPersonnelSalaryService extends IService<PersonnelSalary> {

    IPage<PersonnelSalaryVO> getPersonnelSalaryList(IPage<PersonnelSalaryVO> page, Wrapper<PersonnelSalaryVO> queryWrapper);

    List<PersonnelSalaryVO> getPersonnelSalaryList(Wrapper<PersonnelSalaryVO> queryWrapper);
}