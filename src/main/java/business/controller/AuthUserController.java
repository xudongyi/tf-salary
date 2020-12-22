package business.controller;

import business.bean.AuthUser;
import business.common.api.vo.Result;
import business.jwt.LoginIgnore;
import business.service.IOauthService;
import business.vo.AuthUserVO;
import business.service.IAuthUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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

    @LoginIgnore
    @PostMapping("/admin/login")
    public Result<?> login(@RequestBody AuthUserVO authUserVO) {
        return iOauthService.login(authUserVO);
    }

    @PostMapping("/admin/checkSite")
    public Result<?> checkSite(@RequestBody AuthUserVO authUserVO) {
        return Result.ok(authUserService.list(new LambdaQueryWrapper<AuthUser>().eq(AuthUser::getSite, authUserVO.getSite())));
    }

}
