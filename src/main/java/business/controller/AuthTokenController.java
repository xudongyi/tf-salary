package business.controller;

import business.common.api.vo.Result;
import business.jwt.LoginIgnore;
import business.service.IOauthService;
import business.vo.AuthUserModify;
import business.vo.AuthUserSSO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("auth")
@Slf4j
public class AuthTokenController {
    @Resource
    private IOauthService iOauthService;
    /**
     * 获取token
     * @param sso
     * @return
     */
    @PostMapping("/user/sso")
    @LoginIgnore
    public Result<?> sso(@RequestBody AuthUserSSO sso){
        return iOauthService.sso(sso);
    }

    /**
     * 验证token
     * @param sso
     * @return
     */
    @LoginIgnore
    @PostMapping("/user/checkSso")
    public Result<?> checkSso(@RequestBody AuthUserSSO sso){
        return iOauthService.checkSso(sso);
    }

    /**
     * 校验修改密码的数据
     * @param httpServletRequest
     * @param authUserModify
     * @return
     */
    @PostMapping("/user/sendMobile")
    @LoginIgnore
    public Result<?> sendMobile(HttpServletRequest httpServletRequest, @RequestBody AuthUserModify authUserModify){
        return iOauthService.sendMobile(httpServletRequest,authUserModify);
    }

    /**
     * 修改密码
     * @param authUserModify
     * @return
     */
    @PostMapping("/user/modifyPassword")
    @LoginIgnore
    public Result<?> modifyPassword(HttpServletRequest httpServletRequest, @RequestBody AuthUserModify authUserModify){
        return iOauthService.modifyPassword(httpServletRequest,authUserModify);
    }
}
