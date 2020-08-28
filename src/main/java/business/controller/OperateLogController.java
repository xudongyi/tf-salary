package business.controller;

import business.bean.OperateLog;
import business.common.api.vo.Result;
import business.service.IOperateLogService;
import business.vo.OperateLogVO;
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
    @PostMapping(value = "/list")
    public Result<?> queryPageList(
            @RequestBody OperateLogVO operateLogVO,
            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                   HttpServletRequest req) {
        IPage<OperateLogVO> page = new Page<OperateLogVO>(pageNo, pageSize);
        QueryWrapper<OperateLogVO> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(operateLogVO.getUserId())) {
            queryWrapper.eq("t2.loginid", operateLogVO.getUserId());
        }
        if (StringUtils.isNotBlank(operateLogVO.getDept())) {
            queryWrapper.eq("t2.departmentid", operateLogVO.getDept());
        }
        if (StringUtils.isNotBlank(operateLogVO.getContent())) {
            queryWrapper.like("t1.content", operateLogVO.getContent());
        }

        return Result.ok(iOperateLogService.queryLog(page,queryWrapper));
    }
}
