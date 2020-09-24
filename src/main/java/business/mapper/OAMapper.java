package business.mapper;


import java.util.List;
import java.util.Map;

public interface OAMapper {
    List<Map<String,Object>> subCompanyAll();

    List<Map<String,Object>> getHrmResource(String lastname);

}
