package business.controller;

import business.bean.HrmResource;
import business.service.IHrmResourceService;
import business.service.IPersonnelSalaryService;
import business.util.FileUtils;
import business.vo.PersonnelSalaryVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("salaryExport")
@Slf4j
public class SalaryExporttController {
    @Resource
    IPersonnelSalaryService iPersonnelSalaryService;
    @Resource
    IHrmResourceService iHrmResourceService;

    @GetMapping(value = "/export")
    public void exportExcel(HttpServletResponse response, @ModelAttribute PersonnelSalaryVO personnelSalaryVo) throws Exception {
        QueryWrapper<PersonnelSalaryVO> sqlaryQueryWrapper = new QueryWrapper<>();
        QueryWrapper<HrmResource> hrmQueryWrapper = new QueryWrapper<>();

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
        List<PersonnelSalaryVO> personnelSalaryVoList = iPersonnelSalaryService.getPersonnelSalaryList(sqlaryQueryWrapper);
        //导出
        FileUtils.exportExcel(personnelSalaryVoList,PersonnelSalaryVO.class,"薪资.xls",response);
    }
}
