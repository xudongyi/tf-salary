package business.service.impl;

import business.bean.AdEmployeeVSync;
import business.common.api.vo.Result;
import business.mapper.AdEmployeeVSyncMapper;
import business.service.IAdEmployeeVSyncService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdEmployeeVSyncServiceImpl extends ServiceImpl<AdEmployeeVSyncMapper, AdEmployeeVSync> implements IAdEmployeeVSyncService {


    @Override
    public Result<?> syncData(String site, String syncDate) {
        //1.先删除这个分部下面的这个月的全部数据
        getBaseMapper().deleteAllAdEmployeeVSyncByDate(site,syncDate);
        //2.插入数据
        List<AdEmployeeVSync> all = getBaseMapper().getAllAdEmployeeV(site);
        for(AdEmployeeVSync vSync :all){
            vSync.setSyncDate(syncDate);
        }
        //同步因为树导致的一些特殊节点没有选中的数据
   /*     List<AdEmployeeVSync> allWidhOutTree = getBaseMapper().getAllAdEmployeeVWidthOutTree();
        for(AdEmployeeVSync vSync :allWidhOutTree){
            if(Integer.parseInt(getBaseMapper().checkAdEmployeeV(vSync.getDepartid(),syncDate).get("COUNTS").toString())>0){
                continue;
            }
            vSync.setSyncDate(syncDate);
            all.add(vSync);
        }*/
        boolean result = saveBatch(all);
        if(result){
            return Result.ok(all.size());
        }
        return Result.error("同步失败！");
    }
}