package business.controller;

import business.common.api.vo.Result;
import business.service.IAuthUserService;
import business.service.IPersonnelSalaryService;
import business.service.IPersonnelWelfareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("salaryReport")
@Slf4j
public class SalaryReportController {
    @Resource
    IPersonnelSalaryService iPersonnelSalaryService;
    @Resource
    IPersonnelWelfareService iPersonnelWelfareService;
    @Resource
    IAuthUserService iAuthUserService;
    /**
     * 统计报表
     * @param year
     * @return
     */
    @RequestMapping(value = "/getMonthlyLaborCost", method = RequestMethod.POST)
    public Result<?> getMonthlyLaborCost(@RequestParam("year") String year,@RequestParam("rate") Float rate) {

        return Result.ok(iPersonnelSalaryService.getMonthlyLaborCost(year,rate));
    }
}