package business.service.impl;

import business.bean.AuthToken;
import business.bean.AuthUser;
import business.common.api.vo.Result;
import business.mapper.AuthTokenMapper;
import business.mapper.AuthUserMapper;
import business.service.IAuthUserService;
import business.vo.AuthUserVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AuthUserServiceImpl extends ServiceImpl<AuthUserMapper, AuthUser> implements IAuthUserService {
    @Resource
    private AuthUserMapper authUserMapper;

    @Resource
    private AuthTokenMapper authTokenMapper;

    @Override
    public Result<?> getUserInfo(AuthUserVO authUserVO) {

        return null;
    }

    @Override
    public AuthUser getUserByToken(String token) {
        AuthToken authToken = authTokenMapper.selectOne(new LambdaQueryWrapper<AuthToken>()
                .eq(AuthToken::getToken, token));
        if(authToken!=null){
            AuthUser authUser = authUserMapper.selectById(authToken.getUserId());
            return authUser;
        }
        return null;
    }
}