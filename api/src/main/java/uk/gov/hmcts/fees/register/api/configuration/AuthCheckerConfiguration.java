package uk.gov.hmcts.fees.register.api.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import uk.gov.hmcts.reform.authorisation.ServiceAuthorisationApi;
import uk.gov.hmcts.reform.authorisation.validators.AuthTokenValidator;
import uk.gov.hmcts.reform.authorisation.validators.ServiceAuthTokenValidator;
import uk.gov.hmcts.reform.idam.client.IdamApi;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Configuration
@Lazy
@EnableFeignClients(basePackageClasses = {IdamApi.class, ServiceAuthorisationApi.class})
public class AuthCheckerConfiguration {

    @Bean
    public Function<HttpServletRequest, Collection<String>> authorizedRolesExtractor() {
        return (any) -> Collections.emptyList();
    }

    @Bean
    public Function<HttpServletRequest, Optional<String>> userIdExtractor() {
        return (request) -> Optional.empty();
    }

    @Bean
    public AuthTokenValidator authTokenValidator(final ServiceAuthorisationApi serviceAuthorisationApi) {
        return new ServiceAuthTokenValidator(serviceAuthorisationApi);
    }

}
