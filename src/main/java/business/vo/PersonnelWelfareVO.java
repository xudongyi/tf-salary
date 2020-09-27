package business.vo;

import business.bean.PersonnelWelfare;
import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class PersonnelWelfareVO extends PersonnelWelfare {

    //部门信息
    private String dept;
    @Excel(name = "姓名",orderNum="1")
    private String hrName;

    private String welfarestamonth;

    private String welfareendmonth;

}
