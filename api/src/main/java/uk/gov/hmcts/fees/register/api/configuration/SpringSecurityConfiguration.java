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
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import uk.gov.hmcts.fees.register.api.configuration.security.converter.FRJwtGrantedAuthoritiesConverter;
import uk.gov.hmcts.fees.register.api.configuration.security.validator.AudienceValidator;
import uk.gov.hmcts.fees.register.api.filter.UserAuthVerificationFilter;
import uk.gov.hmcts.fees2.register.util.SecurityUtils;
import org.springframework.security.oauth2.jwt.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity(debug = true)
@PropertySource("classpath:application.properties")
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Value("${spring.security.oauth2.client.provider.oidc.issuer-uri}")
    private String issuerUri;

    @Value("${oidc.audience-list}")
    private String[] allowedAudiences;

    @Value("${oidc.issuer}")
    private String issuerOverride;

    private UserAuthVerificationFilter userAuthVerificationFilter;
    private JwtAuthenticationConverter jwtAuthenticationConverter;


    @Autowired
    public SpringSecurityConfiguration(final Function<HttpServletRequest, Optional<String>> userIdExtractor,
                                       final Function<HttpServletRequest, Collection<String>> authorizedRolesExtractor,
                                       final SecurityUtils securityUtils, final FRJwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter) {
        jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        this.userAuthVerificationFilter = new UserAuthVerificationFilter(userIdExtractor, authorizedRolesExtractor, securityUtils);
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
            .addFilterAfter(userAuthVerificationFilter, BearerTokenAuthenticationFilter.class)
            .sessionManagement().sessionCreationPolicy(STATELESS).and()
            .csrf().disable()
            .formLogin().disable()
            .logout().disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/fees-register/ranged-fees", "/fees-register/fixed-fees", "/fees-register/banded-fees", "/fees-register/relational-fees", "/fees-register/rateable-fees", "/fees/**/versions").hasAuthority("freg-editor")
            .antMatchers(HttpMethod.POST, "/fees-register/bulk-fixed-fees").hasAuthority("freg-editor")
            .antMatchers(HttpMethod.PUT, "/fees-register/ranged-fees/**", "/fees-register/fixed-fees/**", "/fees-register/banded-fees/**", "/fees-register/relational-fees/**", "/fees-register/rateable-fees/**").hasAuthority("freg-editor")
            .antMatchers(HttpMethod.PATCH, "/fees/**/versions/**/approve").hasAuthority("freg-approver")
            .antMatchers(HttpMethod.PATCH, "/fees/**/versions/**/reject").hasAuthority("freg-approver")
            .antMatchers(HttpMethod.PATCH, "/fees/**/versions/**/submit-for-review").hasAuthority("freg-editor")
            .antMatchers(HttpMethod.DELETE, "/fees-register/fees/**", "/fees/**/versions/**").hasAnyAuthority("freg-editor", "freg-admin")
            .antMatchers(HttpMethod.GET, "/fees-register/fees/**").permitAll()
            .and()
            .oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(jwtAuthenticationConverter);
    }

    @Bean
    @SuppressWarnings("unchecked")
    JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder)
            JwtDecoders.fromOidcIssuerLocation(issuerUri);

        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(Arrays.asList(allowedAudiences));
        // We are using issuerOverride instead of issuerUri as SIDAM has the wrong issuer at the moment
        OAuth2TokenValidator<Jwt> withTimestamp = new JwtTimestampValidator();
        // Commented issuer validation as confirmed by IDAM
//        OAuth2TokenValidator<Jwt> withIssuer = new JwtIssuerValidator(issuerOverride);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withTimestamp,
            audienceValidator);
        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;
    }
}
