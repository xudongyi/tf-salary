package business.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Description:
 * @Author:xudy
 * @Date:2018/09/20 16:58
 */
@Data
@Accessors(chain = true)
public class UserSessionVO {

    private Long id;

    private String loginId;

    private String workcode;

    private String lastname;

    private String token;

    private Long roleId;

}