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
@TableName("SALARY_REPORT_CONFIG")
public class SalaryReportConfig extends Model<SalaryReportConfig> {

    @TableId(type = IdType.AUTO)
    private BigDecimal id;

    private String site;

    private String stage;

    private String departName;

    private Integer sort;

    private String tabId;

    private Integer isTotal;
}
