package uk.gov.hmcts.fees2.register.api.controllers;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import uk.gov.hmcts.fees.register.api.configuration.AuthCheckerConfiguration;
import uk.gov.hmcts.fees.register.api.configuration.SpringSecurityConfiguration;
import uk.gov.hmcts.reform.auth.checker.core.RequestAuthorizer;

@TestConfiguration
@Import({SpringSecurityConfiguration.class, AuthCheckerConfiguration.class})
public class TestSecurityConfiguration {

    @Bean
    public RequestAuthorizer requestAuthorizer() {
        return Mockito.mock(RequestAuthorizer.class);
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return Mockito.mock(AuthenticationManager.class);
    }
}
