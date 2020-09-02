package business.service.impl;

import business.bean.PersonnelSalary;
import business.mapper.PersonnelSalaryMapper;
import business.service.IPersonnelSalaryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class PersonnelSalaryServiceImpl extends ServiceImpl<PersonnelSalaryMapper, PersonnelSalary> implements IPersonnelSalaryService {
    @Autowired
    private PersonnelSalaryMapper personnelSalaryMapper;

}
