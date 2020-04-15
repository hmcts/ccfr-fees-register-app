package uk.gov.hmcts.fees.register.api.componenttests;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.hmcts.fees.register.api.componenttests.backdoors.IdamRepositoryMock;
import uk.gov.hmcts.fees.register.api.componenttests.backdoors.SecurityUtilsMock;
import uk.gov.hmcts.fees.register.api.componenttests.backdoors.UserResolverBackdoor;
import uk.gov.hmcts.fees.register.api.repositories.IdamRepository;
import uk.gov.hmcts.fees2.register.util.SecurityUtils;
import uk.gov.hmcts.reform.auth.checker.core.SubjectResolver;
import uk.gov.hmcts.reform.auth.checker.core.user.User;

@Configuration
public class ComponentTestConfiguration {

    @Bean
    @ConditionalOnProperty(name = "idam.client.backdoor", havingValue = "true")
    public SubjectResolver<User> userResolver() {
        return new UserResolverBackdoor();
    }

    @Bean
    @ConditionalOnProperty(name = "idam.client.backdoor", havingValue = "true")
    public SecurityUtils securityUtils() {
        return new SecurityUtilsMock(idamRepository());
    }

    @Bean
    @ConditionalOnProperty(name = "idam.client.backdoor", havingValue = "true")
    public IdamRepository idamRepository() {
        return new IdamRepositoryMock(null);
    }


}
