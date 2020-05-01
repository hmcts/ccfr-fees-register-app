package uk.gov.hmcts.fees.register.api.componenttests.backdoors;

import com.google.common.collect.ImmutableList;
import uk.gov.hmcts.fees.register.api.repositories.IdamRepository;
import uk.gov.hmcts.reform.idam.client.IdamClient;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class IdamRepositoryMock extends IdamRepository {

    private final ConcurrentHashMap<String, UserInfo> tokenToUserMap;

    public IdamRepositoryMock(IdamClient idamClient) {
        super(idamClient);
        tokenToUserMap = new ConcurrentHashMap<>();
        tokenToUserMap.put("Bearer admin", new UserInfo(null, "123", null, null, null, null));
    }

    public UserInfo getUserInfo(final String token) {
        if (token == null) {
            return null;
        }
        return tokenToUserMap.get(token);
    }

    public void registerToken(String token, String userId) {
        final ImmutableList<String> roles = ImmutableList.of("freg-editor", "freg-approver");

        final UserInfo userDetails = new UserInfo(null, userId, null, null, null, roles);

        populateSecurityContextHolder(roles);
        tokenToUserMap.put(token, userDetails);
    }

    private void populateSecurityContextHolder(ImmutableList<String> roles) {
        final List<RoleMock> authorities = new ArrayList<>();

        for (final String role : roles) {
            final RoleMock r = new RoleMock();
            r.setAuthority(role);
            authorities.add(r);
        }
    }

}
