package uk.gov.hmcts.fees2.register.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.fees.register.api.filter.UserAuthVerificationFilter;
import uk.gov.hmcts.fees.register.api.repositories.IdamRepository;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service
public class SecurityUtils {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityUtils.class);

    public static final String AUTHORISATION = "Authorization";

    private final IdamRepository idamRepository;

    @Autowired
    public SecurityUtils(final IdamRepository idamRepository) {
        this.idamRepository = idamRepository;
    }

    public UserInfo getUserInfo(final HttpServletRequest httpRequest) {
        final String userToken = getUserToken(httpRequest);
        LOG.info("The value of accessToken: " + userToken);
        if (userToken == null) {
            return null;
        }
        return idamRepository.getUserInfo(userToken);
    }

    public String getUserToken(final HttpServletRequest httpRequest) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Jwt jwt = authentication != null ? (Jwt) authentication.getPrincipal():null;
        return jwt != null ? jwt.getTokenValue() : httpRequest.getHeader(AUTHORISATION);
    }

    public boolean isAuthenticated() {
        return Objects.nonNull(SecurityContextHolder.getContext().getAuthentication());
    }

}
