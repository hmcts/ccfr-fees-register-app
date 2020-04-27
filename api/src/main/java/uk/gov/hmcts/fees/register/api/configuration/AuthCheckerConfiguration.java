package uk.gov.hmcts.fees.register.api.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import uk.gov.hmcts.fees.register.api.repositories.IdamRepository;
import uk.gov.hmcts.fees2.register.util.SecurityUtils;
import uk.gov.hmcts.reform.idam.client.IdamApi;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@Lazy
@EnableFeignClients(basePackageClasses = {IdamApi.class})
public class AuthCheckerConfiguration {

    @Value("#{'${trusted.user.roles}'.split(',')}")
    private List<String> authorizedRoles;

    @Bean
    public Function<HttpServletRequest, Collection<String>> authorizedRolesExtractor() {
        return (any) -> authorizedRoles;
    }

    @Bean
    public Function<HttpServletRequest, Optional<String>> userIdExtractor() {
        return (request) -> Optional.empty();
    }

}
