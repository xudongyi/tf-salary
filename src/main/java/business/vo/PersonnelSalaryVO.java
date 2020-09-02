package business.vo;

import business.bean.PersonnelSalary;
import lombok.Data;

@Data
public class PersonnelSalaryVO extends PersonnelSalary {

    //部门信息
    private String dept;

    private String userId;

    private String salaryMonth;
}
