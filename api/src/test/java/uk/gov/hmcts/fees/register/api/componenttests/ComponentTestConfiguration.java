package uk.gov.hmcts.fees.register.api.componenttests;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.fees.register.api.componenttests.backdoors.UserResolverBackdoor;
import uk.gov.hmcts.reform.auth.checker.core.SubjectResolver;
import uk.gov.hmcts.reform.auth.checker.core.user.User;

@Configuration
public class ComponentTestConfiguration {

    @Bean
    @ConditionalOnProperty(name = "idam.client.backdoor", havingValue = "true")
    public SubjectResolver<User> userResolver() {
        return new UserResolverBackdoor();
    }
}
