package business.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 权限用户
 *
 * @author xudy
 * @since 2019-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ad_employee_v_sync")
public class AdEmployeeVSync extends Model<AdEmployeeVSync> {

    private String syncDate;

    private String hrids;

    private String hrNo;

    private String hrName;

    private String departid;

    private String departCode;

    private String departName;

    private String departlcode;

    private String departlname;

    private String mobilephone;

    private String headshipTypeCnname;

    private String userStatus;

    private String userStatus2;

    private String gender;

    private String createOrReplaceDate;

    private String updateDate;

    private String payJobName;

    private String ministriescode;

    private String ministriesname;

    private String typeId;

    private String typeNote;

}