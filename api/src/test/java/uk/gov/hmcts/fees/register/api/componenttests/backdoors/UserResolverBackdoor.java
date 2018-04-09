package uk.gov.hmcts.fees.register.api.componenttests.backdoors;

import com.google.common.collect.ImmutableSet;
import uk.gov.hmcts.auth.checker.SubjectResolver;
import uk.gov.hmcts.auth.checker.exceptions.AuthCheckerException;
import uk.gov.hmcts.auth.checker.user.User;

import java.util.concurrent.ConcurrentHashMap;

public class UserResolverBackdoor implements SubjectResolver<User> {
    private final ConcurrentHashMap<String, String> tokenToUserMap = new ConcurrentHashMap<>();

    public UserResolverBackdoor() {
        tokenToUserMap.put("Bearer admin", "123");
    }

    @Override
    public User getTokenDetails(String token) {
        String userId = tokenToUserMap.get(token);

        if (userId == null) {
            throw new AuthCheckerException("Token not found");
        }

        return new User(userId, ImmutableSet.of("freg-finance-admin"));
    }

    public void registerToken(String token, String userId) {
        tokenToUserMap.put(token, userId);
    }
}
