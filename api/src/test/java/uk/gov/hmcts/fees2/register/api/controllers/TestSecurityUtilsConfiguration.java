package uk.gov.hmcts.fees2.register.api.controllers;

import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import uk.gov.hmcts.fees.register.api.componenttests.backdoors.SecurityUtilsMock;
import uk.gov.hmcts.fees.register.api.configuration.AuthCheckerConfiguration;
import uk.gov.hmcts.fees.register.api.configuration.SpringSecurityConfiguration;
import uk.gov.hmcts.fees2.register.util.SecurityUtils;

@TestConfiguration
@Import({SpringSecurityConfiguration.class, AuthCheckerConfiguration.class})
public class TestSecurityUtilsConfiguration {
    @Bean
    //@ConditionalOnProperty(name = "idam.client.backdoor", havingValue = "true")
    public SecurityUtils securityUtils() {
        return new SecurityUtils(null);
    }

}
