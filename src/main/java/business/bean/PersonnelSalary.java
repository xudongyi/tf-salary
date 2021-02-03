package business.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * 薪资表
 * @author xudy
 * @since 2020-09-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("PERSONNEL_SALARY")
public class PersonnelSalary extends Model<PersonnelSalary> {
    @TableId(value = "id", type = IdType.AUTO)
    public Long id;
    @Excel(name = "工号",orderNum="1",isColumnHidden = true)
    public String workcode;
    @Excel(name = "基本工资",orderNum="10",isColumnHidden = true)
    public Float basePay;
    @Excel(name = "考核工资",orderNum="11",isColumnHidden = true)
    public Float assessmentPay;
    @Excel(name = "加班工资",orderNum="12",isColumnHidden = true)
    public Float overtimePay;
    @Excel(name = "计件工资",orderNum="13",isColumnHidden = true)
    public Float pieceRatePay;
    @Excel(name = "计加班工资",orderNum="14",isColumnHidden = false)
    public Float pieceOverPay;
    @Excel(name = "上岗工资",orderNum="15",isColumnHidden = false)
    public Float ondutyPay;
    @Excel(name = "技能工资",orderNum="16",isColumnHidden = false)
    public Float skillPay;
    @Excel(name = "交通补贴",orderNum="17",isColumnHidden = false)
    public Float trafficSubsidy;
    @Excel(name = "增日工资",orderNum="18",isColumnHidden = false)
    public Float increasingDayPay;
    @Excel(name = "三班倒工龄工资",orderNum="19",isColumnHidden = false)
    public Float threeShiftsPay;
    @Excel(name = "带徒津贴",orderNum="20",isColumnHidden = false)
    public Float apprenticeSubsidy;
    @Excel(name = "大学生补贴",orderNum="21",isColumnHidden = false)
    public Float collegeSubsidy;
    @Excel(name = "组长补贴",orderNum="22",isColumnHidden = false)
    public Float groupLeaderSubsidy;
    @Excel(name = "中夜班费及补贴",orderNum="23",isColumnHidden = false)
    public Float nightShiftSubsidy;
    @Excel(name = "病假工资",orderNum="24",isColumnHidden = false)
    public Float sickPay;
    @Excel(name = "增补",orderNum="25",isColumnHidden = false)
    public Float supplementPay;
    @Excel(name = "净化费",orderNum="26",isColumnHidden = false)
    public Float purificationSubsidy;
    @Excel(name = "综合津贴",orderNum="27",isColumnHidden = false)
    public Float comprehensiveAllowance;
    @Excel(name = "工序津贴",orderNum="28",isColumnHidden = false)
    public Float processAllowance;
    @Excel(name = "质量奖",orderNum="29",isColumnHidden = false)
    public Float qualityAward;
    @Excel(name = "塑封补贴",orderNum="30",isColumnHidden = false)
    public Float plasticSealSubsidy;
    @Excel(name = "补贴",orderNum="31",isColumnHidden = false)
    public Float subsidy;
    @Excel(name = "其他补贴",orderNum="32",isColumnHidden = false)
    public Float otherSubsidy;
    @Excel(name = "应发工资",orderNum="33",isColumnHidden = false)
    public Float grossPay;
    @Excel(name = "病假扣款",orderNum="34",isColumnHidden = false)
    public Float sickDeduction;
    @Excel(name = "个税",orderNum="35",isColumnHidden = false)
    public Float incomeTax;
    @Excel(name = "补扣税",orderNum="36",isColumnHidden = false)
    public Float supplementaryTax;
    @Excel(name = "住宿费",orderNum="37",isColumnHidden = false)
    public Float accommondationFee;
    @Excel(name = "水电费",orderNum="38",isColumnHidden = false)
    public Float waterElectricFee;
    @Excel(name = "社保手续费",orderNum="39",isColumnHidden = false)
    public Float socialServiceFee;
    @Excel(name = "会费",orderNum="40",isColumnHidden = false)
    public Float membershipFee;
    @Excel(name = "通讯费",orderNum="41",isColumnHidden = false)
    public Float communicationFee;
    @Excel(name = "行政还款",orderNum="42",isColumnHidden = false)
    public Float adminRepay;
    @Excel(name = "其他工资扣款",orderNum="43",isColumnHidden = false)
    public Float otherDeduction;
    @Excel(name = "公积金",orderNum="44",isColumnHidden = false)
    public Float housepovidentFund;
    @Excel(name = "失保",orderNum="45",isColumnHidden = false)
    public Float unemployInsurance;
    @Excel(name = "养保",orderNum="46",isColumnHidden = false)
    public Float endowmentInsurance;
    @Excel(name = "医保",orderNum="47",isColumnHidden = false)
    public Float medicalInsurance;
    @Excel(name = "大病保",orderNum="48",isColumnHidden = false)
    public Float seriousIllnessInsurance;
    @Excel(name = "实发工资",orderNum="49",isColumnHidden = false)
    public Float netSalary;
    @Excel(name = "年月",orderNum="3",isColumnHidden = false)
    public String salaryDate;
    public String belongDate;
    public String departid;
    @Excel(name = "部门",orderNum="50",isColumnHidden = false)
    @TableField(exist=false)
    public String departName;
}