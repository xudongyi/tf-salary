package business.controller;

import business.common.api.vo.Result;
import business.service.IOauthService;
import business.vo.AuthUserVO;
import business.service.IAuthUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("auth")
@Slf4j
public class AuthUserController {
    @Autowired
    private IAuthUserService authUserService;
    @Autowired
    private IOauthService iOauthService;

    @RequestMapping("/admin/login")
    public Result<?> login(@RequestBody AuthUserVO authUserVO) {
        return iOauthService.login(authUserVO);
    }
}
