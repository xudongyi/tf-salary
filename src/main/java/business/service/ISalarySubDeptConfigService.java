package business.service;

import business.bean.SalarySubDeptConfig;
import business.common.api.vo.Result;
import business.vo.SalarySubDeptConfigVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ISalarySubDeptConfigService extends IService<SalarySubDeptConfig> {

    IPage<SalarySubDeptConfig> query(IPage<SalarySubDeptConfig> page, Wrapper<SalarySubDeptConfig> queryWrapper);

    boolean saveSalarySubDeptConfig(SalarySubDeptConfigVo salarySubDeptConfigVo);

    boolean removeSalarySubDeptConfig(String id);

    SalarySubDeptConfigVo getSalarySubDeptConfig(String id);

    Result<?> subDepartMentAll();

    Result<?> departMentAllBySub(int subid);

}