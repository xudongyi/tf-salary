package business.service;

import business.bean.SalaryBaseInfo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface SalaryBaseInfoService extends IService<SalaryBaseInfo> {

    /**
     * insert
     */
    int insert(SalaryBaseInfo salaryBaseInfo);
}