package business.controller;

import business.common.api.vo.Result;
import business.service.IOauthService;
import business.vo.AuthUserModify;
import business.vo.AuthUserSSO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("auth")
@Slf4j
public class AuthTokenController {
    @Autowired
    private IOauthService iOauthService;

    /**
     * 获取token
     * @param sso
     * @return
     */
    @RequestMapping("/user/sso")
    public Result<?> sso(@RequestBody AuthUserSSO sso){
        return iOauthService.sso(sso);
    }

    /**
     * 验证token
     * @param sso
     * @return
     */
    @RequestMapping("/user/checkSso")
    public Result<?> checkSso(@RequestBody AuthUserSSO sso){
        return iOauthService.checkSso(sso);
    }

    /**
     * 校验修改密码的数据
     * @param httpServletRequest
     * @param authUserModify
     * @return
     */
    @RequestMapping("/user/sendMobile")
    public Result<?> sendMobile(HttpServletRequest httpServletRequest, @RequestBody AuthUserModify authUserModify){
        return iOauthService.sendMobile(httpServletRequest,authUserModify);
    }

    /**
     * 修改密码
     * @param authUserModify
     * @return
     */
    @RequestMapping("/user/modifyPassword")
    public Result<?> modifyPassword(HttpServletRequest httpServletRequest, @RequestBody AuthUserModify authUserModify){
        return iOauthService.modifyPassword(httpServletRequest,authUserModify);
    }
}
