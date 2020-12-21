package business.controller;

import business.bean.HrmResource;
import business.jwt.LoginIgnore;
import business.service.IHrmResourceService;
import business.service.IPersonnelSalaryService;
import business.util.FileUtils;
import business.vo.PersonnelSalaryVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("salaryExport")
@Slf4j
public class ExcelExporttController {
    @Resource
    IPersonnelSalaryService iPersonnelSalaryService;
    @Resource
    IHrmResourceService iHrmResourceService;

    @LoginIgnore
    @GetMapping(value = "/export")
    public void exportExcel(HttpServletResponse response, @ModelAttribute PersonnelSalaryVO personnelSalaryVo,
                            @RequestParam(name="site",required = false,defaultValue = "") String site) throws Exception {
        QueryWrapper<PersonnelSalaryVO> sqlaryQueryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(personnelSalaryVo.getWorkcode())) {
            sqlaryQueryWrapper.eq("workcode", personnelSalaryVo.getWorkcode());
        }
        if(StringUtils.isNotBlank(personnelSalaryVo.getDept())){
            sqlaryQueryWrapper.eq("DEPARTID", personnelSalaryVo.getDept());
        }
        if(StringUtils.isNotBlank(personnelSalaryVo.getSalarystamonth())){
            sqlaryQueryWrapper.ge("salary_date", personnelSalaryVo.getSalarystamonth());
        }
        if(StringUtils.isNotBlank(personnelSalaryVo.getSalaryendmonth())){
            sqlaryQueryWrapper.le("salary_date", personnelSalaryVo.getSalaryendmonth());
        }
        List<PersonnelSalaryVO> personnelSalaryVoList = iPersonnelSalaryService.getPersonnelSalaryList(sqlaryQueryWrapper,site);
        //导出
        FileUtils.exportExcel(personnelSalaryVoList,PersonnelSalaryVO.class,"薪资.xls",response);
    }
}
