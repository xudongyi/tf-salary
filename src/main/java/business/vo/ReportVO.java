package business.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ReportVO{

    //报表页面信息
    private String currentMonthSalary;

    private String lastMonthSalary;

    private String currentMonthImportNumber;

    private String lastMonthImportNumber;

    private String currentMonthVisits;

    private String lastMonthVisits;

    private String currentMonthNotesNumber;

    private String lastMonthNotesNumber;

    private List<String> salaryList;

    private List<String> noteList;

    private List<Map<String,Object>> salaryDeptList;

    private List<Map<String,Object>> noteDeptList;
}
