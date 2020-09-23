package business.service;

import business.bean.PersonnelWelfare;
import business.vo.PersonnelWelfareVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IPersonnelWelfareService extends IService<PersonnelWelfare> {

    IPage<PersonnelWelfareVO> getPersonnelWelfareList(PersonnelWelfareVO personnelWelfareVO, Integer pageNo, Integer pageSize);
}