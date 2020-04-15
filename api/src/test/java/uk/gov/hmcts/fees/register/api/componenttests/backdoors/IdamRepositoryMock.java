package uk.gov.hmcts.fees.register.api.componenttests.backdoors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import uk.gov.hmcts.fees.register.api.repositories.IdamRepository;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;

import java.util.concurrent.ConcurrentHashMap;

public class IdamRepositoryMock extends IdamRepository {

    private final ConcurrentHashMap<String, UserInfo> tokenToUserMap;

    public IdamRepositoryMock(IdamClient idamClient) {
        super(idamClient);
        tokenToUserMap = new ConcurrentHashMap<>();
        tokenToUserMap.put("Bearer admin", new UserInfo(null, "123", null, null, null, null));
    }

    public UserInfo getUserInfo(final String token) {
        return tokenToUserMap.get(token);
    }

    public void registerToken(String token, String userId) {
        final UserInfo userDetails = new UserInfo(null, userId, null, null, null, ImmutableList.of("freg-editor", "freg-approver"));
        tokenToUserMap.put(token, userDetails);
    }

}
