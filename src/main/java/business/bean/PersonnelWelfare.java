package business.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * 薪资表
 * @author xudy
 * @since 2020-09-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("PERSONNEL_WELFARE")
public class PersonnelWelfare extends Model<PersonnelWelfare> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Excel(name = "工号",orderNum="2")
    private String workcode;

    @Excel(name = "发放时间",orderNum="3")
    private String welfareDate;

    private Integer welfareType;

    @Excel(name = "名称",orderNum="4")
    private String welfareTypeName;

    @Excel(name = "金额",orderNum="5")
    private Float welfareAmount;

    private Float welfareAmountSalaries;
    private Float welfareAmountBonus;
    private Float welfareAmountWeal;
    private String departid;



}