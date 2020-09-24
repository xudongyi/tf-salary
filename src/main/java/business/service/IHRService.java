package business.service;

import business.common.api.vo.Result;

public interface IHRService {

    Result<?> departMentAll(String departMentName);

    Result<?> getHrmResource(String lastname);
}
