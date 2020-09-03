package business.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;

import java.util.Map;

@DS("hr")
public interface HrMapper {
    Map<String,Object> getMobilePhone(String workcode);
}
