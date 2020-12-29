package business.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * 薪资表
 * @author xudy
 * @since 2019-09-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("PERSONNEL_SALARY")
public class PersonnelSalary extends Model<PersonnelSalary> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @Excel(name = "工号",orderNum="1")
    private String workcode;
    @Excel(name = "基本工资",orderNum="10")
    private Float basePay;
    @Excel(name = "考核工资",orderNum="11")
    private Float assessmentPay;
    @Excel(name = "加班工资",orderNum="12")
    private Float overtimePay;
    @Excel(name = "计件工资",orderNum="13")
    private Float pieceRatePay;
    @Excel(name = "计加班工资",orderNum="14")
    private Float pieceOverPay;
    @Excel(name = "上岗工资",orderNum="15")
    private Float ondutyPay;
    @Excel(name = "技能工资",orderNum="16")
    private Float skillPay;
    @Excel(name = "交通补贴",orderNum="17")
    private Float trafficSubsidy;
    @Excel(name = "增日工资",orderNum="18")
    private Float increasingDayPay;
    @Excel(name = "三班倒工龄工资",orderNum="19")
    private Float threeShiftsPay;
    @Excel(name = "带徒津贴",orderNum="20")
    private Float apprenticeSubsidy;
    @Excel(name = "大学生补贴",orderNum="21")
    private Float collegeSubsidy;
    @Excel(name = "组长补贴",orderNum="22")
    private Float groupLeaderSubsidy;
    @Excel(name = "中夜班费及补贴",orderNum="23")
    private Float nightShiftSubsidy;
    @Excel(name = "病假工资",orderNum="24")
    private Float sickPay;
    @Excel(name = "增补",orderNum="25")
    private Float supplementPay;
    @Excel(name = "净化费",orderNum="26")
    private Float purificationSubsidy;
    @Excel(name = "质量奖",orderNum="27")
    private Float qualityAward;
    @Excel(name = "塑封补贴",orderNum="28")
    private Float plasticSealSubsidy;
    @Excel(name = "补贴",orderNum="29")
    private Float subsidy;
    @Excel(name = "其他补贴",orderNum="30")
    private Float otherSubsidy;
    @Excel(name = "应发工资",orderNum="31")
    private Float grossPay;
    @Excel(name = "病假扣款",orderNum="32")
    private Float sickDeduction;
    @Excel(name = "个税",orderNum="33")
    private Float incomeTax;
    @Excel(name = "补扣税",orderNum="34")
    private Float supplementaryTax;
    @Excel(name = "住宿费",orderNum="35")
    private Float accommondationFee;
    @Excel(name = "水电费",orderNum="36")
    private Float waterElectricFee;
    @Excel(name = "社保手续费",orderNum="37")
    private Float socialServiceFee;
    @Excel(name = "会费",orderNum="38")
    private Float membershipFee;
    @Excel(name = "通讯费",orderNum="39")
    private Float communicationFee;
    @Excel(name = "行政还款",orderNum="40")
    private Float adminRepay;
    @Excel(name = "其他工资扣款",orderNum="41")
    private Float otherDeduction;
    @Excel(name = "公积金",orderNum="42")
    private Float housepovidentFund;
    @Excel(name = "失保",orderNum="43")
    private Float unemployInsurance;
    @Excel(name = "养保",orderNum="44")
    private Float endowmentInsurance;
    @Excel(name = "医保",orderNum="45")
    private Float medicalInsurance;
    @Excel(name = "实发工资",orderNum="46")
    private Float netSalary;
    @Excel(name = "年月",orderNum="3")
    private String salaryDate;
    private String belongDate;
    private String departid;
    @Excel(name = "部门",orderNum="47")
    private String departName;
}