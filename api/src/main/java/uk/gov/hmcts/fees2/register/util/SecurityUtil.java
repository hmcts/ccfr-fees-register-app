package uk.gov.hmcts.fees2.register.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
            .map(a -> role.equalsIgnoreCase(a.getAuthority()))
            .findFirst()
            .orElse(false);
    }
}
