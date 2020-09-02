package business.controller;

import business.common.api.vo.Result;
import business.service.IOperateLogService;
import business.util.MyBatisWapperUtil;
import business.vo.OperateLogVO;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("log")
@Slf4j
public class OperateLogController {

    @Resource
    IOperateLogService iOperateLogService;
    /**
     * 分页列表查询
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @GetMapping(value = "/list")
    public Result<?> queryPageList(
            @ModelAttribute OperateLogVO operateLogVO,
            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        IPage<OperateLogVO> page = new Page<>(pageNo, pageSize);
        QueryWrapper<OperateLogVO> queryWrapper = new QueryWrapper<>();
        if (operateLogVO.getOperateType()>=0) {
            queryWrapper.eq("t1.operate_type", operateLogVO.getOperateType());
        }
        if (operateLogVO.getOperateTimeST()!=null) {
            queryWrapper.ge("t1.operate_time", DateUtil.parse(operateLogVO.getOperateTimeST(), DatePattern.NORM_DATE_PATTERN));
        }
        if (operateLogVO.getOperateTimeED()!=null) {
            queryWrapper.le("t1.operate_time",DateUtil.parse(operateLogVO.getOperateTimeED(), DatePattern.NORM_DATE_PATTERN));
        }
        if (StringUtils.isNotBlank(operateLogVO.getUserId())) {
            queryWrapper.eq("t2.id", operateLogVO.getUserId());
        }
        if (StringUtils.isNotBlank(operateLogVO.getDept())) {
            queryWrapper.eq("t2.departmentid", operateLogVO.getDept().split("_")[0]);
        }
        if (StringUtils.isNotBlank(operateLogVO.getContent())) {
            queryWrapper.like("t1.content", operateLogVO.getContent());
        }
        MyBatisWapperUtil.doMultiFieldsOrder(queryWrapper,req.getParameterMap(),"t1");
        return Result.ok(iOperateLogService.queryLog(page,queryWrapper));
    }
}
