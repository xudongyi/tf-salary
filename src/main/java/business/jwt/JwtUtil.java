package business.jwt;

import business.vo.AuthUserSSO;
import business.vo.AuthUserVO;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

public class JwtUtil {
    /**
     * 生成登录Token
     * @param authUserVO
     * @return
     */
    public static String getToken(AuthUserVO authUserVO) {
        String sign = authUserVO.getPassword();
        return JWT.create().withExpiresAt(new Date(System.currentTimeMillis()+ 3600)).withAudience(JSON.toJSONString(authUserVO.setPassword(null)))
                .sign(Algorithm.HMAC256(sign));
    }

    /**
     * 生成sso的Token
     * @param authUserSSO
     * @return
     */
    public static String getSSOToken(AuthUserSSO authUserSSO) {
        String sign = authUserSSO.getLoginid();
        return JWT.create().withExpiresAt(new Date(System.currentTimeMillis()+ 3600)).withAudience(JSON.toJSONString(authUserSSO))
                .sign(Algorithm.HMAC256(sign));
    }
}
