package business.controller;

import business.bean.SalaryReportConfig;
import business.common.api.vo.Result;
import business.jwt.LoginIgnore;
import business.service.ISalaryReportConfigService;
import business.vo.SalaryReportConfigVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("report/config")
@Slf4j
public class SalaryReportConfigController {

    @Resource
    ISalaryReportConfigService iSalaryReportConfigService;
    /**
     * 分页列表查询
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    @LoginIgnore
    public Result<?> queryPageList(
            @ModelAttribute SalaryReportConfigVo salaryReportConfigVo,
            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        IPage<SalaryReportConfig> page = new Page<>(pageNo, pageSize);
        QueryWrapper<SalaryReportConfig> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(salaryReportConfigVo.getSite())){
            queryWrapper.eq("t2.ID", salaryReportConfigVo.getSite());
        }
        if(StringUtils.isNotBlank(salaryReportConfigVo.getTabId())){
            queryWrapper.eq("t1.TAB_ID", salaryReportConfigVo.getTabId());
        }
        return Result.ok(iSalaryReportConfigService.query(page,queryWrapper));
    }


    @PostMapping(value = "/save")
    @LoginIgnore
    public Result<?> save(
            @RequestBody SalaryReportConfigVo salaryReportConfigVo) {
        return Result.ok(iSalaryReportConfigService.saveSalaryReportConfig(salaryReportConfigVo));
    }

    @GetMapping(value = "/removeSalaryReportConfig")
    @LoginIgnore
    public Result<?> removeSalaryReportConfig(
            String id) {
        return Result.ok(iSalaryReportConfigService.removeSalaryReportConfig(id));
    }

    @GetMapping(value = "/getSalaryReportConfig")
    @LoginIgnore
    public Result<?> getSalaryReportConfig(
            String id) {
        return Result.ok(iSalaryReportConfigService.getSalaryReportConfig(id));
    }

}
