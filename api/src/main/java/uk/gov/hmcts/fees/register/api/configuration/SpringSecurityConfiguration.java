package uk.gov.hmcts.fees.register.api.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import uk.gov.hmcts.reform.auth.checker.core.RequestAuthorizer;
import uk.gov.hmcts.reform.auth.checker.core.user.User;
import uk.gov.hmcts.reform.auth.checker.spring.useronly.AuthCheckerUserOnlyFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration {

    private final RequestAuthorizer<User> userRequestAuthorizer;
    private final AuthenticationManager authenticationManager;

    public SpringSecurityConfiguration(
        RequestAuthorizer<User> userRequestAuthorizer,
        AuthenticationManager authenticationManager
    ) {
        this.userRequestAuthorizer = userRequestAuthorizer;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Public filter chain. Allow *ALL* GET requests and PUT requests to specific endpoints.
     * PUT requests are allowed to match configuration that was before
     * Spring Boot upgrade to V3.
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    @Order(1)
    public SecurityFilterChain publicFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatchers(match -> match
                .requestMatchers(HttpMethod.GET)
                .requestMatchers(
                    HttpMethod.PUT,
                    "/categories/**",
                    "/fees/**",
                    "/fees/*/versions/**",
                    "/range-groups/**"
                )
            )
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().permitAll()
            );

        return http.build();
    }


    @Bean
    @Order(2)
    public SecurityFilterChain serviceFilterChain(HttpSecurity http) throws Exception {
        AuthCheckerUserOnlyFilter<User> authCheckerUserOnlyFilter =
            new AuthCheckerUserOnlyFilter<>(userRequestAuthorizer);

        authCheckerUserOnlyFilter.setAuthenticationManager(authenticationManager);

        http
            .addFilter(authCheckerUserOnlyFilter)
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    HttpMethod.POST,
                    "/fees-register/banded-fees",
                    "/fees-register/bulk-fixed-fees",
                    "/fees-register/fixed-fees",
                    "/fees-register/ranged-fees",
                    "/fees-register/rateable-fees",
                    "/fees-register/relational-fees",
                    "/fees/*/versions").hasAnyAuthority("freg-editor")
                .requestMatchers(
                    HttpMethod.PUT,
                    "/fees-register/banded-fees/**",
                    "/fees-register/fixed-fees/**",
                    "/fees-register/ranged-fees/**",
                    "/fees-register/rateable-fees/**",
                    "/fees-register/relational-fees/**",
                    "/fees/*/versions").hasAnyAuthority("freg-editor")
                .requestMatchers(
                    HttpMethod.PATCH,
                    "/fees/*/versions/*/approve",
                    "/fees/*/versions/*/reject").hasAuthority("freg-approver")
                .requestMatchers(
                    HttpMethod.PATCH,
                    "/fees/*/versions/*/submit-for-review").hasAuthority("freg-editor")
                .requestMatchers(
                    HttpMethod.DELETE,
                    "/fees-register/fees/**",
                    "/fees/*/versions/**").hasAnyAuthority("freg-editor", "freg-admin")
            );
        return http.build();
    }
}
