package business.controller;

import business.bean.HrmResource;
import business.bean.PersonnelSalary;
import business.common.api.vo.Result;
import business.service.IHrmResourceService;
import business.service.IPersonnelSalaryService;
import business.vo.PersonnelSalaryVO;
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
    /**
     * 分页列表查询
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/query")
    public Result<?> queryPageList(
            @ModelAttribute PersonnelSalaryVO personnelSalary,
            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
            HttpServletRequest req) {
        IPage<PersonnelSalary> page = new Page<PersonnelSalary>(pageNo, pageSize);
        QueryWrapper<PersonnelSalary> sqlaryQueryWrapper = new QueryWrapper<>();
        QueryWrapper<HrmResource> hrmQueryWrapper = new QueryWrapper<>();
        sqlaryQueryWrapper.lambda().orderByDesc(PersonnelSalary::getId);
        if (StringUtils.isNotBlank(personnelSalary.getWorkcode())) {
            sqlaryQueryWrapper.lambda().eq(PersonnelSalary::getWorkcode,personnelSalary.getWorkcode());
        }
        if (StringUtils.isNotBlank(personnelSalary.getUserId())) {
            hrmQueryWrapper.lambda().eq(HrmResource::getId,personnelSalary.getUserId());
            HrmResource hrm = iHrmResourceService.getBaseMapper().selectById(personnelSalary.getUserId());
            sqlaryQueryWrapper.lambda().eq(PersonnelSalary::getWorkcode,hrm.getWorkcode());
        }
        if(StringUtils.isNotBlank(personnelSalary.getDept())){
            hrmQueryWrapper.lambda().eq(HrmResource::getDepartmentid,personnelSalary.getDept().split("_")[0]);
            List<HrmResource> hrmList = iHrmResourceService.getBaseMapper().selectList(hrmQueryWrapper);
            String[] hrmWorkcodeArr = new String[hrmList.size()];
            for(int i = 0; i < hrmList.size();i++){
                hrmWorkcodeArr[i] = hrmList.get(i).getWorkcode();
            }
            sqlaryQueryWrapper.lambda().in(PersonnelSalary::getWorkcode,hrmWorkcodeArr);
        }
        if(StringUtils.isNotBlank(personnelSalary.getSalaryMonth())){
            String salaryMonth = personnelSalary.getSalaryMonth();
            sqlaryQueryWrapper.lambda().eq(PersonnelSalary::getSalaryYear,salaryMonth.split("-")[0]).eq(PersonnelSalary::getSalaryMonth,salaryMonth.split("-")[1]);
        }
        return Result.ok(iPersonnelSalaryService.page(page,sqlaryQueryWrapper));
    }
}
