package business.service.impl;

import business.bean.PersonnelWelfare;
import business.mapper.PersonnelWelfareMapper;
import business.service.IPersonnelWelfareService;
import business.vo.PersonnelWelfareVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
@Slf4j
public class PersonnelWelfareServiceImpl extends ServiceImpl<PersonnelWelfareMapper, PersonnelWelfare> implements IPersonnelWelfareService {

    @Resource
    private PersonnelWelfareMapper personnelWelfareMapper;

    @Override
    public IPage<PersonnelWelfareVO> getPersonnelWelfareList(PersonnelWelfareVO personnelWelfareVO, Integer pageNo, Integer pageSize) {
        IPage<PersonnelWelfareVO> page = new Page<PersonnelWelfareVO>(pageNo, pageSize);
        QueryWrapper<PersonnelWelfareVO> welfareQueryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(personnelWelfareVO.getWorkcode())) {
            welfareQueryWrapper.eq("t1.workcode", personnelWelfareVO.getWorkcode());
        }
        if(StringUtils.isNotBlank(personnelWelfareVO.getDept())){
            welfareQueryWrapper.eq("t2.depart_code", personnelWelfareVO.getDept().split("_")[0]);
        }
        if(StringUtils.isNotBlank(personnelWelfareVO.getWelfarestamonth())){
            welfareQueryWrapper.ge("t1.welfare_date", personnelWelfareVO.getWelfarestamonth());
        }
        if(StringUtils.isNotBlank(personnelWelfareVO.getWelfareendmonth())){
            welfareQueryWrapper.le("t1.welfare_date", personnelWelfareVO.getWelfareendmonth());
        }
        return personnelWelfareMapper.getPersonnelWelfare(page,welfareQueryWrapper);
    }

    @Override
    public List<PersonnelWelfare> getPersonelWelflare(PersonnelWelfareVO personnelWelfareVO) {
        return null;
    }
}
