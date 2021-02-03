package business.vo;

import business.bean.PersonnelSalary;
import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class PersonnelSalaryVO extends PersonnelSalary {

    //部门信息
    public String dept;
    @Excel(name = "姓名",orderNum="2")
    public String hrName;

    public String salarystamonth;

    public String salaryendmonth;
    @Excel(name = "十三薪",orderNum="47")
    public String welfareAmountSalaries;
    @Excel(name = "年终奖",orderNum="48")
    public String welfareAmountBonus;
    @Excel(name = "福利",orderNum="49")
    public String welfareAmountWeal;
}
