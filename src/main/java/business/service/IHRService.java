package business.service;

import business.common.api.vo.Result;

import java.util.List;
import java.util.Map;

public interface IHRService {

    Result<?> departMentAll(String departMentName);

    Result<?> getHrmResource(String lastname);

    Map<String,String> getAllHrmResource();
}
