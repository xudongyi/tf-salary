package business.controller;

import business.bean.HrmResource;
import business.common.api.vo.Result;
import business.jwt.LoginRequired;
import business.mapper.HrMapper;
import business.service.IHrmResourceService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
