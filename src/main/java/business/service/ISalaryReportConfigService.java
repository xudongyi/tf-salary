package business.service;

import business.bean.SalaryReportConfig;
import business.common.api.vo.Result;
import business.vo.SalaryReportConfigVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ISalaryReportConfigService extends IService<SalaryReportConfig> {

    IPage<SalaryReportConfig> query(IPage<SalaryReportConfig> page, Wrapper<SalaryReportConfig> queryWrapper);

    boolean saveSalaryReportConfig(SalaryReportConfigVo salaryReportConfigVo);

    boolean removeSalaryReportConfig(String id);

    SalaryReportConfigVo getSalaryReportConfig(String id);
}