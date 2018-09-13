package uk.gov.hmcts.fees2.register.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uk.gov.hmcts.fees2.register.api.controllers.FeeController;

public class SecurityUtil {
    private static final Logger LOG = LoggerFactory.getLogger(SecurityUtil.class);

    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOG.info("User authorities :{}", authentication.getAuthorities());
        return authentication.getAuthorities().stream()
            .map(a -> role.equalsIgnoreCase(a.getAuthority()))
            .findFirst()
            .orElse(false);
    }
}
