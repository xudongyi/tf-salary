package business.service.impl;

import business.bean.AuthUser;
import business.bean.SalaryReportConfig;
import business.bean.SalaryReportConfigDt;
import business.mapper.SalaryReportConfigDtMapper;
import business.mapper.SalaryReportConfigMapper;
import business.service.ISalaryReportConfigDtService;
import business.service.ISalaryReportConfigService;
import business.vo.SalaryReportConfigVo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SalaryReportConfigServiceImpl extends ServiceImpl<SalaryReportConfigMapper, SalaryReportConfig> implements ISalaryReportConfigService {
    @Resource
    private SalaryReportConfigMapper salaryReportConfigMapper;

    @Resource
    private SalaryReportConfigDtMapper salaryReportConfigDtMapper;

    @Resource
    private ISalaryReportConfigDtService salaryReportConfigDtService;
    @Override
    public IPage<SalaryReportConfig> query(IPage<SalaryReportConfig> page, Wrapper<SalaryReportConfig> queryWrapper) {
        return  salaryReportConfigMapper.getSalaryReportConfigList(page,queryWrapper);
    }

    @Override
    @Transactional
    public boolean saveSalaryReportConfig(SalaryReportConfigVo salaryReportConfigVo) {
        SalaryReportConfig config = new SalaryReportConfig();
        config.setSite(salaryReportConfigVo.getSite());
        config.setDepartName(salaryReportConfigVo.getDepartName());
        config.setSort(salaryReportConfigVo.getSort());
        config.setStage(salaryReportConfigVo.getStage());
        config.setTabId(salaryReportConfigVo.getTabId());
        if(salaryReportConfigVo.getId()!=null){
            //更新主表
            config.setId(salaryReportConfigVo.getId());
            int i = salaryReportConfigMapper.updateById(config);
            for(SalaryReportConfigDt dt : salaryReportConfigVo.getDetails()){
                dt.setMainid(config.getId());
            }
            //删除明细表
            int j = salaryReportConfigDtMapper.delete(new LambdaQueryWrapper<SalaryReportConfigDt>()
                    .eq(SalaryReportConfigDt::getMainid, salaryReportConfigVo.getId()));
            boolean result = salaryReportConfigDtService.saveBatch(salaryReportConfigVo.getDetails());
            return (i>0 && j>0 && result);
            //编辑
        }else{
            //新增
            int i = salaryReportConfigMapper.insert(config);
            for(SalaryReportConfigDt dt : salaryReportConfigVo.getDetails()){
                dt.setMainid(config.getId());
            }
            boolean result = salaryReportConfigDtService.saveBatch(salaryReportConfigVo.getDetails());
            return (i>0 && result);
        }
    }

    @Override
    @Transactional
    public boolean removeSalaryReportConfig(String id) {
        if(id!=null){
            //1.先salaryReportConfigDtMapper删除明细表
            int i = salaryReportConfigDtMapper.delete(new LambdaQueryWrapper<SalaryReportConfigDt>()
                    .eq(SalaryReportConfigDt::getMainid, id));
            //2.再删除主表
            int j = salaryReportConfigMapper.deleteById(id);
            return i>0 && j>0;
        }
        return false;
    }

    @Override
    public SalaryReportConfigVo getSalaryReportConfig(String id) {
        SalaryReportConfig salaryReportConfig = salaryReportConfigMapper.selectById(id);
        SalaryReportConfigVo vo = new SalaryReportConfigVo(salaryReportConfig);
        List<SalaryReportConfigDt> details = salaryReportConfigDtMapper.selectList(new LambdaQueryWrapper<SalaryReportConfigDt>()
                .eq(SalaryReportConfigDt::getMainid, id));
        vo.setDetails(details);
        List<Object> detailArray = new ArrayList<>();
        for(SalaryReportConfigDt dt : details){
            detailArray.add(dt.getSubDepart());
        }
        vo.setDetail(detailArray.toArray());
        return vo;
    }
}