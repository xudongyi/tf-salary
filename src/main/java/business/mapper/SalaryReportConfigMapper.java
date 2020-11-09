package business.mapper;

import business.bean.HrmResource;
import business.bean.SalaryReportConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

public interface SalaryReportConfigMapper extends BaseMapper<SalaryReportConfig> {
    List<SalaryReportConfig> getSalaryReportConfig(String site,String tabId);

}
