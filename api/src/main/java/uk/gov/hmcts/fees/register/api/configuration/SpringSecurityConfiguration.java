package uk.gov.hmcts.fees.register.api.configuration;


import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import uk.gov.hmcts.fees.register.api.filter.V1EndpointsPathParamSecurityFilter;
import uk.gov.hmcts.fees2.register.util.SecurityUtils;
import uk.gov.hmcts.reform.authorisation.filters.ServiceAuthFilter;
import uk.gov.hmcts.reform.authorisation.validators.AuthTokenValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:application.properties")
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {


    private V1EndpointsPathParamSecurityFilter v1EndpointsPathParamSecurityFilter;


    @Autowired
    public SpringSecurityConfiguration(final Function<HttpServletRequest, Optional<String>> userIdExtractor,
                                       final Function<HttpServletRequest, Collection<String>> authorizedRolesExtractor,
                                       final SecurityUtils securityUtils) {
        this.v1EndpointsPathParamSecurityFilter = new V1EndpointsPathParamSecurityFilter(userIdExtractor, authorizedRolesExtractor, securityUtils);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/swagger-ui.html",
            "/webjars/springfox-swagger-ui/**",
            "/swagger-resources/**",
            "/v2/**",
            "/health",
            "/health/liveness",
            "/info");
    }

    @Override
    @SuppressFBWarnings(value = "SPRING_CSRF_PROTECTION_DISABLED", justification = "It's safe to disable CSRF protection as application is not being hit directly from the browser")
    protected void configure(HttpSecurity http) throws Exception {
        http
            .addFilterAfter(v1EndpointsPathParamSecurityFilter, BearerTokenAuthenticationFilter.class)
            .sessionManagement().sessionCreationPolicy(STATELESS).and()
            .csrf().disable()
            .formLogin().disable()
            .logout().disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/fees-register/ranged-fees", "/fees-register/fixed-fees", "/fees-register/banded-fees", "/fees-register/relational-fees", "/fees-register/rateable-fees", "/fees/**/versions").hasAnyAuthority("freg-editor")
            .antMatchers(HttpMethod.POST, "/fees-register/bulk-fixed-fees").hasAuthority("freg-editor")
            .antMatchers(HttpMethod.PUT, "/fees-register/ranged-fees/**", "/fees-register/fixed-fees/**", "/fees-register/banded-fees/**", "/fees-register/relational-fees/**", "/fees-register/rateable-fees/**").hasAuthority("freg-editor")
            .antMatchers(HttpMethod.PATCH, "/fees/**/versions/**/approve").hasAuthority("freg-approver")
            .antMatchers(HttpMethod.PATCH, "/fees/**/versions/**/reject").hasAuthority("freg-approver")
            .antMatchers(HttpMethod.PATCH, "/fees/**/versions/**/submit-for-review").hasAuthority("freg-editor")
            .antMatchers(HttpMethod.DELETE, "/fees-register/fees/**", "/fees/**/versions/**").hasAnyAuthority("freg-editor", "freg-admin")
            .antMatchers(HttpMethod.GET, "/fees-register/fees/**").permitAll();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
