package business.mapper;

import business.bean.SalarySubDeptConfig;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SalarySubDeptConfigMapper extends BaseMapper<SalarySubDeptConfig> {
    List<SalarySubDeptConfig> getSalarySubDeptConfig(String site, String tabId);

    List<Map<String,Object>> getStageGroup(String site, String tabId);

    IPage<SalarySubDeptConfig> getSalarySubDeptConfigList(IPage<SalarySubDeptConfig> page, @Param(Constants.WRAPPER) Wrapper<SalarySubDeptConfig> queryWrapper);

    List<SalarySubDeptConfig> getSubDept(String departMentId);

}
