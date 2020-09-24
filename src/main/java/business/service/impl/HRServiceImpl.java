package business.service.impl;

import business.common.api.vo.Result;
import business.mapper.HrMapper;
import business.service.IHRService;
import business.vo.TreeSelectSimpleVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class HRServiceImpl implements IHRService {
    @Resource
    private HrMapper hrMapper;

    @Override
    public Result<?> departMentAll(String departMentName) {
        List<Map<String,Object>> departMentAll = hrMapper.departMentAll(departMentName);
        List<TreeSelectSimpleVO> treeSelectSimpleVOS = new ArrayList<>();
        for(Map<String,Object> m : departMentAll){
            TreeSelectSimpleVO vo = new TreeSelectSimpleVO();
            vo.setId(m.get("DEPART_CODE").toString());
            vo.setTitle(m.get("LABEL").toString());
            if(m.containsKey("PID")&&m.get("PID")!=null){
                vo.setpId(m.get("PID").toString());
                vo.setSelectable(true);
            }
            vo.setValue(m.get("VALUE").toString());
            treeSelectSimpleVOS.add(vo);
        }
        return Result.ok(treeSelectSimpleVOS);
    }

    @Override
    public Result<?> getHrmResource(String lastname) {
        List<Map<String,Object>> hrmResource = hrMapper.getHrmResource(lastname);
        List<Map<String,Object>> serachList = new ArrayList<>();
        for(Map<String,Object> m :hrmResource){
            Map map = new HashMap();
            map.put("ID",m.get("ID"));
            map.put("LABEL",m.get("HR_NAME")+"("+m.get("DEPART_NAME")+")");
            serachList.add(map);
        }
        return Result.ok(serachList);
    }
}
