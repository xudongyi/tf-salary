package business.vo.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonthlyLaborCostByDeptVo {

    /**
     * 省略了Get、Set方法
     * groupName相同，name不同，即name是groupName的子元素
     * orderNum控制了表头的排序，数字越大，越在后面，默认值是0
     */
    @Excel(name = "部门", orderNum = "0")
    private String departName;

    @Excel(name = "人数",groupName = "1月",  orderNum = "1")
    private BigDecimal janHrmNumber;

    @Excel(name = "金额",groupName = "1月",orderNum = "2")
    private BigDecimal janGrossPay;

    @Excel(name = "福利费",groupName = "1月",orderNum = "3")
    private BigDecimal janWelfareAmountWeal;

    @Excel(name = "保险公积金",groupName = "1月",orderNum = "4")
    private BigDecimal janIaf;

    @Excel(name = "13、14月工资",groupName = "1月",orderNum = "5")
    private BigDecimal janWelfareAmountSalaries;

    @Excel(name = "年终奖",groupName = "1月",orderNum = "6")
    private BigDecimal janWelfareAmountBonus;

    @Excel(name = "小计",groupName = "1月",orderNum = "7")
    private BigDecimal janSubtotal;

    @Excel(name = "人数",groupName = "2月",  orderNum = "8")
    private BigDecimal febHrmNumber;

    @Excel(name = "金额",groupName = "2月",orderNum = "9")
    private BigDecimal febGrossPay;

    @Excel(name = "福利费",groupName = "2月",orderNum = "10")
    private BigDecimal febWelfareAmountWeal;

    @Excel(name = "保险公积金",groupName = "2月",orderNum = "11")
    private BigDecimal febIaf;

    @Excel(name = "13、14月工资",groupName = "2月",orderNum = "12")
    private BigDecimal febWelfareAmountSalaries;

    @Excel(name = "年终奖",groupName = "2月",orderNum = "13")
    private BigDecimal febWelfareAmountBonus;

    @Excel(name = "小计",groupName = "2月",orderNum = "14")
    private BigDecimal febSubtotal;

    @Excel(name = "人数",groupName = "3月",  orderNum = "15")
    private BigDecimal marHrmNumber;

    @Excel(name = "金额",groupName = "3月",orderNum = "16")
    private BigDecimal marGrossPay;

    @Excel(name = "福利费",groupName = "3月",orderNum = "17")
    private BigDecimal marWelfareAmountWeal;

    @Excel(name = "保险公积金",groupName = "3月",orderNum = "18")
    private BigDecimal marIaf;

    @Excel(name = "13、14月工资",groupName = "3月",orderNum = "19")
    private BigDecimal marWelfareAmountSalaries;

    @Excel(name = "年终奖",groupName = "3月",orderNum = "20")
    private BigDecimal marWelfareAmountBonus;

    @Excel(name = "小计",groupName = "3月",orderNum = "21")
    private BigDecimal marSubtotal;

    @Excel(name = "人数",groupName = "4月",  orderNum = "22")
    private BigDecimal aprHrmNumber;

    @Excel(name = "金额",groupName = "4月",orderNum = "23")
    private BigDecimal aprGrossPay;

    @Excel(name = "福利费",groupName = "4月",orderNum = "24")
    private BigDecimal aprWelfareAmountWeal;

    @Excel(name = "保险公积金",groupName = "4月",orderNum = "25")
    private BigDecimal aprIaf;

    @Excel(name = "13、14月工资",groupName = "4月",orderNum = "26")
    private BigDecimal aprWelfareAmountSalaries;

    @Excel(name = "年终奖",groupName = "4月",orderNum = "27")
    private BigDecimal aprWelfareAmountBonus;

    @Excel(name = "小计",groupName = "4月",orderNum = "28")
    private BigDecimal aprSubtotal;

    @Excel(name = "人数",groupName = "5月",  orderNum = "29")
    private BigDecimal mayHrmNumber;

    @Excel(name = "金额",groupName = "5月",orderNum = "30")
    private BigDecimal mayGrossPay;

    @Excel(name = "福利费",groupName = "5月",orderNum = "31")
    private BigDecimal mayWelfareAmountWeal;

    @Excel(name = "保险公积金",groupName = "5月",orderNum = "32")
    private BigDecimal mayIaf;

    @Excel(name = "13、14月工资",groupName = "5月",orderNum = "33")
    private BigDecimal mayWelfareAmountSalaries;

    @Excel(name = "年终奖",groupName = "5月",orderNum = "34")
    private BigDecimal mayWelfareAmountBonus;

    @Excel(name = "小计",groupName = "5月",orderNum = "35")
    private BigDecimal maySubtotal;

    @Excel(name = "人数",groupName = "6月",  orderNum = "36")
    private BigDecimal junHrmNumber;

    @Excel(name = "金额",groupName = "6月",orderNum = "37")
    private BigDecimal junGrossPay;

    @Excel(name = "福利费",groupName = "6月",orderNum = "38")
    private BigDecimal junWelfareAmountWeal;

    @Excel(name = "保险公积金",groupName = "6月",orderNum = "39")
    private BigDecimal junIaf;

    @Excel(name = "13、14月工资",groupName = "6月",orderNum = "40")
    private BigDecimal junWelfareAmountSalaries;

    @Excel(name = "年终奖",groupName = "6月",orderNum = "41")
    private BigDecimal junWelfareAmountBonus;

    @Excel(name = "小计",groupName = "6月",orderNum = "42")
    private BigDecimal junSubtotal;

    @Excel(name = "人数",groupName = "1-6月",  orderNum = "43")
    private BigDecimal halfHrmNumber;

    @Excel(name = "金额",groupName = "1-6月",orderNum = "44")
    private BigDecimal halfGrossPay;

    @Excel(name = "福利费",groupName = "1-6月",orderNum = "45")
    private BigDecimal halfWelfareAmountWeal;

    @Excel(name = "保险公积金",groupName = "1-6月",orderNum = "46")
    private BigDecimal halfIaf;

    @Excel(name = "13、14月工资",groupName = "1-6月",orderNum = "47")
    private BigDecimal halfWelfareAmountSalaries;

    @Excel(name = "年终奖",groupName = "1-6月",orderNum = "48")
    private BigDecimal halfWelfareAmountBonus;

    @Excel(name = "小计",groupName = "1-6月",orderNum = "49")
    private BigDecimal halfSubtotal;

    @Excel(name = "人数",groupName = "7月",  orderNum = "50")
    private BigDecimal julHrmNumber;

    @Excel(name = "金额",groupName = "7月",orderNum = "51")
    private BigDecimal julGrossPay;

    @Excel(name = "福利费",groupName = "7月",orderNum = "52")
    private BigDecimal julWelfareAmountWeal;

    @Excel(name = "保险公积金",groupName = "7月",orderNum = "53")
    private BigDecimal julIaf;

    @Excel(name = "13、14月工资",groupName = "7月",orderNum = "54")
    private BigDecimal julWelfareAmountSalaries;

    @Excel(name = "年终奖",groupName = "7月",orderNum = "55")
    private BigDecimal julWelfareAmountBonus;

    @Excel(name = "小计",groupName = "7月",orderNum = "56")
    private BigDecimal julSubtotal;

    @Excel(name = "人数",groupName = "8月",  orderNum = "57")
    private BigDecimal augHrmNumber;

    @Excel(name = "金额",groupName = "8月",orderNum = "58")
    private BigDecimal augGrossPay;

    @Excel(name = "福利费",groupName = "8月",orderNum = "59")
    private BigDecimal augWelfareAmountWeal;

    @Excel(name = "保险公积金",groupName = "8月",orderNum = "60")
    private BigDecimal augIaf;

    @Excel(name = "13、14月工资",groupName = "8月",orderNum = "61")
    private BigDecimal augWelfareAmountSalaries;

    @Excel(name = "年终奖",groupName = "8月",orderNum = "62")
    private BigDecimal augWelfareAmountBonus;

    @Excel(name = "小计",groupName = "8月",orderNum = "63")
    private BigDecimal augSubtotal;

    @Excel(name = "人数",groupName = "9月",  orderNum = "63")
    private BigDecimal sepHrmNumber;

    @Excel(name = "金额",groupName = "9月",orderNum = "65")
    private BigDecimal sepGrossPay;

    @Excel(name = "福利费",groupName = "9月",orderNum = "66")
    private BigDecimal sepWelfareAmountWeal;

    @Excel(name = "保险公积金",groupName = "9月",orderNum = "67")
    private BigDecimal sepIaf;

    @Excel(name = "13、14月工资",groupName = "9月",orderNum = "68")
    private BigDecimal sepWelfareAmountSalaries;

    @Excel(name = "年终奖",groupName = "9月",orderNum = "69")
    private BigDecimal sepWelfareAmountBonus;

    @Excel(name = "小计",groupName = "9月",orderNum = "70")
    private BigDecimal sepSubtotal;

    @Excel(name = "人数",groupName = "10月",  orderNum = "71")
    private BigDecimal octHrmNumber;

    @Excel(name = "金额",groupName = "10月",orderNum = "72")
    private BigDecimal octGrossPay;

    @Excel(name = "福利费",groupName = "10月",orderNum = "73")
    private BigDecimal octWelfareAmountWeal;

    @Excel(name = "保险公积金",groupName = "10月",orderNum = "74")
    private BigDecimal octIaf;

    @Excel(name = "13、14月工资",groupName = "10月",orderNum = "75")
    private BigDecimal octWelfareAmountSalaries;

    @Excel(name = "年终奖",groupName = "10月",orderNum = "76")
    private BigDecimal octWelfareAmountBonus;

    @Excel(name = "小计",groupName = "10月",orderNum = "77")
    private BigDecimal octSubtotal;

    @Excel(name = "人数",groupName = "11月",  orderNum = "78")
    private BigDecimal novHrmNumber;

    @Excel(name = "金额",groupName = "11月",orderNum = "79")
    private BigDecimal novGrossPay;

    @Excel(name = "福利费",groupName = "11月",orderNum = "80")
    private BigDecimal novWelfareAmountWeal;

    @Excel(name = "保险公积金",groupName = "11月",orderNum = "81")
    private BigDecimal novIaf;

    @Excel(name = "13、14月工资",groupName = "11月",orderNum = "82")
    private BigDecimal novWelfareAmountSalaries;

    @Excel(name = "年终奖",groupName = "11月",orderNum = "83")
    private BigDecimal novWelfareAmountBonus;

    @Excel(name = "小计",groupName = "11月",orderNum = "84")
    private BigDecimal novSubtotal;

    @Excel(name = "人数",groupName = "12月",  orderNum = "85")
    private BigDecimal decHrmNumber;

    @Excel(name = "金额",groupName = "12月",orderNum = "86")
    private BigDecimal decGrossPay;

    @Excel(name = "福利费",groupName = "12月",orderNum = "87")
    private BigDecimal decWelfareAmountWeal;

    @Excel(name = "保险公积金",groupName = "12月",orderNum = "88")
    private BigDecimal decIaf;

    @Excel(name = "13、14月工资",groupName = "12月",orderNum = "89")
    private BigDecimal decWelfareAmountSalaries;

    @Excel(name = "年终奖",groupName = "12月",orderNum = "90")
    private BigDecimal decWelfareAmountBonus;

    @Excel(name = "小计",groupName = "12月",orderNum = "91")
    private BigDecimal decSubtotal;

    @Excel(name = "人数",groupName = "1-12月",  orderNum = "92")
    private BigDecimal wholeHrmNumber;

    @Excel(name = "金额",groupName = "1-12月",orderNum = "93")
    private BigDecimal wholeGrossPay;

    @Excel(name = "福利费",groupName = "1-12月",orderNum = "94")
    private BigDecimal wholeWelfareAmountWeal;

    @Excel(name = "保险公积金",groupName = "1-12月",orderNum = "95")
    private BigDecimal wholeIaf;

    @Excel(name = "13、14月工资",groupName = "1-12月",orderNum = "96")
    private BigDecimal wholeWelfareAmountSalaries;

    @Excel(name = "年终奖",groupName = "1-12月",orderNum = "97")
    private BigDecimal wholeWelfareAmountBonus;

    @Excel(name = "小计",groupName = "1-12月",orderNum = "98")
    private BigDecimal wholeSubtotal;

}
