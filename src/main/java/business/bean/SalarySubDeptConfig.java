package business.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("SALARY_SUBDEPT_CONFIG")
public class SalarySubDeptConfig extends Model<SalarySubDeptConfig> {

    @TableId(type = IdType.AUTO)
    private BigDecimal id;

    private String subName;

    private Integer sort;

}
