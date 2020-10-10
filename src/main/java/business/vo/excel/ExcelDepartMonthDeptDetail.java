package business.vo.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@Data
public class ExcelDepartMonthDeptDetail implements Serializable {
    private static final long serialVersionUID = 6178454340122797296L;

    @Excel(name = "部门",width = 20,needMerge = true)
    private String departName;

    @Excel(name = "人数",width = 20,needMerge = true)
    private Integer userCount;

    @Excel(name = "金额",width = 20,needMerge = true)
    private BigDecimal salary;

    @Excel(name = "福利费",width = 20,needMerge = true)
    private BigDecimal flf;

    @Excel(name = "保险公积金",width = 20,needMerge = true)
    private BigDecimal gjj;

    @Excel(name = "13、14月工资",width = 20,needMerge = true)
    private BigDecimal otherSalary;

    @Excel(name = "年终奖",width = 20,needMerge = true)
    private BigDecimal yearTotal;

    @Excel(name = "小计",width = 20,needMerge = true)
    private BigDecimal total;











}