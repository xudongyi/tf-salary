package business.controller;

import business.bean.HrmResource;
import business.bean.ManufacturingDeptConfig;
import business.common.api.vo.Result;
import business.jwt.LoginRequired;
import business.mapper.HrMapper;
import business.service.IHrmResourceService;
import business.vo.PersonnelWelfareVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("test")
@Slf4j
public class TestController {
    @Resource
    IHrmResourceService hrmResourceService;

    @RequestMapping("/test")
    public String test(){
        return "123";
    }

    @LoginRequired
    @RequestMapping("/hrmlist")
    public Result<?> queryPageList(
                                   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        Page<HrmResource> page = new Page<>(pageNo, pageSize);
        return Result.ok( hrmResourceService.page(page));
    }

    @GetMapping(value = "/queryWelfare")
    public void queryWelfarePageList() {
        ManufacturingDeptConfig m = new ManufacturingDeptConfig();
        System.out.println(m.getDepartNames());
    }
}
