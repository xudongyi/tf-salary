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
            vo.setId(m.get("DEPARTID").toString());
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
            map.put("ID",m.get("HRIDS"));
            map.put("LABEL",m.get("HR_NAME")+"("+m.get("DEPART_NAME")+")");
            serachList.add(map);
        }
        return Result.ok(serachList);
    }

    @Override
    public Map<String,String> getAllHrmResource() {
        List<Map<String,Object>> hrmResource = hrMapper.getAllHrmResource();
        Map<String,String> hrmResourceMap = new HashMap<String,String>();
        for(Map<String,Object> m :hrmResource){
            hrmResourceMap.put(m.get("HR_NO").toString(),m.get("DEPARTID").toString());
        }
        return hrmResourceMap;
    }

    @Override
    public List<Map<String, Object>> getDeptByDepartName(String departName) {
        return hrMapper.getDeptByDepartName(departName);
    }

    @Override
    public Result<?> subDepartMentAll(String departMentName) {
        List<Map<String,Object>> departMentAll = hrMapper.subDepartMentAll(departMentName);
        List<TreeSelectSimpleVO> treeSelectSimpleVOS = new ArrayList<>();
        for(Map<String,Object> m : departMentAll){
            TreeSelectSimpleVO vo = new TreeSelectSimpleVO();
            vo.setId(m.get("DEPARTID").toString());
            vo.setTitle(m.get("LABEL").toString());
            vo.setSelectable(true);
            vo.setValue(m.get("VALUE").toString());
            treeSelectSimpleVOS.add(vo);
        }
        return Result.ok(treeSelectSimpleVOS);
    }

    @Override
    public Result<?> departMentAllBySub(String subid) {
        List<Map<String,Object>> departMentAll = hrMapper.departMentAllBySub(subid);
        List<TreeSelectSimpleVO> treeSelectSimpleVOS = new ArrayList<>();
        for(Map<String,Object> m : departMentAll){
            TreeSelectSimpleVO vo = new TreeSelectSimpleVO();
            vo.setId(m.get("DEPARTID").toString());
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
}
