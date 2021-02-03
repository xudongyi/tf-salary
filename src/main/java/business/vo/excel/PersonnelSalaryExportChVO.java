package business.vo.excel;

import business.bean.PersonnelSalary;
import business.vo.PersonnelSalaryVO;
import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class PersonnelSalaryExportChVO{
    @Excel(name = "工号",orderNum="1")
    public String workcode;
    @Excel(name = "姓名",orderNum="2")
    public String hrName;
    @Excel(name = "年月",orderNum="3")
    public String salaryDate;
    @Excel(name = "基本工资",orderNum="10")
    public Float basePay;
    @Excel(name = "考核工资",orderNum="11")
    public Float assessmentPay;
    @Excel(name = "加班工资",orderNum="12")
    public Float overtimePay;
    @Excel(name = "计件工资",orderNum="13")
    public Float pieceRatePay;
    @Excel(name = "计加班工资",orderNum="14")
    public Float pieceOverPay;
    @Excel(name = "上岗工资",orderNum="15")
    public Float ondutyPay;
    @Excel(name = "技能工资",orderNum="16")
    public Float skillPay;
    @Excel(name = "交通补贴",orderNum="17")
    public Float trafficSubsidy;
    @Excel(name = "增日工资",orderNum="18")
    public Float increasingDayPay;
    @Excel(name = "三班倒工龄工资",orderNum="19")
    public Float threeShiftsPay;
    @Excel(name = "带徒津贴",orderNum="20")
    public Float apprenticeSubsidy;
    @Excel(name = "大学生补贴",orderNum="21")
    public Float collegeSubsidy;
    @Excel(name = "组长补贴",orderNum="22")
    public Float groupLeaderSubsidy;
    @Excel(name = "中夜班费及补贴",orderNum="23")
    public Float nightShiftSubsidy;
    @Excel(name = "病假工资",orderNum="24")
    public Float sickPay;
    @Excel(name = "增补",orderNum="25")
    public Float supplementPay;
    @Excel(name = "净化费",orderNum="26")
    public Float purificationSubsidy;
    public Float comprehensiveAllowance;
    public Float processAllowance;
    @Excel(name = "质量奖",orderNum="29")
    public Float qualityAward;
    @Excel(name = "塑封补贴",orderNum="30")
    public Float plasticSealSubsidy;
    @Excel(name = "补贴",orderNum="31")
    public Float subsidy;
    @Excel(name = "其他补贴",orderNum="32")
    public Float otherSubsidy;
    @Excel(name = "应发工资",orderNum="33")
    public Float grossPay;
    @Excel(name = "病假扣款",orderNum="34")
    public Float sickDeduction;
    @Excel(name = "个税",orderNum="35")
    public Float incomeTax;
    @Excel(name = "补扣税",orderNum="36")
    public Float supplementaryTax;
    @Excel(name = "住宿费",orderNum="37")
    public Float accommondationFee;
    @Excel(name = "水电费",orderNum="38")
    public Float waterElectricFee;
    @Excel(name = "社保手续费",orderNum="39")
    public Float socialServiceFee;
    @Excel(name = "会费",orderNum="40")
    public Float membershipFee;
    @Excel(name = "通讯费",orderNum="41")
    public Float communicationFee;
    @Excel(name = "行政还款",orderNum="42")
    public Float adminRepay;
    @Excel(name = "其他工资扣款",orderNum="43")
    public Float otherDeduction;
    @Excel(name = "公积金",orderNum="44")
    public Float housepovidentFund;
    @Excel(name = "失保",orderNum="45")
    public Float unemployInsurance;
    @Excel(name = "养保",orderNum="46")
    public Float endowmentInsurance;
    @Excel(name = "医保",orderNum="47")
    public Float medicalInsurance;
    public Float seriousIllnessInsurance;
    @Excel(name = "实发工资",orderNum="49")
    public Float netSalary;
    @Excel(name = "部门",orderNum="50")
    public String departName;
    @Excel(name = "十三薪",orderNum="51")
    public String welfareAmountSalaries;
    @Excel(name = "年终奖",orderNum="52")
    public String welfareAmountBonus;
    @Excel(name = "福利",orderNum="53")
    public String welfareAmountWeal;

    public PersonnelSalaryExportChVO transModel(PersonnelSalaryVO personnelSalaryVO){
        PersonnelSalaryExportChVO personnelSalaryExportChVO = new PersonnelSalaryExportChVO();
        personnelSalaryExportChVO.setWorkcode(personnelSalaryVO.getWorkcode());
        personnelSalaryExportChVO.setHrName(personnelSalaryVO.getHrName());
        personnelSalaryExportChVO.setSalaryDate(personnelSalaryVO.getSalaryDate());
        personnelSalaryExportChVO.setBasePay(personnelSalaryVO.getBasePay());
        personnelSalaryExportChVO.setAssessmentPay(personnelSalaryVO.getAssessmentPay());
        personnelSalaryExportChVO.setOvertimePay(personnelSalaryVO.getOvertimePay());
        personnelSalaryExportChVO.setPieceRatePay(personnelSalaryVO.getPieceRatePay());
        personnelSalaryExportChVO.setPieceOverPay(personnelSalaryVO.getPieceOverPay());
        personnelSalaryExportChVO.setOndutyPay(personnelSalaryVO.getOndutyPay());
        personnelSalaryExportChVO.setSkillPay(personnelSalaryVO.getSkillPay());
        personnelSalaryExportChVO.setTrafficSubsidy(personnelSalaryVO.getTrafficSubsidy());
        personnelSalaryExportChVO.setIncreasingDayPay(personnelSalaryVO.getIncreasingDayPay());
        personnelSalaryExportChVO.setThreeShiftsPay(personnelSalaryVO.getThreeShiftsPay());
        personnelSalaryExportChVO.setApprenticeSubsidy(personnelSalaryVO.getApprenticeSubsidy());
        personnelSalaryExportChVO.setCollegeSubsidy(personnelSalaryVO.getCollegeSubsidy());
        personnelSalaryExportChVO.setGroupLeaderSubsidy(personnelSalaryVO.getGroupLeaderSubsidy());
        personnelSalaryExportChVO.setNightShiftSubsidy(personnelSalaryVO.getNightShiftSubsidy());
        personnelSalaryExportChVO.setSickPay(personnelSalaryVO.getSickPay());
        personnelSalaryExportChVO.setSupplementPay(personnelSalaryVO.getSupplementPay());
        personnelSalaryExportChVO.setPurificationSubsidy(personnelSalaryVO.getPurificationSubsidy());
        personnelSalaryExportChVO.setComprehensiveAllowance(personnelSalaryVO.getComprehensiveAllowance());
        personnelSalaryExportChVO.setProcessAllowance(personnelSalaryVO.getProcessAllowance());
        personnelSalaryExportChVO.setQualityAward(personnelSalaryVO.getQualityAward());
        personnelSalaryExportChVO.setPlasticSealSubsidy(personnelSalaryVO.getPlasticSealSubsidy());
        personnelSalaryExportChVO.setSubsidy(personnelSalaryVO.getSubsidy());
        personnelSalaryExportChVO.setOtherSubsidy(personnelSalaryVO.getOtherSubsidy());
        personnelSalaryExportChVO.setGrossPay(personnelSalaryVO.getGrossPay());
        personnelSalaryExportChVO.setSickDeduction(personnelSalaryVO.getSickDeduction());
        personnelSalaryExportChVO.setIncomeTax(personnelSalaryVO.getIncomeTax());
        personnelSalaryExportChVO.setSupplementaryTax(personnelSalaryVO.getSupplementaryTax());
        personnelSalaryExportChVO.setAccommondationFee(personnelSalaryVO.getAccommondationFee());
        personnelSalaryExportChVO.setWaterElectricFee(personnelSalaryVO.getWaterElectricFee());
        personnelSalaryExportChVO.setSocialServiceFee(personnelSalaryVO.getSocialServiceFee());
        personnelSalaryExportChVO.setMembershipFee(personnelSalaryVO.getMembershipFee());
        personnelSalaryExportChVO.setCommunicationFee(personnelSalaryVO.getCommunicationFee());
        personnelSalaryExportChVO.setAdminRepay(personnelSalaryVO.getAdminRepay());
        personnelSalaryExportChVO.setOtherDeduction(personnelSalaryVO.getOtherDeduction());
        personnelSalaryExportChVO.setHousepovidentFund(personnelSalaryVO.getHousepovidentFund());
        personnelSalaryExportChVO.setUnemployInsurance(personnelSalaryVO.getUnemployInsurance());
        personnelSalaryExportChVO.setEndowmentInsurance(personnelSalaryVO.getEndowmentInsurance());
        personnelSalaryExportChVO.setMedicalInsurance(personnelSalaryVO.getMedicalInsurance());
        personnelSalaryExportChVO.setSeriousIllnessInsurance(personnelSalaryVO.getSeriousIllnessInsurance());
        personnelSalaryExportChVO.setNetSalary(personnelSalaryVO.getNetSalary());
        personnelSalaryExportChVO.setDepartName(personnelSalaryVO.getDepartName());
        personnelSalaryExportChVO.setWelfareAmountSalaries(personnelSalaryVO.getWelfareAmountSalaries());
        personnelSalaryExportChVO.setWelfareAmountBonus(personnelSalaryVO.getWelfareAmountBonus());
        personnelSalaryExportChVO.setWelfareAmountWeal(personnelSalaryVO.getWelfareAmountWeal());
        return personnelSalaryExportChVO;
    }
}
