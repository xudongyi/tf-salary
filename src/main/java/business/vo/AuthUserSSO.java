package business.vo;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class AuthUserSSO {


    /**
     * 主键
     */
    private Long id;

    private String appid;

    private String appname;

    /**
     * 登录名
     */
    private String loginid;

    /**
     * 姓名
     */
    private String lastname;
    /**
     * 工号
     */
    private String workcode;

    private String token;

    private Long roleId=1L;

    /**
     * 过期时间
     */
    private long expireTime;

    private int first_login;

    private String site;

}
