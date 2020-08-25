package business.vo;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors(chain = true)
public class AuthUserModify {

    private String loginid;

    private String mobile;

    private String password;

    private String checkPass;

    private String captcha;
}
