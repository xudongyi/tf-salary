package business.mapper;

import business.bean.HrmResource;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

@DS("oa")
public interface HrmResourceMapper extends BaseMapper<HrmResource> {
    Map<String,Object> getHrmResource(String loginId);

    List<Map<String,Object>> getHrmResourceByWorkcode(String workcode);
}
