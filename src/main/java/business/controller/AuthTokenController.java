package business.controller;

import business.common.api.vo.Result;
import business.service.IOauthService;
import business.vo.AuthUserSSO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
