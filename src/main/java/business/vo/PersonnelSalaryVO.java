package business.vo;

import business.bean.PersonnelSalary;
import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class PersonnelSalaryVO extends PersonnelSalary {

    //部门信息
    private String dept;
    @Excel(name = "姓名",orderNum="2")
    private String hrName;

    private String salarystamonth;

    private String salaryendmonth;
    @Excel(name = "十三薪",orderNum="47")
    private String welfareAmountSalaries;
    @Excel(name = "年终奖",orderNum="48")
    private String welfareAmountBonus;
    @Excel(name = "福利",orderNum="49")
    private String welfareAmountWeal;
}
