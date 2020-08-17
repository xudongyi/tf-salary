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
import business.mapper.HrmResourceMapper;
import business.service.IOauthService;
import business.util.ExceptionUtil;
import business.vo.AuthUserSSO;
import business.vo.AuthUserVO;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Map;


@Service
@Slf4j
public class OathServiceImpl implements IOauthService {
    @Autowired
    private AuthUserMapper authUserDao;

    @Autowired
    private AuthTokenMapper authTokenDao;

    @Autowired
    private HrmResourceMapper hrmResourceMapper;
    @Override
    public Result<?> login(AuthUserVO authUserVO) {
        log.debug("login {}", authUserVO);
        if (authUserVO == null || StringUtils.isBlank(authUserVO.getLoginid()) || StringUtils.isBlank(authUserVO.getPassword())) {
            ExceptionUtil.rollback(ErrorEnum.PARAM_ERROR);
        }
        String psw = SecureUtil.md5(authUserVO.getPassword());
        AuthUser authUser = authUserDao.selectOne(new LambdaQueryWrapper<AuthUser>()
                .eq(AuthUser::getPassword, psw)
                .eq(AuthUser::getLoginid, authUserVO.getLoginid()));
        if(authUser==null){
            return Result.error(500,"用户名或密码错误！");
        }
        authUserVO.setRoleId(authUser.getRoleid());
        String token = JwtUtil.getToken(new AuthUserVO().setPassword(authUser.getPassword()).setLastname(authUser.getLastname()).setId(authUser.getId()));
        authUserVO.setToken(token);
        AuthToken authToken = authTokenDao.selectOne(new LambdaQueryWrapper<AuthToken>().eq(AuthToken::getUserId,authUser.getId()));
        if(authToken==null){
            authToken = new AuthToken();
            authToken.setUserId(authUser.getId())
                    .setToken(token);
        }else{
            authTokenDao.deleteById(authToken.getId());
        }
        Date expireDate = new Date(Constants.EXPIRE_TIME + System.currentTimeMillis());
        authToken.setExpireTime(expireDate);
        authTokenDao.insert(new AuthToken()
                .setUserId(authUser.getId())
                .setToken(token)
                .setExpireTime(expireDate));
        authUserVO.setExpireTime(expireDate.getTime());
        return Result.ok(authUserVO);
    }

    @Override
    public Result<?> sso(AuthUserSSO authUserSSO) {
        log.debug("sso {}", authUserSSO);
        if (authUserSSO == null || StringUtils.isBlank(authUserSSO.getAppid())|| StringUtils.isBlank(authUserSSO.getLoginid())|| StringUtils.isBlank(authUserSSO.getWorkcode())) {
            ExceptionUtil.rollback(ErrorEnum.PARAM_ERROR);
        }

        if(!authUserSSO.getAppid().equals(Constants.OA_APP_ID)){
            ExceptionUtil.rollback(ErrorEnum.PARAM_INCORRECT);
        }
        AuthUser authUser = authUserDao.selectOne(new LambdaQueryWrapper<AuthUser>()
                .eq(AuthUser::getRoleid, RoleEnum.ADMIN.getRoleId())
                .eq(AuthUser::getLoginid, authUserSSO.getLoginid()));
        if(authUser==null){
            Map<String,Object> hrmresource = hrmResourceMapper.getHrmResource(authUserSSO.getLoginid());
            if(hrmresource==null){
                return Result.error(500,"OA系统中未找到该登录名，请核查信息！");
            }
            //插入数据
            authUser = new AuthUser();
            authUser.setLoginid(authUserSSO.getLoginid());
            authUser.setRoleid(1L);
            authUser.setWorkcode(authUser.getWorkcode());
            authUser.setFirst_login(0);
            authUser.setLastname(hrmresource.get("lastname").toString());
            authUserDao.insert(authUser);
        }
        String token = JwtUtil.getSSOToken(authUserSSO);

        AuthToken authToken = authTokenDao.selectOne(new LambdaQueryWrapper<AuthToken>().eq(AuthToken::getUserId,authUser.getId()));
        if(authToken==null){
            authToken = new AuthToken();
            authToken.setUserId(authUser.getId())
                    .setToken(token);
        }else{
            authTokenDao.deleteById(authToken.getId());
        }
        Date expireDate = new Date(Constants.EXPIRE_TIME + System.currentTimeMillis());
        authToken.setExpireTime(expireDate);
        authTokenDao.insert(new AuthToken()
                .setUserId(authUser.getId())
                .setToken(token)
                .setExpireTime(expireDate));
        authUserSSO.setToken(token);
        authUserSSO.setExpireTime(expireDate.getTime());
        return  Result.ok(authUserSSO);
    }

    @Override
    public Result<?> checkSso(AuthUserSSO authUserSSO) {
        log.debug("checkSso {}", authUserSSO);
        if (authUserSSO == null || StringUtils.isBlank(authUserSSO.getLoginid())|| StringUtils.isBlank(authUserSSO.getToken())) {
            return Result.error(500,"参数错误！");
        }
        Map<String,Object> hrmresource = hrmResourceMapper.getHrmResource(authUserSSO.getLoginid());
        if(hrmresource==null){
            return Result.error(500,"OA中不存在该用户！");
        }
        AuthUser authUser = authUserDao.selectOne(new LambdaQueryWrapper<AuthUser>()
                .eq(AuthUser::getLoginid, authUserSSO.getLoginid()));
        if(authUser==null){
            return Result.error(500,"用户未在系统中注册，请检查用户！");
        }
        AuthToken authToken = authTokenDao.selectOne(new LambdaQueryWrapper<AuthToken>().eq(AuthToken::getUserId,authUser.getId()).eq(AuthToken::getToken,authUserSSO.getToken()));
        if(authToken==null){
            return Result.error(500,"验证失败，请重新获取token！");
        }
        authUserSSO.setRoleId(authUser.getRoleid());
        authUserSSO.setWorkcode(authUser.getWorkcode());
        authUserSSO.setLastname(hrmresource.get("LASTNAME").toString());
        authUserSSO.setExpireTime(authToken.getExpireTime().getTime());
        return  Result.ok(authUserSSO);
    }


}
