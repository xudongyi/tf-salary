package business.vo.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonthlyLaborCostByTypeVo {

    /**
     * 省略了Get、Set方法
     * groupName相同，name不同，即name是groupName的子元素
     * orderNum控制了表头的排序，数字越大，越在后面，默认值是0
     */
    @Excel(name = "部门", orderNum = "0")
    private String departName;

    @Excel(name = "人数",groupName = "管理技术人员",  orderNum = "1")
    private Integer mtHrmNumber=0;

    @Excel(name = "金额",groupName = "管理技术人员",orderNum = "2")
    private BigDecimal mtGrossPay=new BigDecimal(0);

    @Excel(name = "福利费",groupName = "管理技术人员",orderNum = "3")
    private BigDecimal mtWelfareAmountWeal=new BigDecimal(0);

    @Excel(name = "公积金",groupName = "管理技术人员",orderNum = "4")
    private BigDecimal mtAcFund=new BigDecimal(0);

    @Excel(name = "失保",groupName = "管理技术人员",orderNum = "5")
    private BigDecimal mtLoi=new BigDecimal(0);

    @Excel(name = "保险公积金",groupName = "管理技术人员",orderNum = "6")
    private BigDecimal mtIaf=new BigDecimal(0);

    @Excel(name = "13、14月工资",groupName = "管理技术人员",orderNum = "7")
    private BigDecimal mtWelfareAmountSalaries=new BigDecimal(0);

    @Excel(name = "年终奖",groupName = "管理技术人员",orderNum = "8")
    private BigDecimal mtWelfareAmountBonus=new BigDecimal(0);

    @Excel(name = "小计",groupName = "管理技术人员",orderNum = "9")
    private BigDecimal mtSubtotal=new BigDecimal(0);

    @Excel(name = "人数",groupName = "工程技术人员",  orderNum = "10")
    private Integer etHrmNumber=0;

    @Excel(name = "金额",groupName = "工程技术人员",orderNum = "11")
    private BigDecimal etGrossPay=new BigDecimal(0);

    @Excel(name = "福利费",groupName = "工程技术人员",orderNum = "12")
    private BigDecimal etWelfareAmountWeal=new BigDecimal(0);

    @Excel(name = "公积金",groupName = "工程技术人员",orderNum = "13")
    private BigDecimal etAcFund=new BigDecimal(0);

    @Excel(name = "失保",groupName = "工程技术人员",orderNum = "14")
    private BigDecimal etLoi=new BigDecimal(0);

    @Excel(name = "保险公积金",groupName = "工程技术人员",orderNum = "15")
    private BigDecimal etIaf=new BigDecimal(0);

    @Excel(name = "13、14月工资",groupName = "工程技术人员",orderNum = "16")
    private BigDecimal etWelfareAmountSalaries=new BigDecimal(0);

    @Excel(name = "年终奖",groupName = "工程技术人员",orderNum = "17")
    private BigDecimal etWelfareAmountBonus=new BigDecimal(0);

    @Excel(name = "小计",groupName = "工程技术人员",orderNum = "18")
    private BigDecimal etSubtotal=new BigDecimal(0);

    @Excel(name = "人数",groupName = "管技人员合计",  orderNum = "19")
    private Integer mtTotalHrmNumber=0;

    @Excel(name = "金额",groupName = "管技人员合计",orderNum = "20")
    private BigDecimal mtTotalGrossPay=new BigDecimal(0);

    @Excel(name = "福利费",groupName = "管技人员合计",orderNum = "21")
    private BigDecimal mtTotalWelfareAmountWeal=new BigDecimal(0);

    @Excel(name = "公积金",groupName = "管技人员合计",orderNum = "22")
    private BigDecimal mtTotalAcFund=new BigDecimal(0);

    @Excel(name = "失保",groupName = "管技人员合计",orderNum = "23")
    private BigDecimal mtTotalLoi=new BigDecimal(0);

    @Excel(name = "保险公积金",groupName = "管技人员合计",orderNum = "24")
    private BigDecimal mtTotalIaf=new BigDecimal(0);

    @Excel(name = "13、14月工资",groupName = "管技人员合计",orderNum = "25")
    private BigDecimal mtTotalWelfareAmountSalaries=new BigDecimal(0);

    @Excel(name = "年终奖",groupName = "管技人员合计",orderNum = "26")
    private BigDecimal mtTotalWelfareAmountBonus=new BigDecimal(0);

    @Excel(name = "小计",groupName = "管技人员合计",orderNum = "27")
    private BigDecimal mtTotalSubtotal=new BigDecimal(0);

    @Excel(name = "人数",groupName = "生产作业人员",  orderNum = "28")
    private Integer prodHrmNumber=0;

    @Excel(name = "金额",groupName = "生产作业人员",orderNum = "29")
    private BigDecimal prodGrossPay=new BigDecimal(0);

    @Excel(name = "福利费",groupName = "生产作业人员",orderNum = "30")
    private BigDecimal prodWelfareAmountWeal=new BigDecimal(0);

    @Excel(name = "公积金",groupName = "生产作业人员",orderNum = "31")
    private BigDecimal prodAcFund=new BigDecimal(0);

    @Excel(name = "失保",groupName = "生产作业人员",orderNum = "32")
    private BigDecimal prodLoi=new BigDecimal(0);

    @Excel(name = "保险公积金",groupName = "生产作业人员",orderNum = "33")
    private BigDecimal prodIaf=new BigDecimal(0);

    @Excel(name = "13、14月工资",groupName = "生产作业人员",orderNum = "34")
    private BigDecimal prodWelfareAmountSalaries=new BigDecimal(0);

    @Excel(name = "年终奖",groupName = "生产作业人员",orderNum = "35")
    private BigDecimal prodWelfareAmountBonus=new BigDecimal(0);

    @Excel(name = "小计",groupName = "生产作业人员",orderNum = "36")
    private BigDecimal prodSubtotal=new BigDecimal(0);

    @Excel(name = "人数",groupName = "后勤服务人员",  orderNum = "37")
    private Integer logisHrmNumber=0;

    @Excel(name = "金额",groupName = "后勤服务人员",orderNum = "38")
    private BigDecimal logisGrossPay=new BigDecimal(0);

    @Excel(name = "福利费",groupName = "后勤服务人员",orderNum = "39")
    private BigDecimal logisWelfareAmountWeal=new BigDecimal(0);

    @Excel(name = "公积金",groupName = "后勤服务人员",orderNum = "40")
    private BigDecimal logisAcFund=new BigDecimal(0);

    @Excel(name = "失保",groupName = "后勤服务人员",orderNum = "41")
    private BigDecimal logisLoi=new BigDecimal(0);

    @Excel(name = "保险公积金",groupName = "后勤服务人员",orderNum = "42")
    private BigDecimal logisIaf=new BigDecimal(0);

    @Excel(name = "13、14月工资",groupName = "后勤服务人员",orderNum = "43")
    private BigDecimal logisWelfareAmountSalaries=new BigDecimal(0);

    @Excel(name = "年终奖",groupName = "后勤服务人员",orderNum = "44")
    private BigDecimal logisWelfareAmountBonus=new BigDecimal(0);

    @Excel(name = "小计",groupName = "后勤服务人员",orderNum = "45")
    private BigDecimal logisSubtotal=new BigDecimal(0);

    @Excel(name = "人数",groupName = "质量检验人员",  orderNum = "46")
    private Integer qiHrmNumber=0;

    @Excel(name = "金额",groupName = "质量检验人员",orderNum = "47")
    private BigDecimal qiGrossPay=new BigDecimal(0);

    @Excel(name = "福利费",groupName = "质量检验人员",orderNum = "48")
    private BigDecimal qiWelfareAmountWeal=new BigDecimal(0);

    @Excel(name = "公积金",groupName = "质量检验人员",orderNum = "49")
    private BigDecimal qiAcFund=new BigDecimal(0);

    @Excel(name = "失保",groupName = "质量检验人员",orderNum = "50")
    private BigDecimal qiLoi=new BigDecimal(0);

    @Excel(name = "保险公积金",groupName = "质量检验人员",orderNum = "51")
    private BigDecimal qiIaf=new BigDecimal(0);

    @Excel(name = "13、14月工资",groupName = "质量检验人员",orderNum = "52")
    private BigDecimal qiWelfareAmountSalaries=new BigDecimal(0);

    @Excel(name = "年终奖",groupName = "质量检验人员",orderNum = "53")
    private BigDecimal qiWelfareAmountBonus=new BigDecimal(0);

    @Excel(name = "小计",groupName = "质量检验人员",orderNum = "54")
    private BigDecimal qiSubtotal=new BigDecimal(0);

    @Excel(name = "人数",groupName = "生产保障人员",  orderNum = "55")
    private Integer psHrmNumber=0;

    @Excel(name = "金额",groupName = "生产保障人员",orderNum = "56")
    private BigDecimal psGrossPay=new BigDecimal(0);

    @Excel(name = "福利费",groupName = "生产保障人员",orderNum = "57")
    private BigDecimal psWelfareAmountWeal=new BigDecimal(0);

    @Excel(name = "公积金",groupName = "生产保障人员",orderNum = "58")
    private BigDecimal psAcFund=new BigDecimal(0);

    @Excel(name = "失保",groupName = "生产保障人员",orderNum = "59")
    private BigDecimal psLoi=new BigDecimal(0);

    @Excel(name = "保险公积金",groupName = "生产保障人员",orderNum = "60")
    private BigDecimal psIaf=new BigDecimal(0);

    @Excel(name = "13、14月工资",groupName = "生产保障人员",orderNum = "61")
    private BigDecimal psWelfareAmountSalaries=new BigDecimal(0);

    @Excel(name = "年终奖",groupName = "生产保障人员",orderNum = "62")
    private BigDecimal psWelfareAmountBonus=new BigDecimal(0);

    @Excel(name = "小计",groupName = "生产保障人员",orderNum = "63")
    private BigDecimal psSubtotal=new BigDecimal(0);

    @Excel(name = "人数",groupName = "合计",  orderNum = "64")
    private Integer totalHrmNumber=0;

    @Excel(name = "金额",groupName = "合计",orderNum = "65")
    private BigDecimal totalGrossPay=new BigDecimal(0);

    @Excel(name = "福利费",groupName = "合计",orderNum = "66")
    private BigDecimal totalWelfareAmountWeal=new BigDecimal(0);

    @Excel(name = "公积金",groupName = "合计",orderNum = "67")
    private BigDecimal totalAcFund=new BigDecimal(0);

    @Excel(name = "失保",groupName = "合计",orderNum = "68")
    private BigDecimal totalLoi=new BigDecimal(0);

    @Excel(name = "保险公积金",groupName = "合计",orderNum = "69")
    private BigDecimal totalIaf=new BigDecimal(0);

    @Excel(name = "13、14月工资",groupName = "合计",orderNum = "70")
    private BigDecimal totalWelfareAmountSalaries=new BigDecimal(0);

    @Excel(name = "年终奖",groupName = "合计",orderNum = "71")
    private BigDecimal totalWelfareAmountBonus=new BigDecimal(0);

    @Excel(name = "小计",groupName = "合计",orderNum = "72")
    private BigDecimal totalSubtotal=new BigDecimal(0);
}
