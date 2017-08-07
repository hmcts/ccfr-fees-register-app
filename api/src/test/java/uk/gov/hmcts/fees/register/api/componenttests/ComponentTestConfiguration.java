package uk.gov.hmcts.fees.register.api.componenttests;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.auth.checker.SubjectResolver;
import uk.gov.hmcts.auth.checker.user.User;
import uk.gov.hmcts.fees.register.api.componenttests.backdoors.UserResolverBackdoor;

@Configuration
public class ComponentTestConfiguration {
    @Bean
    public SubjectResolver<User> userResolver() {
        return new UserResolverBackdoor();
    }
}
