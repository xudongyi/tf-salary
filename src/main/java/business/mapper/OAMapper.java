package business.mapper;


import java.util.List;
import java.util.Map;

public interface OAMapper {
    List<Map<String,Object>> departMentAll(String departMentName);

    List<Map<String,Object>> subCompanyAll();

}
