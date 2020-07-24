package uk.gov.hmcts.fees.register.functional.idam;

import com.fasterxml.jackson.annotation.JsonProperty;
import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

public interface IdamApi {

    @RequestMapping(method = RequestMethod.POST, value = "/testing-support/accounts")
    @RequestLine("POST /testing-support/accounts")
    @Headers("Content-Type: application/json")
    void createUser(CreateUserRequest createUserRequest);

    @RequestLine("POST /o/token")
    @Headers({"Authorization: {authorization}", "Content-Type: application/x-www-form-urlencoded"})
    @Body("response_type={response_type}&username={username}&password={password}&grant_type={grant_type}&client_id={client_id}&client_secret={client_secret}&scope={scope}&redirect_uri={redirect_uri}")
    AuthenticateUserResponse authenticateUser(@Param("username") String username,
                                              @Param("password") String password,
                                              @Param("grant_type") String grantType,
                                              @Param("client_id") String clientId,
                                              @Param("client_secret") String clientSecret,
                                              @Param("scope") String scope,
                                              @Param("redirect_uri") String redirectUri);

    @Data
    @AllArgsConstructor
    @Builder(builderMethodName = "userRequestWith")
    class CreateUserRequest {
        private final String email;
        private final String forename = "John";
        private final String surname = "Smith";
        private final UserGroup userGroup;
        private final List<Role> roles;
        private final String password;
    }

    @AllArgsConstructor
    @Getter
    class UserGroup {
        private String code;
    }

    @AllArgsConstructor
    @Getter
    class Role {
        private String code;
    }

    @Data
    class AuthenticateUserResponse {
        @JsonProperty("id_token")
        private String idToken;

        @JsonProperty("access_token")
        private String accessToken;
    }

}
