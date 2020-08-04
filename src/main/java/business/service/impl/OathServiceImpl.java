package business.service.impl;

import business.bean.AuthToken;
import business.bean.AuthUser;
import business.common.api.vo.Result;
import business.constant.Constants;
import business.emum.ErrorEnum;
import business.emum.RoleEnum;
import business.jwt.JwtUtil;
import business.mapper.AuthTokenMapper;
import business.mapper.AuthUserMapper;
import business.service.IOauthService;
import business.util.ExceptionUtil;
import business.vo.AuthUserVO;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.Date;

import static cn.hutool.crypto.SecureUtil.*;

@Service
@Slf4j
public class OathServiceImpl implements IOauthService {
    @Autowired
    private AuthUserMapper authUserDao;

    @Autowired
    private AuthTokenMapper authTokenDao;
    @Override
    public Result<?> login(AuthUserVO authUserVO) {
        log.debug("login {}", authUserVO);
        if (authUserVO == null || StringUtils.isBlank(authUserVO.getLoginid()) || StringUtils.isBlank(authUserVO.getPassword())) {
            ExceptionUtil.rollback(ErrorEnum.PARAM_ERROR);
        }
        AuthUser authUser = authUserDao.selectOne(new LambdaQueryWrapper<AuthUser>()
                .eq(AuthUser::getRoleid, RoleEnum.ADMIN.getRoleId())
                .eq(AuthUser::getLoginid, authUserVO.getLoginid()));
        ExceptionUtil.isRollback(authUser == null, ErrorEnum.ACCOUNT_NOT_EXIST);

        String psw = SecureUtil.md5(authUserVO.getPassword());
        ExceptionUtil.isRollback(!authUser.getPassword().equals(psw), ErrorEnum.PASSWORD_ERROR);

        authUserVO.setRoleId(authUser.getRoleid());
        String token = JwtUtil.getToken(new AuthUserVO().setPassword(authUser.getPassword()).setLastname(authUser.getLastname()).setId(authUser.getId()));
        authUserVO.setToken(token);
        AuthToken authToken = authTokenDao.selectOne(new LambdaQueryWrapper<AuthToken>().eq(AuthToken::getUserId,authUser.getId()));
        if(authToken==null){
            authToken = new AuthToken();
            authToken.setUserId(authUser.getId())
                    .setToken(token);
        }
        authToken.setExpireTime(new Date(Constants.EXPIRE_TIME + System.currentTimeMillis()));
        authTokenDao.insert(new AuthToken()
                .setUserId(authUser.getId())
                .setToken(token)
                .setExpireTime(new Date(Constants.EXPIRE_TIME + System.currentTimeMillis())));
        return Result.ok(authUserVO);
    }
}
