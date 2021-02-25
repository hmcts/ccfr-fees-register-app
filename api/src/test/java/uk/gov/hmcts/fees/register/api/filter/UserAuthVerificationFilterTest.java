package uk.gov.hmcts.fees.register.api.filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import uk.gov.hmcts.fees2.register.util.SecurityUtils;
import uk.gov.hmcts.fees.register.api.configuration.AuthCheckerConfiguration;
import uk.gov.hmcts.reform.idam.client.models.UserInfo;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.lenient;

@RunWith(MockitoJUnitRunner.class)
public class UserAuthVerificationFilterTest {
    @Mock
    private SecurityUtils securityUtils;
    private UserAuthVerificationFilter filter;
    private MockHttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @Mock
    SecurityContext securityContext;

    @Mock
    Authentication authentication;



    @Before
    public void setUp() {
        AuthCheckerConfiguration config = new AuthCheckerConfiguration();
        filter = new UserAuthVerificationFilter(config.userIdExtractor(),
            config.authorizedRolesExtractor(),
            securityUtils);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = new MockFilterChain();

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldReturn200ResponseWhenRoleMatches() throws Exception {
        request.setRequestURI("/fees-register/");
        lenient().when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(getJWTAuthenticationTokenBasedOnRoles("payments"));
        lenient().when(securityUtils.getUserInfo(request)).thenReturn(getUserInfoBasedOnUID_Roles("user123","freg-editor"));
        filter.doFilterInternal(request, response, filterChain);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


    public static UserInfo getUserInfoBasedOnUID_Roles(String UID, String roles){
        return UserInfo.builder()
            .uid(UID)
            .roles(Arrays.asList(roles))
            .build();
    }

    @SuppressWarnings("unchecked")
    private JwtAuthenticationToken getJWTAuthenticationTokenBasedOnRoles(String authority) {
        List<String> stringGrantedAuthority = new ArrayList();
        stringGrantedAuthority.add(authority);

        Map<String,Object> claimsMap = new HashMap<>();
        claimsMap.put("roles", stringGrantedAuthority);

        Map<String,Object> headersMap = new HashMap<>();
        headersMap.put("authorisation","test-token");

        Jwt jwt = new Jwt("test_token",null,null,headersMap,claimsMap);
        return new JwtAuthenticationToken(jwt);
    }

}
