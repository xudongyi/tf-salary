package business.mapper;

import java.util.List;
import java.util.Map;

public interface HrMapper {
    Map<String,Object> getMobilePhone(String workcode);

    List<Map<String,Object>> departMentAllQuery(String departMentName);

    List<Map<String,Object>> departMentAll(String departMentName);

    List<Map<String,Object>> getHrmResource(String lastname);

    List<Map<String,Object>> getAllHrmResourceByDate(String belongDate,String site);

    List<Map<String,Object>> getAllHrmResource();

    List<Map<String,Object>> subDepartMentAll(String departMentName);

    List<Map<String,Object>> departMentAllBySub(String subid);

    Map<String,Object> getSiteDepartMent(String departMentId);

    List<Map<String,Object>> getDeptByDepartName(String departName,String belongDate,String site);


}
