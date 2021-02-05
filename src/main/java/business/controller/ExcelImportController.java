package business.controller;

import business.bean.PersonnelSalary;
import business.bean.PersonnelWelfare;
import business.common.api.vo.Result;
import business.service.IHRService;
import business.service.IPersonnelSalaryService;
import business.service.IPersonnelWelfareService;
import business.util.FileUtils;
import com.alibaba.fastjson.JSONObject;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.executor.BatchExecutorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("import")
@Slf4j
public class ExcelImportController {
    @Resource
    private IPersonnelSalaryService personnelSalaryService;

    @Resource
    private IPersonnelWelfareService personnelWelfareService;

    @Resource
    private IHRService iHRService;

    @Value("${site.CH}")
    public String ch;
    @Value("${site.ST}")
    public String st;

    @RequestMapping(value = "/salaryImport", method = RequestMethod.POST)
    public Result<?> salaryImportExcel(@RequestParam("file") MultipartFile file,
                                       @RequestParam("belongDate") String belongDate,
                                       @RequestParam("grantDate") String grantDate,
                                       @RequestParam("importType") String importType,
                                       @RequestParam("site") String site) throws Exception {
        List<PersonnelSalary> salaryList = FileUtils.importExcel(file, 0,1, PersonnelSalary.class);
        List<PersonnelSalary> importList = new ArrayList<PersonnelSalary>();
        Map departCodeMap = iHRService.getAllHrmResourceByDate(belongDate,site);
        try {
            if(!StringUtils.isNotBlank(site)){
                return Result.error("当前管理员分部不存在！");
            }else if(!site.equals(ch)&&!site.equals(st)){
                return Result.error("当前管理员分部未对接薪资系统，请联系系统管理员！");
            }
            if(importType.equals("0")){
                //全量导入,需要把当前导入管理员所在分部的人员数据全部清掉（发放日期相同的数据）
                personnelSalaryService.deleteSalaryBySiteAndDate(site,belongDate,grantDate);
            }


            for(int i=0;i<salaryList.size();i++){
                salaryList.get(i).setSalaryDate(grantDate);
                salaryList.get(i).setBelongDate(belongDate);
                if((salaryList.get(i).getWorkcode()==null||salaryList.get(i).getWorkcode().equals(""))
                        &&(salaryList.get(i).getBasePay()==null||salaryList.get(i).getBasePay().equals(""))
                        &&(salaryList.get(i).getAssessmentPay()==null||salaryList.get(i).getAssessmentPay().equals(""))
                        &&(salaryList.get(i).getOvertimePay()==null||salaryList.get(i).getOvertimePay().equals(""))){
                    break;
                }else if(salaryList.get(i).getWorkcode()==null||salaryList.get(i).getWorkcode().equals("")){
                    return Result.error("第"+(i+2)+"行工号不能为空！");
                }else if((salaryList.get(i).getBasePay()==null||salaryList.get(i).getBasePay().equals(""))||
                        (salaryList.get(i).getAssessmentPay()==null||salaryList.get(i).getAssessmentPay().equals(""))||
                        (salaryList.get(i).getOvertimePay()==null||salaryList.get(i).getOvertimePay().equals(""))||
                        (salaryList.get(i).getPieceRatePay()==null||salaryList.get(i).getPieceRatePay().equals(""))||
                        (salaryList.get(i).getSkillPay()==null||salaryList.get(i).getSkillPay().equals(""))||
                        (salaryList.get(i).getTrafficSubsidy()==null||salaryList.get(i).getTrafficSubsidy().equals(""))||
                        (salaryList.get(i).getThreeShiftsPay()==null||salaryList.get(i).getThreeShiftsPay().equals(""))||
                        (salaryList.get(i).getApprenticeSubsidy()==null||salaryList.get(i).getApprenticeSubsidy().equals(""))||
                        (salaryList.get(i).getNightShiftSubsidy()==null||salaryList.get(i).getNightShiftSubsidy().equals(""))||
                        (salaryList.get(i).getSupplementPay()==null||salaryList.get(i).getSupplementPay().equals(""))||
                        (salaryList.get(i).getPurificationSubsidy()==null||salaryList.get(i).getPurificationSubsidy().equals(""))||
                        (salaryList.get(i).getQualityAward()==null||salaryList.get(i).getQualityAward().equals(""))||
                        (salaryList.get(i).getSubsidy()==null||salaryList.get(i).getSubsidy().equals(""))||
                        (salaryList.get(i).getOtherSubsidy()==null||salaryList.get(i).getOtherSubsidy().equals(""))||
                        (salaryList.get(i).getGrossPay()==null||salaryList.get(i).getGrossPay().equals(""))||
                        (salaryList.get(i).getSickDeduction()==null||salaryList.get(i).getSickDeduction().equals(""))||
                        (salaryList.get(i).getIncomeTax()==null||salaryList.get(i).getIncomeTax().equals(""))||
                        (salaryList.get(i).getSupplementaryTax()==null||salaryList.get(i).getSupplementaryTax().equals(""))||
                        (salaryList.get(i).getAccommondationFee()==null||salaryList.get(i).getAccommondationFee().equals(""))||
                        (salaryList.get(i).getWaterElectricFee()==null||salaryList.get(i).getWaterElectricFee().equals(""))||
                        (salaryList.get(i).getSocialServiceFee()==null||salaryList.get(i).getSocialServiceFee().equals(""))||
                        (salaryList.get(i).getMembershipFee()==null||salaryList.get(i).getMembershipFee().equals(""))||
                        (salaryList.get(i).getCommunicationFee()==null||salaryList.get(i).getCommunicationFee().equals(""))||
                        (salaryList.get(i).getAdminRepay()==null||salaryList.get(i).getAdminRepay().equals(""))||
                        (salaryList.get(i).getOtherDeduction()==null||salaryList.get(i).getOtherDeduction().equals(""))||
                        (salaryList.get(i).getHousepovidentFund()==null||salaryList.get(i).getHousepovidentFund().equals(""))||
                        (salaryList.get(i).getUnemployInsurance()==null||salaryList.get(i).getUnemployInsurance().equals(""))||
                        (salaryList.get(i).getEndowmentInsurance()==null||salaryList.get(i).getEndowmentInsurance().equals(""))||
                        (salaryList.get(i).getMedicalInsurance()==null||salaryList.get(i).getMedicalInsurance().equals(""))||
                        (salaryList.get(i).getNetSalary()==null||salaryList.get(i).getNetSalary().equals(""))){
                    return Result.error("第"+(i+2)+"行薪资数据不能为空！");
                }else if(((salaryList.get(i).getPieceOverPay()==null||salaryList.get(i).getPieceOverPay().equals(""))||
                        (salaryList.get(i).getOndutyPay()==null||salaryList.get(i).getOndutyPay().equals(""))||
                        (salaryList.get(i).getIncreasingDayPay()==null||salaryList.get(i).getIncreasingDayPay().equals(""))||
                        (salaryList.get(i).getCollegeSubsidy()==null||salaryList.get(i).getCollegeSubsidy().equals(""))||
                        (salaryList.get(i).getGroupLeaderSubsidy()==null||salaryList.get(i).getGroupLeaderSubsidy().equals(""))||
                        (salaryList.get(i).getSickPay()==null||salaryList.get(i).getSickPay().equals(""))||
                        (salaryList.get(i).getPlasticSealSubsidy()==null||salaryList.get(i).getPlasticSealSubsidy().equals("")))&&site.equals(ch)){
                    return Result.error("第"+(i+2)+"行薪资数据不能为空！");
                }else if(((salaryList.get(i).getComprehensiveAllowance()==null||salaryList.get(i).getComprehensiveAllowance().equals(""))||
                        (salaryList.get(i).getProcessAllowance()==null||salaryList.get(i).getProcessAllowance().equals(""))||
                        (salaryList.get(i).getSeriousIllnessInsurance()==null||salaryList.get(i).getSeriousIllnessInsurance().equals("")))&&site.equals(st)){
                    return Result.error("第"+(i+2)+"行薪资数据不能为空！");
                }
                if(departCodeMap.get(salaryList.get(i).getWorkcode())!=null){
                    salaryList.get(i).setDepartid(departCodeMap.get(salaryList.get(i).getWorkcode()).toString());
                }else{
                    System.out.println("JSON:"+ JSONObject.toJSON(salaryList.get(i)));
                    //根据工号取不到部门数据就按照导入的部门来
                    if(!StringUtils.isNotBlank(salaryList.get(i).getDepartName())){
                        return Result.error("第"+(i+2)+"行匹配不到部门，请导入EXCEL时填写部门！");
                    }
                    List<Map<String,Object>> deptInfoList = iHRService.getDeptByDepartName(salaryList.get(i).getDepartName(),belongDate,site);
                    if(deptInfoList.size()==0){
                        return Result.error("第"+(i+2)+"行部门名称不存在！");
                    }else if(deptInfoList.size()>1){
                        return Result.error("第"+(i+2)+"行部门名称存在重复！");
                    }else{
                        salaryList.get(i).setDepartid(deptInfoList.get(0).get("DEPARTID").toString());
                    }
                }
                //数据处理为0
                if(site.equals(ch)){
                    salaryList.get(i).setComprehensiveAllowance(0.00f);
                    salaryList.get(i).setProcessAllowance(0.00f);
                    salaryList.get(i).setSeriousIllnessInsurance(0.00f);
                }else if(site.equals(ch)){
                    salaryList.get(i).setPieceOverPay(0.00f);
                    salaryList.get(i).setOndutyPay(0.00f);
                    salaryList.get(i).setIncreasingDayPay(0.00f);
                    salaryList.get(i).setCollegeSubsidy(0.00f);
                    salaryList.get(i).setGroupLeaderSubsidy(0.00f);
                    salaryList.get(i).setSickPay(0.00f);
                    salaryList.get(i).setPlasticSealSubsidy(0.00f);
                }
                importList.add(salaryList.get(i));
            }
            personnelSalaryService.saveBatch(importList);
        } catch(PersistenceException e){
            if(e.getCause()!=null && "org.apache.ibatis.executor.BatchExecutorException".equals(e.getCause().getClass().getName())){
                // 唯一约束异常
                BatchExecutorException executorException = (BatchExecutorException)e.getCause();
                return Result.error("第"+(executorException.getBatchUpdateException().getUpdateCounts().length+2)+"行工号当前月薪资存在重复！");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return Result.error("上传失败！");
        }
        return Result.ok();
    }

    @RequestMapping(value = "/welfareImport", method = RequestMethod.POST)
    public Result<?> welfareImportExcel(@RequestParam("file") MultipartFile file) throws Exception {
        List<PersonnelWelfare> welfareList = FileUtils.importExcel(file, 0,1, PersonnelWelfare.class);
        List<PersonnelWelfare> importList = new ArrayList<PersonnelWelfare>();
        Map deartCodeMap = iHRService.getAllHrmResource();

        try {
            for(int i=0;i<welfareList.size();i++){
                if((welfareList.get(i).getWorkcode()==null||welfareList.get(i).getWorkcode().equals(""))
                        &&(welfareList.get(i).getWelfareTypeName()==null||welfareList.get(i).getWelfareTypeName().equals(""))
                        &&(welfareList.get(i).getWelfareAmount()==null||welfareList.get(i).getWelfareAmount().equals(""))
                        &&(welfareList.get(i).getWelfareDate()==null||welfareList.get(i).getWelfareDate().equals(""))){
                    break;
                }else if(welfareList.get(i).getWorkcode()==null||welfareList.get(i).getWorkcode().equals("")){
                    return Result.error("第"+(i+2)+"行工号不能为空！");
                }else if(welfareList.get(i).getWelfareTypeName()==null||welfareList.get(i).getWelfareTypeName().equals("")){
                    return Result.error("第"+(i+2)+"名称不能为空！");
                }else if(welfareList.get(i).getWelfareAmount()==null||welfareList.get(i).getWelfareAmount().equals("")){
                    return Result.error("第"+(i+2)+"金额不能为空！");
                }else if(welfareList.get(i).getWelfareDate()==null||welfareList.get(i).getWelfareDate().equals("")){
                    return Result.error("第"+(i+2)+"发放时间不能为空！");
                }

                    if(welfareList.get(i).getWelfareTypeName().equals("十三薪")){
                        welfareList.get(i).setWelfareType(1);
                        welfareList.get(i).setWelfareAmountSalaries(welfareList.get(i).getWelfareAmount());
                        welfareList.get(i).setWelfareAmountBonus(0f);
                        welfareList.get(i).setWelfareAmountWeal(0f);
                    }else if(welfareList.get(i).getWelfareTypeName().equals("年终奖")){
                    welfareList.get(i).setWelfareType(3);
                    welfareList.get(i).setWelfareAmountSalaries(0f);
                    welfareList.get(i).setWelfareAmountBonus(welfareList.get(i).getWelfareAmount());
                    welfareList.get(i).setWelfareAmountWeal(0f);
                }else if(welfareList.get(i).getWelfareTypeName().equals("半年奖")){
                    welfareList.get(i).setWelfareType(2);
                    welfareList.get(i).setWelfareAmountSalaries(0f);
                    welfareList.get(i).setWelfareAmountBonus(welfareList.get(i).getWelfareAmount());
                    welfareList.get(i).setWelfareAmountWeal(0f);
                }else{
                    welfareList.get(i).setWelfareType(4);
                    welfareList.get(i).setWelfareAmountSalaries(0f);
                    welfareList.get(i).setWelfareAmountBonus(0f);
                    welfareList.get(i).setWelfareAmountWeal(welfareList.get(i).getWelfareAmount());
                }
                if(deartCodeMap.get(welfareList.get(i).getWorkcode())!=null){
                    welfareList.get(i).setDepartid(deartCodeMap.get(welfareList.get(i).getWorkcode()).toString());
                }
                importList.add(welfareList.get(i));
            }
            personnelWelfareService.saveBatch(importList);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return Result.error("上传失败！");
        }
        return Result.ok();
    }
}
