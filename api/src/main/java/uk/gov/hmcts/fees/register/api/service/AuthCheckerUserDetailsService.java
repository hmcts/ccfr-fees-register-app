package uk.gov.hmcts.fees.register.api.service;

import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;
import uk.gov.hmcts.fees.register.api.security.model.UserDetails;

@Service
public class AuthCheckerUserDetailsService<T extends PreAuthenticatedAuthenticationToken> implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {
    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {

        final UserInfo principal = (UserInfo) token.getPrincipal();
        final UserInfo user = (UserInfo) principal;

        return new UserDetails(user.getUid(), (String) token.getCredentials(), user.getRoles());
    }
}
