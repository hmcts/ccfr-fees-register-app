package uk.gov.hmcts.fees2.register.data.service;

import org.springframework.util.MultiValueMap;

public interface IdamService {

    String getUserName(MultiValueMap<String, String> headers, final String userId);
}
