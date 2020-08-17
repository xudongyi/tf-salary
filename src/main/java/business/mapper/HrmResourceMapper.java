package business.mapper;

import business.bean.HrmResource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.Map;

public interface HrmResourceMapper extends BaseMapper<HrmResource> {
    Map<String,Object> getHrmResource(String loginId);
}
