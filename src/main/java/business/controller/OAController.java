package business.controller;

import business.common.api.vo.Result;
import business.service.IOAService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("oa")
@Slf4j
public class OAController {

    @Resource
    IOAService ioaService;

    @RequestMapping("/departMentAll")
    public Result<?> departMentAll(String departMentName) {
        return ioaService.departMentAll(departMentName);
    }
    @RequestMapping("/getHrmResource")
    public Result<?> getHrmResource(@RequestParam(name="lastname", defaultValue="10") String lastname) {
        return ioaService.getHrmResource(lastname);
    }
}
