package business.service;

import business.bean.AuthUser;
import business.bean.HrmResource;
import business.common.api.vo.Result;
import business.vo.AuthUserVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: authUser
 * @Author: jeecg-boot
 * @Date:   2020-07-07
 * @Version: V1.0
 */
public interface IAuthUserService extends IService<AuthUser> {
    /**
     * 获取用户信息
     *
     * @param authUserVO
     * @return
     */
    Result<?> getUserInfo(AuthUserVO authUserVO);

    AuthUser getUserByToken(String token);
}
