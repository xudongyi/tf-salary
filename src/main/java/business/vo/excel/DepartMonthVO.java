package business.vo.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepartMonthVO {

    /**
     * 省略了Get、Set方法
     * groupName相同，name不同，即name是groupName的子元素
     * orderNum控制了表头的排序，数字越大，越在后面，默认值是0
     */
    @Excel(name = "部门", orderNum = "0")
    private String departMentName;

    @Excel(name = "人数",groupName = "一月份",  orderNum = "1")
    private BigDecimal people1;

    @Excel(name = "金额",groupName = "一月份",orderNum = "2")
    private BigDecimal salary1;

    @Excel(name = "福利费",groupName = "一月份",orderNum = "3")
    private BigDecimal fl1;

    @Excel(name = "保险公积金",groupName = "一月份",orderNum = "4")
    private BigDecimal bx1;

    @Excel(name = "13、14月工资",groupName = "一月份",orderNum = "5")
    private BigDecimal other1;

    @Excel(name = "年终奖",groupName = "一月份",orderNum = "6")
    private BigDecimal year1;

    @Excel(name = "小计",groupName = "一月份",orderNum = "7")
    private BigDecimal total1;

    @Excel(name = "人数",groupName = "二月份",  orderNum = "8")
    private BigDecimal people2;

    @Excel(name = "金额",groupName = "二月份",orderNum = "9")
    private BigDecimal salary2;

    @Excel(name = "福利费",groupName = "二月份",orderNum = "10")
    private BigDecimal fl2;

    @Excel(name = "保险公积金",groupName = "二月份",orderNum = "11")
    private BigDecimal bx2;

    @Excel(name = "13、14月工资",groupName = "二月份",orderNum = "12")
    private BigDecimal other2;

    @Excel(name = "年终奖",groupName = "二月份",orderNum = "13")
    private BigDecimal year2;

    @Excel(name = "小计",groupName = "二月份",orderNum = "14")
    private BigDecimal total2;


}
