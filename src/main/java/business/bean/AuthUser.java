package business.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.io.Serializable;

/**
 * 权限用户
 * @author xudy
 * @since 2019-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("auth_user")
public class AuthUser extends Model<AuthUser> {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long hrmid;

    private String mobile;

    private String loginid;

    private String password;

    private String workcode;

    private String lastname;

    /**
     * 角色主键 1 普通用户 2 admin
     */
    private Long roleid;

    /**
     * 邮箱
     */
    private String email;

    /**0-第一次 1-不是第一次**/
    private int firstLogin;


    @Override
    protected Serializable pkVal() {
        return id;
    }
}