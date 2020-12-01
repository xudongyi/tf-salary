package business.service.impl;

import business.bean.SalarySubDeptConfig;
import business.bean.SalarySubDeptConfigDt;
import business.mapper.SalarySubDeptConfigDtMapper;
import business.mapper.SalarySubDeptConfigMapper;
import business.service.ISalarySubDeptConfigDtService;
import business.service.ISalarySubDeptConfigService;
import business.vo.SalarySubDeptConfigVo;
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
public class SalarySubDeptConfigServiceImpl extends ServiceImpl<SalarySubDeptConfigMapper, SalarySubDeptConfig> implements ISalarySubDeptConfigService {
    @Resource
    private SalarySubDeptConfigMapper salarySubDeptConfigMapper;

    @Resource
    private SalarySubDeptConfigDtMapper salarySubDeptConfigDtMapper;

    @Resource
    private ISalarySubDeptConfigDtService salarySubDeptConfigDtService;
    @Override
    public IPage<SalarySubDeptConfig> query(IPage<SalarySubDeptConfig> page, Wrapper<SalarySubDeptConfig> queryWrapper) {
        return  salarySubDeptConfigMapper.getSalarySubDeptConfigList(page,queryWrapper);
    }

    @Override
    @Transactional
    public boolean saveSalarySubDeptConfig(SalarySubDeptConfigVo salarySubDeptConfigVo) {
        SalarySubDeptConfig config = new SalarySubDeptConfig();
        config.setSubName(salarySubDeptConfigVo.getSubName());
        config.setSort(salarySubDeptConfigVo.getSort());
        if(salarySubDeptConfigVo.getId()!=null){
            //更新主表
            config.setId(salarySubDeptConfigVo.getId());
            int i = salarySubDeptConfigMapper.updateById(config);
            for(SalarySubDeptConfigDt dt : salarySubDeptConfigVo.getDetails()){
                dt.setMainid(config.getId());
            }
            //删除明细表
            int j = salarySubDeptConfigDtMapper.delete(new LambdaQueryWrapper<SalarySubDeptConfigDt>()
                    .eq(SalarySubDeptConfigDt::getMainid, salarySubDeptConfigVo.getId()));
            boolean result = salarySubDeptConfigDtService.saveBatch(salarySubDeptConfigVo.getDetails());
            return (i>0 && j>0 && result);
            //编辑
        }else{
            //新增
            int i = salarySubDeptConfigMapper.insert(config);
            for(SalarySubDeptConfigDt dt : salarySubDeptConfigVo.getDetails()){
                dt.setMainid(config.getId());
            }
            boolean result = salarySubDeptConfigDtService.saveBatch(salarySubDeptConfigVo.getDetails());
            return (i>0 && result);
        }
    }

    @Override
    @Transactional
    public boolean removeSalarySubDeptConfig(String id) {
        if(id!=null){
            //1.先salarySubDeptConfigDtMapper删除明细表
            int i = salarySubDeptConfigDtMapper.delete(new LambdaQueryWrapper<SalarySubDeptConfigDt>()
                    .eq(SalarySubDeptConfigDt::getMainid, id));
            //2.再删除主表
            int j = salarySubDeptConfigMapper.deleteById(id);
            return i>0 && j>0;
        }
        return false;
    }

    @Override
    public SalarySubDeptConfigVo getSalarySubDeptConfig(String id) {
        SalarySubDeptConfig salarySubDeptConfig = salarySubDeptConfigMapper.selectById(id);
        SalarySubDeptConfigVo vo = new SalarySubDeptConfigVo(salarySubDeptConfig);
        List<SalarySubDeptConfigDt> details = salarySubDeptConfigDtMapper.selectList(new LambdaQueryWrapper<SalarySubDeptConfigDt>()
                .eq(SalarySubDeptConfigDt::getMainid, id));
        vo.setDetails(details);
        List<Object> detailArray = new ArrayList<>();
        for(SalarySubDeptConfigDt dt : details){
            detailArray.add(dt.getSubDepart());
        }
        vo.setDetail(detailArray.toArray());
        return vo;
    }
}