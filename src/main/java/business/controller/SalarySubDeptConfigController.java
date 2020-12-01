package business.controller;

import business.bean.SalarySubDeptConfig;
import business.common.api.vo.Result;
import business.jwt.LoginIgnore;
import business.service.ISalarySubDeptConfigService;
import business.vo.SalarySubDeptConfigVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("subdept/config")
@Slf4j
public class SalarySubDeptConfigController {

    @Resource
    ISalarySubDeptConfigService iSalarySubDeptConfigService;
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
            @ModelAttribute SalarySubDeptConfigVo salarySubDeptConfigVo,
            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        IPage<SalarySubDeptConfig> page = new Page<>(pageNo, pageSize);
        QueryWrapper<SalarySubDeptConfig> queryWrapper = new QueryWrapper<>();
        return Result.ok(iSalarySubDeptConfigService.query(page,queryWrapper));
    }


    @PostMapping(value = "/save")
    @LoginIgnore
    public Result<?> save(
            @RequestBody SalarySubDeptConfigVo salarySubDeptConfigVo) {
        return Result.ok(iSalarySubDeptConfigService.saveSalarySubDeptConfig(salarySubDeptConfigVo));
    }

    @GetMapping(value = "/removeSalarySubDeptConfig")
    @LoginIgnore
    public Result<?> removeSalarySubDeptConfig(
            String id) {
        return Result.ok(iSalarySubDeptConfigService.removeSalarySubDeptConfig(id));
    }

    @GetMapping(value = "/getSalarySubDeptConfig")
    @LoginIgnore
    public Result<?> getSalarySubDeptConfig(
            String id) {
        return Result.ok(iSalarySubDeptConfigService.getSalarySubDeptConfig(id));
    }

}
