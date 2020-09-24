package business.controller;

import business.common.api.vo.Result;
import business.service.IHRService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("hr")
@Slf4j
public class HRController {

    @Resource
    IHRService ihrService;

    @GetMapping("/departMentAll")
    public Result<?> departMentAll(String departMentName) {
        return ihrService.departMentAll(departMentName);
    }
    @GetMapping("/getHrmResource")
    public Result<?> getHrmResource(@RequestParam(name="lastname") String lastname) {
        return ihrService.getHrmResource(lastname);
    }
}
