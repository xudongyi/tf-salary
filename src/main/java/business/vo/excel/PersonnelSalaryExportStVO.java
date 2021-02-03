package business.vo.excel;

import business.vo.PersonnelSalaryVO;
import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class PersonnelSalaryExportStVO{
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
    public Float pieceOverPay;
    public Float ondutyPay;
    @Excel(name = "技能工资",orderNum="16")
    public Float skillPay;
    @Excel(name = "交通补贴",orderNum="17")
    public Float trafficSubsidy;
    public Float increasingDayPay;
    @Excel(name = "三班倒工龄工资",orderNum="19")
    public Float threeShiftsPay;
    @Excel(name = "带徒津贴",orderNum="20")
    public Float apprenticeSubsidy;
    public Float collegeSubsidy;
    public Float groupLeaderSubsidy;
    @Excel(name = "中夜班费及补贴",orderNum="23")
    public Float nightShiftSubsidy;
    public Float sickPay;
    @Excel(name = "增补",orderNum="25")
    public Float supplementPay;
    @Excel(name = "净化费",orderNum="26")
    public Float purificationSubsidy;
    @Excel(name = "综合津贴",orderNum="27")
    public Float comprehensiveAllowance;
    @Excel(name = "工序津贴",orderNum="28")
    public Float processAllowance;
    @Excel(name = "质量奖",orderNum="29")
    public Float qualityAward;
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
    @Excel(name = "大病保",orderNum="48")
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

    public PersonnelSalaryExportStVO transModel(PersonnelSalaryVO personnelSalaryVO){
        PersonnelSalaryExportStVO PersonnelSalaryExportStVO = new PersonnelSalaryExportStVO();
        PersonnelSalaryExportStVO.setWorkcode(personnelSalaryVO.getWorkcode());
        PersonnelSalaryExportStVO.setHrName(personnelSalaryVO.getHrName());
        PersonnelSalaryExportStVO.setSalaryDate(personnelSalaryVO.getSalaryDate());
        PersonnelSalaryExportStVO.setBasePay(personnelSalaryVO.getBasePay());
        PersonnelSalaryExportStVO.setAssessmentPay(personnelSalaryVO.getAssessmentPay());
        PersonnelSalaryExportStVO.setOvertimePay(personnelSalaryVO.getOvertimePay());
        PersonnelSalaryExportStVO.setPieceRatePay(personnelSalaryVO.getPieceRatePay());
        PersonnelSalaryExportStVO.setPieceOverPay(personnelSalaryVO.getPieceOverPay());
        PersonnelSalaryExportStVO.setOndutyPay(personnelSalaryVO.getOndutyPay());
        PersonnelSalaryExportStVO.setSkillPay(personnelSalaryVO.getSkillPay());
        PersonnelSalaryExportStVO.setTrafficSubsidy(personnelSalaryVO.getTrafficSubsidy());
        PersonnelSalaryExportStVO.setIncreasingDayPay(personnelSalaryVO.getIncreasingDayPay());
        PersonnelSalaryExportStVO.setThreeShiftsPay(personnelSalaryVO.getThreeShiftsPay());
        PersonnelSalaryExportStVO.setApprenticeSubsidy(personnelSalaryVO.getApprenticeSubsidy());
        PersonnelSalaryExportStVO.setCollegeSubsidy(personnelSalaryVO.getCollegeSubsidy());
        PersonnelSalaryExportStVO.setGroupLeaderSubsidy(personnelSalaryVO.getGroupLeaderSubsidy());
        PersonnelSalaryExportStVO.setNightShiftSubsidy(personnelSalaryVO.getNightShiftSubsidy());
        PersonnelSalaryExportStVO.setSickPay(personnelSalaryVO.getSickPay());
        PersonnelSalaryExportStVO.setSupplementPay(personnelSalaryVO.getSupplementPay());
        PersonnelSalaryExportStVO.setPurificationSubsidy(personnelSalaryVO.getPurificationSubsidy());
        PersonnelSalaryExportStVO.setComprehensiveAllowance(personnelSalaryVO.getComprehensiveAllowance());
        PersonnelSalaryExportStVO.setProcessAllowance(personnelSalaryVO.getProcessAllowance());
        PersonnelSalaryExportStVO.setQualityAward(personnelSalaryVO.getQualityAward());
        PersonnelSalaryExportStVO.setPlasticSealSubsidy(personnelSalaryVO.getPlasticSealSubsidy());
        PersonnelSalaryExportStVO.setSubsidy(personnelSalaryVO.getSubsidy());
        PersonnelSalaryExportStVO.setOtherSubsidy(personnelSalaryVO.getOtherSubsidy());
        PersonnelSalaryExportStVO.setGrossPay(personnelSalaryVO.getGrossPay());
        PersonnelSalaryExportStVO.setSickDeduction(personnelSalaryVO.getSickDeduction());
        PersonnelSalaryExportStVO.setIncomeTax(personnelSalaryVO.getIncomeTax());
        PersonnelSalaryExportStVO.setSupplementaryTax(personnelSalaryVO.getSupplementaryTax());
        PersonnelSalaryExportStVO.setAccommondationFee(personnelSalaryVO.getAccommondationFee());
        PersonnelSalaryExportStVO.setWaterElectricFee(personnelSalaryVO.getWaterElectricFee());
        PersonnelSalaryExportStVO.setSocialServiceFee(personnelSalaryVO.getSocialServiceFee());
        PersonnelSalaryExportStVO.setMembershipFee(personnelSalaryVO.getMembershipFee());
        PersonnelSalaryExportStVO.setCommunicationFee(personnelSalaryVO.getCommunicationFee());
        PersonnelSalaryExportStVO.setAdminRepay(personnelSalaryVO.getAdminRepay());
        PersonnelSalaryExportStVO.setOtherDeduction(personnelSalaryVO.getOtherDeduction());
        PersonnelSalaryExportStVO.setHousepovidentFund(personnelSalaryVO.getHousepovidentFund());
        PersonnelSalaryExportStVO.setUnemployInsurance(personnelSalaryVO.getUnemployInsurance());
        PersonnelSalaryExportStVO.setEndowmentInsurance(personnelSalaryVO.getEndowmentInsurance());
        PersonnelSalaryExportStVO.setMedicalInsurance(personnelSalaryVO.getMedicalInsurance());
        PersonnelSalaryExportStVO.setSeriousIllnessInsurance(personnelSalaryVO.getSeriousIllnessInsurance());
        PersonnelSalaryExportStVO.setNetSalary(personnelSalaryVO.getNetSalary());
        PersonnelSalaryExportStVO.setDepartName(personnelSalaryVO.getDepartName());
        PersonnelSalaryExportStVO.setWelfareAmountSalaries(personnelSalaryVO.getWelfareAmountSalaries());
        PersonnelSalaryExportStVO.setWelfareAmountBonus(personnelSalaryVO.getWelfareAmountBonus());
        PersonnelSalaryExportStVO.setWelfareAmountWeal(personnelSalaryVO.getWelfareAmountWeal());
        return PersonnelSalaryExportStVO;
    }
}
