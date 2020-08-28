package business.mapper;

import business.bean.OperateLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.Map;

public interface OperateLogMapper extends BaseMapper<OperateLog> {

    /**
     * 多表查询分页
     * @param page
     * @return
     */
    IPage<OperateLog> queryLog(IPage<OperateLog> page);

}
