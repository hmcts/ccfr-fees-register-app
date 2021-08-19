package uk.gov.hmcts.fees2.register.data.service;

import java.security.Principal;

public interface IdamService {

    String getUserName(Principal principal, final String userId);
}
