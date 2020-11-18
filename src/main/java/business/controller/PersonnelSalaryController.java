package business.controller;

import business.bean.AuthUser;
import business.bean.PersonnelSalary;
import business.common.api.vo.Result;
import business.jwt.LoginIgnore;
import business.service.IAuthUserService;
import business.service.IPersonnelSalaryService;
import business.service.IPersonnelWelfareService;
import business.util.CaptchaUtil;
import business.vo.AuthUserModify;
import business.vo.PersonnelSalaryVO;
import business.vo.PersonnelWelfareVO;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("personnelSalary")
@Slf4j
public class PersonnelSalaryController {
    @Resource
    IPersonnelSalaryService iPersonnelSalaryService;
    @Resource
    IPersonnelWelfareService iPersonnelWelfareService;
    @Resource
    IAuthUserService iAuthUserService;
    /**
     * 分页列表查询
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/querySalary")
    public Result<?> querySalaryPageList(
            @ModelAttribute PersonnelSalaryVO personnelSalaryVo,
            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
            @RequestParam(name="site") String site,
            HttpServletRequest req) {
        return Result.ok(iPersonnelSalaryService.getPersonnelSalaryList(personnelSalaryVo,site,pageNo,pageSize));
    }

    @GetMapping(value = "/queryWelfare")
    public Result<?> queryWelfarePageList(
            @ModelAttribute PersonnelWelfareVO personnelWelfareVO,
            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
            @RequestParam(name="site") String site,
            HttpServletRequest req) {
        return Result.ok(iPersonnelWelfareService.getPersonnelWelfareList(personnelWelfareVO,site,pageNo,pageSize));
    }


    @RequestMapping(value = "/queryWelfareSingle", method = RequestMethod.POST)
    public Result<?> queryWelfareSingleList(
            @RequestBody  PersonnelWelfareVO personnelWelfareVO) {
        return Result.ok(iPersonnelWelfareService.getMonthlyWelfareSingle(personnelWelfareVO));
    }

    /**
     * 校验手机号验证码
     * */
    @RequestMapping(value = "/checMobileCaptcha", method = RequestMethod.POST)
    @LoginIgnore
    public Result<?> checMobileCaptcha(HttpServletRequest httpServletRequest,@RequestBody AuthUserModify authUserModify) throws Exception {
        if(authUserModify==null || StringUtils.isBlank(authUserModify.getCaptcha())
                || StringUtils.isBlank( authUserModify.getWorkcode()) || authUserModify.getMobile()==null|| authUserModify.getCaptcha()==null){
            return Result.error(500,"参数错误！");
        }
        //1.验证手机号和验证码是否匹配
        String result = CaptchaUtil.validate(authUserModify.getMobile(),authUserModify.getCaptcha());
        if(!result.equals("")){
            return Result.error(500,result);
        }
        return Result.ok();
    }

    /**
     * 校验密码
     * */
    @RequestMapping(value = "/checkPassword", method = RequestMethod.POST)
    public Result<?> checkPassword(HttpServletRequest httpServletRequest,@RequestBody AuthUserModify authUserModify) throws Exception {
//        if(authUserModify==null || StringUtils.isBlank(authUserModify.getCaptcha())
//                ||StringUtils.isBlank( authUserModify.getWorkcode()) || authUserModify.getPassword()==null){
//            return Result.error(500,"参数错误！");
//        }
//        //1.验证手机号和验证码是否匹配
//        String result = CaptchaUtil.validate(authUserModify.getMobile(),authUserModify.getCaptcha());
//        if(!result.equals("")){
//            return Result.error(500,result);
//        }

        QueryWrapper<AuthUser> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("workcode",authUserModify.getWorkcode());
        userQueryWrapper.eq("password", SecureUtil.md5(authUserModify.getPassword()));
        List<AuthUser> userList = iAuthUserService.list(userQueryWrapper);
        if(userList.size()==0){
            return Result.error(500,"密码输入错误！");
        }
        return Result.ok();
    }

    @RequestMapping(value = "/querySalary", method = RequestMethod.POST)
    public Result<?> querySalary(HttpServletRequest httpServletRequest,@RequestBody PersonnelSalary personnelSalary){
        PersonnelSalary salary = iPersonnelSalaryService.getOne(new LambdaQueryWrapper<PersonnelSalary>().eq(PersonnelSalary::getSalaryDate,personnelSalary.getSalaryDate()).eq(PersonnelSalary::getWorkcode,personnelSalary.getWorkcode()));
        return Result.ok(salary);
    }

    /**
     * 报表查询
     * */
    @RequestMapping(value = "/queryReportHeader", method = RequestMethod.POST)
    public Result<?> queryReportHeader(){
        return Result.ok(iPersonnelSalaryService.getReportHeader());
    }

    /**
     * 报表查询(有日期)
     * */
    @RequestMapping(value = "/queryReportBody", method = RequestMethod.POST)
    public Result<?> queryReportBody(@RequestParam(name="staDate") String staDate,@RequestParam(name="endDate") String endDate){
        return Result.ok(iPersonnelSalaryService.getReportBodyList(staDate,endDate));
    }
}
