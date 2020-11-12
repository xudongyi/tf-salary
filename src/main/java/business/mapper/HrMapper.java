package business.mapper;

import java.util.List;
import java.util.Map;

public interface HrMapper {
    Map<String,Object> getMobilePhone(String workcode);

    List<Map<String,Object>> departMentAll(String departMentName);

    List<Map<String,Object>> getHrmResource(String lastname);

    List<Map<String,Object>> getAllHrmResource();

    List<Map<String,Object>> subDepartMentAll(String departMentName);

    List<Map<String,Object>> departMentAllBySub(String subid);

    Map<String,Object> getSiteDepartMent(String departMentCode);

}
