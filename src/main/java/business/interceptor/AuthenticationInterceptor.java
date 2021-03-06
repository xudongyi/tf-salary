package business.interceptor;

import business.bean.AuthToken;
import business.constant.Constants;
import business.emum.ErrorEnum;
import business.emum.RoleEnum;
import business.jwt.LoginIgnore;
import business.jwt.LoginRequired;
import business.mapper.AuthTokenMapper;
import business.util.ExceptionUtil;
import business.util.SessionUtil;
import business.vo.UserSessionVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @Author:xudy
 * @Date:2018/09/27 12:52
 */
@Configuration
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Resource
    private AuthTokenMapper authTokenMapper;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) {
        // 从 http 请求头中取出 token
        String token = httpServletRequest.getHeader(Constants.AUTHENTICATION);
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod) || method.isAnnotationPresent(LoginIgnore.class)) {
            return true;
        }

        //检查是否有LoginRequired注释，没有有则跳过认证

        // 执行认证
        if (token == null || token.indexOf("Bearer ")==-1) {
            httpServletResponse.setStatus(ErrorEnum.INVALID_TOKEN.getCode());
            ExceptionUtil.rollback(ErrorEnum.INVALID_TOKEN);
        }else{
            //验证token是否过期，过期了，则跳转到登录页面
            token = token.split(" ")[1];
            AuthToken authToken = authTokenMapper.selectOne(new LambdaQueryWrapper<AuthToken>().eq(AuthToken::getToken,token));
            if(authToken==null){
                httpServletResponse.setStatus(ErrorEnum.INVALID_TOKEN.getCode());
                ExceptionUtil.rollback(ErrorEnum.INVALID_TOKEN);

            }
            //如果token存在，并且操作过了
                /*Date expireDate = new Date(Constants.EXPIRE_TIME + System.currentTimeMillis());
                authToken.setExpireTime(expireDate);
                authTokenMapper.updateById(authToken);*/
        }

        if (!method.isAnnotationPresent(LoginRequired.class)) {
            return true;
        }

        LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
        if (loginRequired.required()) {
            RoleEnum role = loginRequired.role();
            if (role == RoleEnum.USER) {
                return true;
            }

            if (role == RoleEnum.ADMIN) {
                UserSessionVO userSessionInfo = SessionUtil.getUserSessionInfo();
                if (role != RoleEnum.getEnumTypeMap().get(userSessionInfo.getRoleId())) {
                    ExceptionUtil.rollback(ErrorEnum.ACCESS_NO_PRIVILEGE);
                }
            }

            return true;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) {
    }
}