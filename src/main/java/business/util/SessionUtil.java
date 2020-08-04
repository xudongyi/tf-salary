package business.util;

import business.bean.AuthToken;
import business.bean.AuthUser;
import business.constant.Constants;
import business.emum.ErrorEnum;
import business.mapper.AuthTokenMapper;
import business.mapper.AuthUserMapper;
import business.vo.UserSessionVO;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @Author:xudy
 * @Date:2018/09/27 12:52
 */
public class SessionUtil {

    /**
     * 获取用户Session信息
     *
     * @return
     */
    public static UserSessionVO getUserSessionInfo() {

        // 获取请求对象
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // 获取请求头Token值
        String token = Optional.ofNullable(request.getHeader(Constants.AUTHENTICATION)).orElse(null);

        if (StringUtils.isBlank(token)) {
            return null;
        }

        // 获取 token 中的 user id
        AuthUser authUser = null;
        try {
            authUser = JSON.parseObject(JWT.decode(token).getAudience().get(0), AuthUser.class);
        } catch (JWTDecodeException j) {
            ExceptionUtil.rollback(ErrorEnum.INVALID_TOKEN);
        }

        AuthUserMapper userDao = BeanTool.getBean(AuthUserMapper.class);
        AuthUser user = userDao.selectById(authUser.getId());
        if (user == null) {
            ExceptionUtil.rollback(ErrorEnum.LOGIN_ERROR);
        }

        // 验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getWorkcode())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            ExceptionUtil.rollback(ErrorEnum.INVALID_TOKEN);
        }

        AuthTokenMapper authTokenDao = BeanTool.getBean(AuthTokenMapper.class);
        Integer count = authTokenDao.selectCount(new LambdaQueryWrapper<AuthToken>().eq(AuthToken::getToken, token).eq(AuthToken::getUserId, user.getId()).ge(AuthToken::getExpireTime,
                LocalDateTime.now()));
        if (count.equals(Constants.ZERO)) {
            ExceptionUtil.rollback(ErrorEnum.INVALID_TOKEN);
        }

        UserSessionVO userSessionVO = new UserSessionVO();
        userSessionVO.setLastname(user.getLastname()).setRoleId(user.getRoleId()).setId(user.getId());
        return userSessionVO;
    }

}