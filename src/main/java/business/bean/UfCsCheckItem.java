package business.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * 权限token
 * @author xudy
 * @since 2020-09-18
 */
@Data
@TableName("uf_cs_checkitem")
public class UfCsCheckItem extends Model<UfCsCheckItem> {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * token
     */
    @Excel(name = "物资类别")
    private String name;

    /**
     * 过期时间
     */
    @Excel(name = "规格型号")
    private String sort;

    /**
     * token所属人
     */
    @Excel(name = "工厂")
    private String isseal;
}