package uk.gov.hmcts.fees.register.api.componenttests.backdoors;

import org.springframework.security.core.GrantedAuthority;

public class RoleMock implements GrantedAuthority {

    private String auth;

    @Override
    public String getAuthority() {
        return auth;
    }

    protected void setAuthority(final String role) {
        this.auth = role;
    }

}
