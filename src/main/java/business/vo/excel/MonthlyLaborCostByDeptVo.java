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
    private Integer janHrmNumber=0;

    @Excel(name = "金额",groupName = "1月",orderNum = "2")
    private BigDecimal janGrossPay=new BigDecimal(0);

    @Excel(name = "福利费",groupName = "1月",orderNum = "3")
    private BigDecimal janWelfareAmountWeal=new BigDecimal(0);

    @Excel(name = "保险公积金",groupName = "1月",orderNum = "4")
    private BigDecimal janIaf=new BigDecimal(0);

    @Excel(name = "13、14月工资",groupName = "1月",orderNum = "5")
    private BigDecimal janWelfareAmountSalaries=new BigDecimal(0);

    @Excel(name = "年终奖",groupName = "1月",orderNum = "6")
    private BigDecimal janWelfareAmountBonus=new BigDecimal(0);

    @Excel(name = "小计",groupName = "1月",orderNum = "7")
    private BigDecimal janSubtotal=new BigDecimal(0);

    @Excel(name = "人数",groupName = "2月",  orderNum = "8")
    private Integer febHrmNumber=0;

    @Excel(name = "金额",groupName = "2月",orderNum = "9")
    private BigDecimal febGrossPay=new BigDecimal(0);

    @Excel(name = "福利费",groupName = "2月",orderNum = "10")
    private BigDecimal febWelfareAmountWeal=new BigDecimal(0);

    @Excel(name = "保险公积金",groupName = "2月",orderNum = "11")
    private BigDecimal febIaf=new BigDecimal(0);

    @Excel(name = "13、14月工资",groupName = "2月",orderNum = "12")
    private BigDecimal febWelfareAmountSalaries=new BigDecimal(0);

    @Excel(name = "年终奖",groupName = "2月",orderNum = "13")
    private BigDecimal febWelfareAmountBonus=new BigDecimal(0);

    @Excel(name = "小计",groupName = "2月",orderNum = "14")
    private BigDecimal febSubtotal=new BigDecimal(0);

    @Excel(name = "人数",groupName = "3月",  orderNum = "15")
    private Integer marHrmNumber=0;

    @Excel(name = "金额",groupName = "3月",orderNum = "16")
    private BigDecimal marGrossPay=new BigDecimal(0);

    @Excel(name = "福利费",groupName = "3月",orderNum = "17")
    private BigDecimal marWelfareAmountWeal=new BigDecimal(0);

    @Excel(name = "保险公积金",groupName = "3月",orderNum = "18")
    private BigDecimal marIaf=new BigDecimal(0);

    @Excel(name = "13、14月工资",groupName = "3月",orderNum = "19")
    private BigDecimal marWelfareAmountSalaries=new BigDecimal(0);

    @Excel(name = "年终奖",groupName = "3月",orderNum = "20")
    private BigDecimal marWelfareAmountBonus=new BigDecimal(0);

    @Excel(name = "小计",groupName = "3月",orderNum = "21")
    private BigDecimal marSubtotal=new BigDecimal(0);

    @Excel(name = "人数",groupName = "4月",  orderNum = "22")
    private Integer aprHrmNumber=0;

    @Excel(name = "金额",groupName = "4月",orderNum = "23")
    private BigDecimal aprGrossPay=new BigDecimal(0);

    @Excel(name = "福利费",groupName = "4月",orderNum = "24")
    private BigDecimal aprWelfareAmountWeal=new BigDecimal(0);

    @Excel(name = "保险公积金",groupName = "4月",orderNum = "25")
    private BigDecimal aprIaf=new BigDecimal(0);

    @Excel(name = "13、14月工资",groupName = "4月",orderNum = "26")
    private BigDecimal aprWelfareAmountSalaries=new BigDecimal(0);

    @Excel(name = "年终奖",groupName = "4月",orderNum = "27")
    private BigDecimal aprWelfareAmountBonus=new BigDecimal(0);

    @Excel(name = "小计",groupName = "4月",orderNum = "28")
    private BigDecimal aprSubtotal=new BigDecimal(0);

    @Excel(name = "人数",groupName = "5月",  orderNum = "29")
    private Integer mayHrmNumber=0;

    @Excel(name = "金额",groupName = "5月",orderNum = "30")
    private BigDecimal mayGrossPay=new BigDecimal(0);

    @Excel(name = "福利费",groupName = "5月",orderNum = "31")
    private BigDecimal mayWelfareAmountWeal=new BigDecimal(0);

    @Excel(name = "保险公积金",groupName = "5月",orderNum = "32")
    private BigDecimal mayIaf=new BigDecimal(0);

    @Excel(name = "13、14月工资",groupName = "5月",orderNum = "33")
    private BigDecimal mayWelfareAmountSalaries=new BigDecimal(0);

    @Excel(name = "年终奖",groupName = "5月",orderNum = "34")
    private BigDecimal mayWelfareAmountBonus=new BigDecimal(0);

    @Excel(name = "小计",groupName = "5月",orderNum = "35")
    private BigDecimal maySubtotal=new BigDecimal(0);

    @Excel(name = "人数",groupName = "6月",  orderNum = "36")
    private Integer junHrmNumber=0;

    @Excel(name = "金额",groupName = "6月",orderNum = "37")
    private BigDecimal junGrossPay=new BigDecimal(0);

    @Excel(name = "福利费",groupName = "6月",orderNum = "38")
    private BigDecimal junWelfareAmountWeal=new BigDecimal(0);

    @Excel(name = "保险公积金",groupName = "6月",orderNum = "39")
    private BigDecimal junIaf=new BigDecimal(0);

    @Excel(name = "13、14月工资",groupName = "6月",orderNum = "40")
    private BigDecimal junWelfareAmountSalaries=new BigDecimal(0);

    @Excel(name = "年终奖",groupName = "6月",orderNum = "41")
    private BigDecimal junWelfareAmountBonus=new BigDecimal(0);

    @Excel(name = "小计",groupName = "6月",orderNum = "42")
    private BigDecimal junSubtotal=new BigDecimal(0);

    @Excel(name = "人数",groupName = "1-6月",  orderNum = "43")
    private Integer halfHrmNumber=0;

    @Excel(name = "金额",groupName = "1-6月",orderNum = "44")
    private BigDecimal halfGrossPay=new BigDecimal(0);

    @Excel(name = "福利费",groupName = "1-6月",orderNum = "45")
    private BigDecimal halfWelfareAmountWeal=new BigDecimal(0);

    @Excel(name = "保险公积金",groupName = "1-6月",orderNum = "46")
    private BigDecimal halfIaf=new BigDecimal(0);

    @Excel(name = "13、14月工资",groupName = "1-6月",orderNum = "47")
    private BigDecimal halfWelfareAmountSalaries=new BigDecimal(0);

    @Excel(name = "年终奖",groupName = "1-6月",orderNum = "48")
    private BigDecimal halfWelfareAmountBonus=new BigDecimal(0);

    @Excel(name = "小计",groupName = "1-6月",orderNum = "49")
    private BigDecimal halfSubtotal=new BigDecimal(0);

    @Excel(name = "人数",groupName = "7月",  orderNum = "50")
    private Integer julHrmNumber=0;

    @Excel(name = "金额",groupName = "7月",orderNum = "51")
    private BigDecimal julGrossPay=new BigDecimal(0);

    @Excel(name = "福利费",groupName = "7月",orderNum = "52")
    private BigDecimal julWelfareAmountWeal=new BigDecimal(0);

    @Excel(name = "保险公积金",groupName = "7月",orderNum = "53")
    private BigDecimal julIaf=new BigDecimal(0);

    @Excel(name = "13、14月工资",groupName = "7月",orderNum = "54")
    private BigDecimal julWelfareAmountSalaries=new BigDecimal(0);

    @Excel(name = "年终奖",groupName = "7月",orderNum = "55")
    private BigDecimal julWelfareAmountBonus=new BigDecimal(0);

    @Excel(name = "小计",groupName = "7月",orderNum = "56")
    private BigDecimal julSubtotal=new BigDecimal(0);

    @Excel(name = "人数",groupName = "8月",  orderNum = "57")
    private Integer augHrmNumber=0;

    @Excel(name = "金额",groupName = "8月",orderNum = "58")
    private BigDecimal augGrossPay=new BigDecimal(0);

    @Excel(name = "福利费",groupName = "8月",orderNum = "59")
    private BigDecimal augWelfareAmountWeal=new BigDecimal(0);

    @Excel(name = "保险公积金",groupName = "8月",orderNum = "60")
    private BigDecimal augIaf=new BigDecimal(0);

    @Excel(name = "13、14月工资",groupName = "8月",orderNum = "61")
    private BigDecimal augWelfareAmountSalaries=new BigDecimal(0);

    @Excel(name = "年终奖",groupName = "8月",orderNum = "62")
    private BigDecimal augWelfareAmountBonus=new BigDecimal(0);

    @Excel(name = "小计",groupName = "8月",orderNum = "63")
    private BigDecimal augSubtotal=new BigDecimal(0);

    @Excel(name = "人数",groupName = "9月",  orderNum = "63")
    private Integer sepHrmNumber=0;

    @Excel(name = "金额",groupName = "9月",orderNum = "65")
    private BigDecimal sepGrossPay=new BigDecimal(0);

    @Excel(name = "福利费",groupName = "9月",orderNum = "66")
    private BigDecimal sepWelfareAmountWeal=new BigDecimal(0);

    @Excel(name = "保险公积金",groupName = "9月",orderNum = "67")
    private BigDecimal sepIaf=new BigDecimal(0);

    @Excel(name = "13、14月工资",groupName = "9月",orderNum = "68")
    private BigDecimal sepWelfareAmountSalaries=new BigDecimal(0);

    @Excel(name = "年终奖",groupName = "9月",orderNum = "69")
    private BigDecimal sepWelfareAmountBonus=new BigDecimal(0);

    @Excel(name = "小计",groupName = "9月",orderNum = "70")
    private BigDecimal sepSubtotal=new BigDecimal(0);

    @Excel(name = "人数",groupName = "10月",  orderNum = "71")
    private Integer octHrmNumber=0;

    @Excel(name = "金额",groupName = "10月",orderNum = "72")
    private BigDecimal octGrossPay=new BigDecimal(0);

    @Excel(name = "福利费",groupName = "10月",orderNum = "73")
    private BigDecimal octWelfareAmountWeal=new BigDecimal(0);

    @Excel(name = "保险公积金",groupName = "10月",orderNum = "74")
    private BigDecimal octIaf=new BigDecimal(0);

    @Excel(name = "13、14月工资",groupName = "10月",orderNum = "75")
    private BigDecimal octWelfareAmountSalaries=new BigDecimal(0);

    @Excel(name = "年终奖",groupName = "10月",orderNum = "76")
    private BigDecimal octWelfareAmountBonus=new BigDecimal(0);

    @Excel(name = "小计",groupName = "10月",orderNum = "77")
    private BigDecimal octSubtotal=new BigDecimal(0);

    @Excel(name = "人数",groupName = "11月",  orderNum = "78")
    private Integer novHrmNumber=0;

    @Excel(name = "金额",groupName = "11月",orderNum = "79")
    private BigDecimal novGrossPay=new BigDecimal(0);

    @Excel(name = "福利费",groupName = "11月",orderNum = "80")
    private BigDecimal novWelfareAmountWeal=new BigDecimal(0);

    @Excel(name = "保险公积金",groupName = "11月",orderNum = "81")
    private BigDecimal novIaf=new BigDecimal(0);

    @Excel(name = "13、14月工资",groupName = "11月",orderNum = "82")
    private BigDecimal novWelfareAmountSalaries=new BigDecimal(0);

    @Excel(name = "年终奖",groupName = "11月",orderNum = "83")
    private BigDecimal novWelfareAmountBonus=new BigDecimal(0);

    @Excel(name = "小计",groupName = "11月",orderNum = "84")
    private BigDecimal novSubtotal=new BigDecimal(0);

    @Excel(name = "人数",groupName = "12月",  orderNum = "85")
    private Integer decHrmNumber=0;

    @Excel(name = "金额",groupName = "12月",orderNum = "86")
    private BigDecimal decGrossPay=new BigDecimal(0);

    @Excel(name = "福利费",groupName = "12月",orderNum = "87")
    private BigDecimal decWelfareAmountWeal=new BigDecimal(0);

    @Excel(name = "保险公积金",groupName = "12月",orderNum = "88")
    private BigDecimal decIaf=new BigDecimal(0);

    @Excel(name = "13、14月工资",groupName = "12月",orderNum = "89")
    private BigDecimal decWelfareAmountSalaries=new BigDecimal(0);

    @Excel(name = "年终奖",groupName = "12月",orderNum = "90")
    private BigDecimal decWelfareAmountBonus=new BigDecimal(0);

    @Excel(name = "小计",groupName = "12月",orderNum = "91")
    private BigDecimal decSubtotal=new BigDecimal(0);

    @Excel(name = "人数",groupName = "1-12月",  orderNum = "92")
    private Integer wholeHrmNumber=0;

    @Excel(name = "金额",groupName = "1-12月",orderNum = "93")
    private BigDecimal wholeGrossPay=new BigDecimal(0);

    @Excel(name = "福利费",groupName = "1-12月",orderNum = "94")
    private BigDecimal wholeWelfareAmountWeal=new BigDecimal(0);

    @Excel(name = "保险公积金",groupName = "1-12月",orderNum = "95")
    private BigDecimal wholeIaf=new BigDecimal(0);

    @Excel(name = "13、14月工资",groupName = "1-12月",orderNum = "96")
    private BigDecimal wholeWelfareAmountSalaries=new BigDecimal(0);

    @Excel(name = "年终奖",groupName = "1-12月",orderNum = "97")
    private BigDecimal wholeWelfareAmountBonus=new BigDecimal(0);

    @Excel(name = "小计",groupName = "1-12月",orderNum = "98")
    private BigDecimal wholeSubtotal=new BigDecimal(0);



}
