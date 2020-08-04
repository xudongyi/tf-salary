package business.service;

import business.common.api.vo.Result;
import business.vo.AuthUserVO;

public interface IOauthService {

    /**
     * 保存管理员
     */
    Result<?> login(AuthUserVO authUserVO);

}