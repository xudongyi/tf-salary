package business.mapper;

import business.bean.AdEmployeeVSync;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @author xudy
 * @since 2019-09-18
 */
public interface AdEmployeeVSyncMapper extends BaseMapper<AdEmployeeVSync> {

    List<AdEmployeeVSync> getAllAdEmployeeV(String site);

    int deleteAllAdEmployeeVSyncByDate(String site, String syncDate);

    List<AdEmployeeVSync> getAllAdEmployeeVWidthOutTree();

    Map<String, Object> checkAdEmployeeV(String departMentId, String syncDate);
}