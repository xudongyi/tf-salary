package business.vo.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ExcelDepartMonthVo implements Serializable {
 
    private static final long serialVersionUID = 1L;
 
    /**
     * 月份
     */
    @Excel(name = "月份", width = 20,needMerge = true)
    private String month;


    //子条目集合（这里是实现一对多的关键。name=""是为了不出现表头，如果不为空表头会多一层合并的单元格）
    @ExcelCollection(name = "")
    private List<ExcelDepartMonthDept> excelDepartMonthDepts;
 
 
 
 
 
 
}