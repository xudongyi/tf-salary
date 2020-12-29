package business.mapper;

import business.bean.SalaryReportConfig;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SalaryReportConfigMapper extends BaseMapper<SalaryReportConfig> {
    List<SalaryReportConfig> getSalaryReportConfigIsTotal(String site,String tabId);

    List<SalaryReportConfig> getSalaryReportConfig(String site,String tabId);

    List<SalaryReportConfig> getSalaryReportConfigNoTotal(String site,String tabId);

    List<Map<String,Object>> getSiteInfoByConfigure(String site,String tabId);

    List<Map<String,Object>> getStageGroup(String site,String tabId);

    IPage<SalaryReportConfig> getSalaryReportConfigList(IPage<SalaryReportConfig> page , @Param(Constants.WRAPPER) Wrapper<SalaryReportConfig> queryWrapper);

}
