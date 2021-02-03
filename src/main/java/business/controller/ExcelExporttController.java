package business.controller;

import business.bean.HrmResource;
import business.common.api.vo.Result;
import business.jwt.LoginIgnore;
import business.service.IHrmResourceService;
import business.service.IPersonnelSalaryService;
import business.util.EasyPoiUtil;
import business.util.FileUtils;
import business.vo.PersonnelSalaryVO;
import business.vo.excel.PersonnelSalaryExportChVO;
import business.vo.excel.PersonnelSalaryExportStVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("salaryExport")
@Slf4j
public class ExcelExporttController {
    @Resource
    IPersonnelSalaryService iPersonnelSalaryService;
    @Resource
    IHrmResourceService iHrmResourceService;
    @Value("${site.CH}")
    public String ch;
    @Value("${site.ST}")
    public String st;

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

        if(site.equals(ch)){
            List<PersonnelSalaryExportChVO> personnelSalaryExportChVOList = new ArrayList<PersonnelSalaryExportChVO>();
            PersonnelSalaryExportChVO personnelSalaryExportChVO = new PersonnelSalaryExportChVO();
            for(PersonnelSalaryVO vo:personnelSalaryVoList){
                personnelSalaryExportChVO = personnelSalaryExportChVO.transModel(vo);
                personnelSalaryExportChVOList.add(personnelSalaryExportChVO);
            }
            FileUtils.exportExcel(personnelSalaryExportChVOList, PersonnelSalaryExportChVO.class,"薪资.xls",response);
        }else if(site.equals(st)){
            List<PersonnelSalaryExportStVO> personnelSalaryExportStVOList = new ArrayList<PersonnelSalaryExportStVO>();
            PersonnelSalaryExportStVO personnelSalaryExportStVO = new PersonnelSalaryExportStVO();
            for(PersonnelSalaryVO vo:personnelSalaryVoList){
                personnelSalaryExportStVO = personnelSalaryExportStVO.transModel(vo);
                personnelSalaryExportStVOList.add(personnelSalaryExportStVO);
            }
            FileUtils.exportExcel(personnelSalaryExportStVOList, PersonnelSalaryExportStVO.class,"薪资.xls",response);
        }
    }
}
