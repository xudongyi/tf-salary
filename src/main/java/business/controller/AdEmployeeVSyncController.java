package business.controller;

import business.bean.AdEmployeeVSync;
import business.bean.AuthToken;
import business.common.api.vo.Result;
import business.jwt.LoginIgnore;
import business.service.IAdEmployeeVSyncService;
import business.vo.PersonnelWelfareVO;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("sync")
@Slf4j
public class AdEmployeeVSyncController {
    @Autowired
    private IAdEmployeeVSyncService adEmployeeVSyncService;



    /**
     * 分页列表查询
     * @param adEmployeeVSync
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping(value = "/list")
    @LoginIgnore
    public Result<?> queryPageList(
            @ModelAttribute AdEmployeeVSync adEmployeeVSync,
            @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
            @RequestParam(name="pageSize", defaultValue="10") Integer pageSize) {
        IPage<AdEmployeeVSync> page = new Page<>(pageNo, pageSize);
        LambdaQueryWrapper<AdEmployeeVSync> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtils.isBlank(adEmployeeVSync.getSyncDate())) {
            lambdaQueryWrapper.eq(AdEmployeeVSync::getSyncDate,adEmployeeVSync.getSyncDate());
        }
        if (!StringUtils.isBlank(adEmployeeVSync.getHrNo())) {
            lambdaQueryWrapper.eq(AdEmployeeVSync::getHrNo,adEmployeeVSync.getHrNo());
        }
        return Result.ok(adEmployeeVSyncService.page(page,lambdaQueryWrapper));
    }

    /**
     * @param site
     * @param syncDate
     * @return
     */
    @GetMapping(value = "/syncData")
    public Result<?> syncData( @RequestParam String site, @RequestParam String syncDate) {
        return adEmployeeVSyncService.syncData(site,syncDate);
    }
}
