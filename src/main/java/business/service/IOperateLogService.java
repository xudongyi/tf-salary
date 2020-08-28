package business.service;

import business.bean.AuthUser;
import business.bean.OperateLog;
import business.emum.OperLogType;
import business.vo.OperateLogVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IOperateLogService  extends IService<OperateLog> {

    void insertLog(OperLogType operateTye, String content);

    /**
     * 多表查询分页
     * @param page
     * @return
     */
    IPage<OperateLog> queryLog(IPage<OperateLogVO> page, Wrapper<OperateLogVO> queryWrapper);
}
