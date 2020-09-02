package business.service;

import business.bean.HrmResource;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 数采仪
 * @Author: jeecg-boot
 * @Date:   2020-07-07
 * @Version: V1.0
 */
public interface IHrmResourceService extends IService<HrmResource> {
    Map<String,Object> getHrmResource(String loginId);

    List<Map<String,Object>> getHrmResourceByWorkcode(String workcodes);
}
