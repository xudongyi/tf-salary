package business.mapper;

import business.bean.OperateLog;
import business.vo.OperateLogVO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface OperateLogMapper extends BaseMapper<OperateLog> {

    /**
     * 多表查询分页
     * @param page
     * @return
     */
    IPage<OperateLog> queryLog(IPage<OperateLogVO> page ,@Param(Constants.WRAPPER) Wrapper<OperateLogVO> queryWrapper);

    List<Map<String,Object>> getVisitTimesByMonth(String month,String site);

    List<Map<String,Object>> getNoteNumberByMonth(String month,String site);

    List<Map<String,Object>> getNoteTimesRankByDepartment(String month,String site);

    List<Map<String,Object>> getNoteTimesBetweenMonth(String staDate,String endDate,String site);

    List<OperateLog> loginInfo(OperateLogVO operateLogVO);


}
