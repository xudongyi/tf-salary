package business.controller;

import business.bean.AuthUser;
import business.bean.HrmResource;
import business.common.api.vo.Result;
import business.service.IAuthUserService;
import business.service.IHrmResourceService;
import business.service.IPersonnelSalaryService;
import business.util.CaptchaUtil;
import business.vo.AuthUserModify;
import business.vo.PersonnelSalaryVO;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    IHrmResourceService iHrmResourceService;
    @Resource
    IAuthUserService iAuthUserService;
    /**
     * 分页列表查询
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/query")
    public Result<?> queryPageList(
            @ModelAttribute PersonnelSalaryVO personnelSalaryVo,
            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
            HttpServletRequest req) {
        IPage<PersonnelSalaryVO> page = new Page<PersonnelSalaryVO>(pageNo, pageSize);
        QueryWrapper<PersonnelSalaryVO> sqlaryQueryWrapper = new QueryWrapper<>();
        QueryWrapper<HrmResource> hrmQueryWrapper = new QueryWrapper<>();
        //sqlaryQueryWrapper.lambda().orderByDesc(PersonnelSalary::getId);
        if (StringUtils.isNotBlank(personnelSalaryVo.getWorkcode())) {
            sqlaryQueryWrapper.eq("t1.workcode", personnelSalaryVo.getWorkcode());
        }
        if(StringUtils.isNotBlank(personnelSalaryVo.getDept())){
            hrmQueryWrapper.lambda().eq(HrmResource::getDepartmentid,personnelSalaryVo.getDept().split("_")[0]);
            List<HrmResource> hrmList = iHrmResourceService.getBaseMapper().selectList(hrmQueryWrapper);
            String[] hrmDepartmentIdArr = new String[hrmList.size()];
            for(int i = 0; i < hrmList.size();i++){
                hrmDepartmentIdArr[i] = hrmList.get(i).getDepartmentid();
            }
            sqlaryQueryWrapper.in("t2.departmentid",hrmDepartmentIdArr);
        }
        if(StringUtils.isNotBlank(personnelSalaryVo.getSalarystamonth())){
            sqlaryQueryWrapper.ge("t1.salary_date", personnelSalaryVo.getSalarystamonth());
        }
        if(StringUtils.isNotBlank(personnelSalaryVo.getSalaryendmonth())){
            sqlaryQueryWrapper.le("t1.salary_date", personnelSalaryVo.getSalaryendmonth());
        }
        return Result.ok(iPersonnelSalaryService.getPersonnelSalaryList(page,sqlaryQueryWrapper));
    }

    /**
     * 校验密码
     * */
    @RequestMapping(value = "/checkPassword", method = RequestMethod.POST)
    public Result<?> checkPassword(HttpServletRequest httpServletRequest,@RequestBody AuthUserModify authUserModify) throws Exception {
        if(authUserModify==null || StringUtils.isBlank(authUserModify.getCaptcha())
                ||StringUtils.isBlank( authUserModify.getWorkcode()) || authUserModify.getPassword()==null){
            return Result.error(500,"参数错误！");
        }
        //1.验证手机号和验证码是否匹配
        String result = CaptchaUtil.validate(authUserModify.getMobile(),authUserModify.getCaptcha());
        if(!result.equals("")){
            return Result.error(500,result);
        }

        QueryWrapper<AuthUser> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("workcode",authUserModify.getWorkcode());
        userQueryWrapper.eq("password", SecureUtil.md5(authUserModify.getPassword()));
        List<AuthUser> userList = iAuthUserService.list(userQueryWrapper);
        if(userList.size()==0){
            return Result.error(500,"密码输入错误！");
        }
        return Result.ok();
    }
}
