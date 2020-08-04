package business.service.impl;

import business.bean.AuthUser;
import business.common.api.vo.Result;
import business.mapper.AuthTokenMapper;
import business.mapper.AuthUserMapper;
import business.service.IAuthUserService;
import business.vo.AuthUserVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthUserServiceImpl extends ServiceImpl<AuthUserMapper, AuthUser> implements IAuthUserService {
    @Autowired
    private AuthUserMapper authUserDao;

    @Autowired
    private AuthTokenMapper authTokenDao;

    @Override
    public Result<?> getUserInfo(AuthUserVO authUserVO) {

        return null;
    }
}