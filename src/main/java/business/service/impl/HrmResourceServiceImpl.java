package business.service.impl;

import business.bean.HrmResource;
import business.mapper.AuthTokenMapper;
import business.mapper.HrmResourceMapper;
import business.service.IHrmResourceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Description: 数采仪
 * @Author: jeecg-boot
 * @Date:   2020-07-07
 * @Version: V1.0
 */
@Service
public class HrmResourceServiceImpl extends ServiceImpl<HrmResourceMapper, HrmResource> implements IHrmResourceService {
    @Resource
    private HrmResourceMapper hrmResourceMapper;
    @Override
    public Map<String, Object> getHrmResource(String loginId) {
        return hrmResourceMapper.getHrmResource(loginId);
    }
}
