package business.service;

import business.common.api.vo.Result;
import business.vo.AuthUserModify;
import business.vo.AuthUserSSO;
import business.vo.AuthUserVO;

import javax.servlet.http.HttpServletRequest;

public interface IOauthService {

    /**
     * 保存管理员
     */
    Result<?> login(AuthUserVO authUserVO);

    Result<?> sso(AuthUserSSO authUserSSO);

    Result<?> checkSso(AuthUserSSO authUserSSO);

    Result<?> sendMobile(HttpServletRequest httpRequest, AuthUserModify authUserModify);

    Result<?> modifyPassword(HttpServletRequest httpRequest,AuthUserModify authUserModify);


}