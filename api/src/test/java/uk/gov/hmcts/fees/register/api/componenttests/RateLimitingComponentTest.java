package uk.gov.hmcts.fees.register.api.componenttests;

import jakarta.transaction.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = MOCK)
@ActiveProfiles({"embedded", "idam-backdoor"})
@TestPropertySource(properties = {
    "resilience4j.ratelimiter.instances.fees-register-api.limit-for-period=1",
    "resilience4j.ratelimiter.instances.fees-register-api.limit-refresh-period=1m",
    "resilience4j.ratelimiter.instances.fees-register-api.timeout-duration=0s"
})
@DirtiesContext
@Transactional
public class RateLimitingComponentTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        mvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    public void shouldRateLimitControllerEndpoints() throws Exception {
        mvc.perform(get("/fees")).andExpect(status().isOk());

        mvc.perform(get("/fees")).andExpect(status().isTooManyRequests());
    }
}
