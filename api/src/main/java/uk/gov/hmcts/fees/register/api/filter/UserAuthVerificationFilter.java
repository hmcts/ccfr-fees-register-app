package uk.gov.hmcts.fees.register.api.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;
import uk.gov.hmcts.fees.register.api.exception.UnauthorizedException;
import uk.gov.hmcts.fees2.register.util.SecurityUtils;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;

public class UserAuthVerificationFilter extends OncePerRequestFilter {

    private static final Logger LOG = LoggerFactory.getLogger(UserAuthVerificationFilter.class);

    private final Function<HttpServletRequest, Optional<String>> userIdExtractor;
    private final Function<HttpServletRequest, Collection<String>> authorizedRolesExtractor;
    private final SecurityUtils securityUtils;

    public UserAuthVerificationFilter(final Function<HttpServletRequest, Optional<String>> userIdExtractor,
                                      final Function<HttpServletRequest, Collection<String>> authorizedRolesExtractor,
                                      final SecurityUtils securityUtils) {
        this.userIdExtractor = userIdExtractor;
        this.authorizedRolesExtractor = authorizedRolesExtractor;
        this.securityUtils = securityUtils;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
        throws ServletException, IOException {
        LOG.info("Inside filter: UserAuthVerificationFilter");
        Collection<String> authorizedRoles = authorizedRolesExtractor.apply(request);
        Optional<String> userIdOptional = userIdExtractor.apply(request);
        final String bearerToken = request.getHeader(SecurityUtils.AUTHORISATION);
        UserInfo userInfo = null;
        if ((securityUtils.isAuthenticated() || bearerToken != null) && (!authorizedRoles.isEmpty() || userIdOptional.isPresent())) {
            try {
                userInfo = verifyRoleAndUserId(authorizedRoles, userIdOptional, request);
            } catch (UnauthorizedException ex) {
                LOG.warn("Unauthorised roles or userId in the request path", ex);
                response.sendError(HttpStatus.FORBIDDEN.value(), "Access Denied");
                return;
            }
            PreAuthenticatedAuthenticationToken authResult = new PreAuthenticatedAuthenticationToken(userInfo, request.getHeader(SecurityUtils.AUTHORISATION));
            SecurityContextHolder.getContext().setAuthentication(authResult);
        }

        filterChain.doFilter(request, response);
    }

    private UserInfo verifyRoleAndUserId(Collection<String> authorizedRoles, Optional<String> userIdOptional, HttpServletRequest httpRequest) {
        UserInfo userInfo = securityUtils.getUserInfo(httpRequest);

        if (userInfo != null && !authorizedRoles.isEmpty() && Collections.disjoint(authorizedRoles, userInfo.getRoles())) {
            throw new UnauthorizedException("Unauthorised role in the path");
        }
        userIdOptional.ifPresent(resourceUserId -> {
            if (!resourceUserId.equalsIgnoreCase(userInfo.getUid())) {
                throw new UnauthorizedException("Unauthorised userId in the path");
            }
        });
        return userInfo;
    }

}
