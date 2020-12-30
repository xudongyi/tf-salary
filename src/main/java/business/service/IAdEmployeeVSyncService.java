package business.service;

import business.bean.AdEmployeeVSync;
import business.common.api.vo.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: authUser
 * @Author: jeecg-boot
 * @Date:   2020-07-07
 * @Version: V1.0
 */
public interface IAdEmployeeVSyncService extends IService<AdEmployeeVSync> {

    Result<?> syncData(String site, String syncDate);
}
