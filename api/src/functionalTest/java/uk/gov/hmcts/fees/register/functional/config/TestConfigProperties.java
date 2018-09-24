package uk.gov.hmcts.fees.register.functional.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class TestConfigProperties {

    @Autowired
    private Oauth2 oauth2;

    @Value("${base.test.url}")
    private String baseTestUrl;

    @Value("${generated.user.email.pattern}")
    private String generatedUserEmailPattern;

    @Value("${test.user.password}")
    private String testUserPassword;

    @Value("${idam.api.url}")
    private String idamApiUrl;

}
