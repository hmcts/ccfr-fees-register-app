package uk.gov.hmcts.fees.register.functional.idam;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.fees.register.functional.config.TestConfigProperties;
import uk.gov.hmcts.fees.register.functional.idam.IdamApi.CreateUserRequest;
import uk.gov.hmcts.fees.register.functional.idam.IdamApi.Role;
import uk.gov.hmcts.fees.register.functional.idam.IdamApi.TokenExchangeResponse;
import uk.gov.hmcts.fees.register.functional.idam.IdamApi.UserGroup;
import uk.gov.hmcts.fees.register.functional.idam.models.*;

import java.util.Base64;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static uk.gov.hmcts.fees.register.functional.idam.IdamApi.CreateUserRequest.*;

@Service
public class IdamService {

    public static final String BEARER = "Bearer ";
    public static final String AUTHORIZATION_CODE = "authorization_code";
    public static final String CODE = "code";
    public static final String BASIC = "Basic ";
    public static final String USER_GROUP = "freg-finance-admin";

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
       // String email = "fee-test-aUser@feemail.com";
        CreateUserRequest userRequest = userRequest(email, roles);
        idamApi.createUser(userRequest);

        String accessToken = authenticateUser(email, testConfig.getTestUserPassword());
        return User.userWith()
            .authorisationToken(accessToken)
            .email(email)
            .build();
    }

    public String authenticateUser(String username, String password) {
        String authorisation = username + ":" + password;
        String base64Authorisation = Base64.getEncoder().encodeToString(authorisation.getBytes());

        IdamApi.AuthenticateUserResponse authenticateUserResponse = idamApi.authenticateUser(
            BASIC + base64Authorisation,
            CODE,
            testConfig.getOauth2().getClientId(),
            testConfig.getOauth2().getRedirectUrl());

        TokenExchangeResponse tokenExchangeResponse = idamApi.exchangeCode(
            authenticateUserResponse.getCode(),
            AUTHORIZATION_CODE,
            testConfig.getOauth2().getClientId(),
            testConfig.getOauth2().getClientSecret(),
            testConfig.getOauth2().getRedirectUrl()
        );

        return BEARER + tokenExchangeResponse.getAccessToken();
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
        return format(testConfig.getGeneratedUserEmailPattern(), RandomStringUtils.randomAlphanumeric(10));
    }
}
