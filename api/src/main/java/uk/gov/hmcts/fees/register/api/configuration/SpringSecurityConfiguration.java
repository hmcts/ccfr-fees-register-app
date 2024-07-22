package uk.gov.hmcts.fees.register.api.configuration;


import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import uk.gov.hmcts.reform.auth.checker.corer.RequestAuthorizer;
import uk.gov.hmcts.reform.auth.checker.core.user.User;
import uk.gov.hmcts.reform.auth.checker.spring.useronly.AuthCheckerUserOnlyFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AuthCheckerUserOnlyFilter authCheckerFilter;

    @Autowired
    public SpringSecurityConfiguration(final RequestAuthorizer<User> userRequestAuthorizer,
                                       final AuthenticationManager authenticationManager) {
        authCheckerFilter = new AuthCheckerUserOnlyFilter(userRequestAuthorizer);
        authCheckerFilter.setAuthenticationManager(authenticationManager);
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers("/swagger-ui.html",
                "/webjars/springfox-swagger-ui/**",
                "/swagger-resources/**",
                "/v2/**",
                "/health",
                "/health/liveness",
                "/health/readiness",
                "/info");
    }

    @Override
    @SuppressFBWarnings(value = "SPRING_CSRF_PROTECTION_DISABLED", justification = "It's safe to disable CSRF protection as application is not being hit directly from the browser")
    protected void configure(final HttpSecurity http) throws Exception {
        authCheckerFilter.setAuthenticationManager(authenticationManager());

        http
                .addFilter(authCheckerFilter)
                .sessionManagement().sessionCreationPolicy(STATELESS).and()
                .csrf().disable()
                .formLogin().disable()
                .logout().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/fees-register/ranged-fees", "/fees-register/fixed-fees",
                        "/fees-register/banded-fees", "/fees-register/relational-fees", "/fees-register/rateable-fees",
                        "/fees/**/versions").hasAnyAuthority("freg-editor")
                .antMatchers(HttpMethod.POST, "/fees-register/bulk-fixed-fees").hasAuthority("freg-editor")
                .antMatchers(HttpMethod.PUT, "/fees-register/ranged-fees/**", "/fees-register/fixed-fees/**",
                        "/fees-register/banded-fees/**", "/fees-register/relational-fees/**",
                        "/fees-register/rateable-fees/**").hasAuthority("freg-editor")
                .antMatchers(HttpMethod.PATCH, "/fees/**/versions/**/approve").hasAuthority("freg-approver")
                .antMatchers(HttpMethod.PATCH, "/fees/**/versions/**/reject").hasAuthority("freg-approver")
                .antMatchers(HttpMethod.PATCH, "/fees/**/versions/**/submit-for-review").hasAuthority("freg-editor")
                .antMatchers(HttpMethod.DELETE, "/fees-register/fees/**", "/fees/**/versions/**")
                .hasAnyAuthority("freg-editor", "freg-admin")
                .antMatchers(HttpMethod.GET, "/fees-register/fees/**").permitAll();
    }
}
