package business.mapper;

import business.bean.PersonnelWelfare;
import business.vo.PersonnelWelfareVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

public interface PersonnelWelfareMapper extends BaseMapper<PersonnelWelfare> {

    /**
     * 薪资查询关联人员姓名
     */
    IPage<PersonnelWelfareVO> getPersonnelWelfare(IPage<PersonnelWelfareVO> page, @Param(Constants.WRAPPER) Wrapper<PersonnelWelfareVO> queryWrapper);

}
