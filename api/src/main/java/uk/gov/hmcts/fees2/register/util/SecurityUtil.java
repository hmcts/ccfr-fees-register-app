package uk.gov.hmcts.fees2.register.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    private static final Logger LOG = LoggerFactory.getLogger(SecurityUtil.class);

    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
            .filter(a -> role.equalsIgnoreCase(a.getAuthority()))
            .findFirst()
            .isPresent();
    }
}
