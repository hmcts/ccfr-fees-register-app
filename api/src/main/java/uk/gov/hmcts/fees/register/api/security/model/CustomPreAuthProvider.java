package uk.gov.hmcts.fees.register.api.security.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees.register.api.service.AuthCheckerUserDetailsService;

import javax.annotation.PostConstruct;

@Component("preAuthProvider")
public class CustomPreAuthProvider extends PreAuthenticatedAuthenticationProvider {

    @Autowired
    private AuthCheckerUserDetailsService<PreAuthenticatedAuthenticationToken> userService;

    public CustomPreAuthProvider(){
        super();
    }

    @PostConstruct
    public void init(){
        super.setPreAuthenticatedUserDetailsService(userService);
    }

}
