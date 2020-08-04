package business.jwt;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;

public class JwtUtil {
    /**
     * 生成Token
     * @param authUserVO
     * @return
     */
    public static String getToken(AuthUserVO authUserVO) {
        String sign = authUserVO.getWorkcode();
        return JWT.create().withExpiresAt(new Date(System.currentTimeMillis()+ 3600)).withAudience(JSON.toJSONString(authUserVO.setWorkcode(null)))
                .sign(Algorithm.HMAC256(sign));
    }
}
