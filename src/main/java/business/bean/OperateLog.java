package business.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("operate_log")
public class OperateLog extends Model<OperateLog> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String ip;

    private String userId;

    private Date operateTime;

    private int operateType=-1;

    private String operateName;

    private String content;


}
