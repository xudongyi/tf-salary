package business.service.impl;

import business.common.api.vo.Result;
import business.mapper.OAMapper;
import business.service.IOAService;
import business.vo.TreeSelectSimpleVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
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
            vo.setSelectable(Integer.parseInt(m.get("IS_SUB").toString())==1);
            treeSelectSimpleVOS.add(vo);
        }
        return Result.ok(treeSelectSimpleVOS);
    }

    @Override
    public Result<?> getHrmResource(String lastname) {
        List<Map<String,Object>> hrmResource = oaMapper.getHrmResource(lastname);
        List<Map<String,Object>> serachList = new ArrayList<>();
        for(Map<String,Object> m :hrmResource){
            Map map = new HashMap();
            map.put("ID",m.get("ID"));
            map.put("LABEL",m.get("LASTNAME")+"("+m.get("DEPARTMENTNAME")+")");
            serachList.add(map);
        }
        return Result.ok(serachList);
    }
}
