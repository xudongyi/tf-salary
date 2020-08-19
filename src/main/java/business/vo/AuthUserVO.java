package business.vo;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class AuthUserVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 登录名
     */
    private String loginid;
    /**
     * 工号
     */
    private String workcode;

    /**
     * 用户名
     */
    private String lastname;

    /**
     * 密码
     */
    private String password;


    private String token;

    private Long roleId;

    /**
     * 过期时间
     */
    private long expireTime;

    private int first_login;

}
