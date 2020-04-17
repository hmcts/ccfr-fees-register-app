package uk.gov.hmcts.fees2.register.util;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = MOCK)
@ActiveProfiles({"embedded", "idam-backdoor"})
public class SecurityUtilsTest {

    @Autowired
    private SecurityUtils securityUtils;

    @Test
    public void shouldReturnFalseWhenIsAuthenticationInvoked() {
        final boolean authenticated = securityUtils.isAuthenticated();
        Assert.assertTrue(authenticated);
    }

}
