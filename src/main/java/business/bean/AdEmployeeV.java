package business.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("ad_employee_v")
public class AdEmployeeV extends Model<AdEmployeeV> {

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

    @TableField(value = "\"CREATE OR REPLACE_DATE\"")
    private String createOrReplaceDate;

    private String updateDate;

    private String payJobName;

    private String ministriescode;

    private String ministriesname;

    private String typeId;

    private String typeNote;

}
