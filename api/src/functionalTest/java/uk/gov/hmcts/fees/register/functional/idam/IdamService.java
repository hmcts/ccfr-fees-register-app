package uk.gov.hmcts.fees.register.functional.idam;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.fees.register.functional.config.TestConfigProperties;
import uk.gov.hmcts.fees.register.functional.idam.IdamApi.CreateUserRequest;
import uk.gov.hmcts.fees.register.functional.idam.IdamApi.Role;
//import uk.gov.hmcts.fees.register.functional.idam.IdamApi.TokenExchangeResponse;
import uk.gov.hmcts.fees.register.functional.idam.IdamApi.UserGroup;
import uk.gov.hmcts.fees.register.functional.idam.models.*;

import java.util.Base64;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static uk.gov.hmcts.fees.register.functional.idam.IdamApi.CreateUserRequest.*;

@Service
public class IdamService {

    public static final String BEARER = "Bearer ";
    public static final String AUTHORIZATION_CODE = "authorization_code";
    public static final String CODE = "code";
    public static final String BASIC = "Basic ";
    public static final String USER_GROUP = "freg-users";
    public static final String GRANT_TYPE = "password";
    public static final String SCOPE = "openid profile roles";

    private final IdamApi idamApi;
    private final TestConfigProperties testConfig;

    @Autowired
    public IdamService(TestConfigProperties testConfig) {
        this.testConfig = testConfig;
        idamApi = Feign.builder()
            .encoder(new JacksonEncoder())
            .decoder(new JacksonDecoder())
            .target(IdamApi.class, testConfig.getIdamApiUrl());
    }


    public User createUserWith(String... roles) {
        String email = nextUserEmail();
        CreateUserRequest userRequest = userRequest(email, roles);
        idamApi.createUser(userRequest);

        String accessToken = authenticateUser(email, testConfig.getTestUserPassword());
        return User.userWith()
            .authorisationToken(accessToken)
            .email(email)
            .build();
    }

    public String authenticateUser(final String username, final String password) {

        final IdamApi.AuthenticateUserResponse authenticateUserResponse = idamApi.authenticateUser(
            username,
            password,
            GRANT_TYPE,
            testConfig.getOauth2().getClientId(),
            testConfig.getOauth2().getClientSecret(),
            SCOPE,
            testConfig.getOauth2().getRedirectUrl()
            );

        return BEARER + authenticateUserResponse.getAccessToken();
    }


    private CreateUserRequest userRequest(String email, String[] roles) {
        return userRequestWith()
            .email(email)
            .password(testConfig.getTestUserPassword())
            .roles(Stream.of(roles)
                .map(Role::new)
                .collect(toList()))
            .userGroup(new UserGroup(USER_GROUP))
            .build();
    }

    private String nextUserEmail() {
        return String.format(testConfig.getGeneratedUserEmailPattern(), RandomStringUtils.randomAlphanumeric(10));
    }
}
