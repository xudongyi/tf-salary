package business.service.impl;

import business.common.api.vo.Result;
import business.mapper.OAMapper;
import business.service.IOAService;
import business.vo.TreeSelectSimpleVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Service
public class OAServiceImpl implements IOAService {
    @Resource
    private OAMapper oaMapper;

    @Override
    public Result<?> departMentAll(String departMentName) {
        List<Map<String,Object>> departMentAll = oaMapper.departMentAll(departMentName);
        List<TreeSelectSimpleVO> treeSelectSimpleVOS = new ArrayList<>();
        for(Map<String,Object> m : departMentAll){
            TreeSelectSimpleVO vo = new TreeSelectSimpleVO();
            vo.setId(Integer.parseInt(m.get("ID").toString()));
            vo.setTitle(m.get("LABEL").toString());
            vo.setpId(Integer.parseInt(m.get("PID").toString()));
            vo.setValue(m.get("VALUE").toString());
            treeSelectSimpleVOS.add(vo);
        }
        return Result.ok(treeSelectSimpleVOS);
    }
}
