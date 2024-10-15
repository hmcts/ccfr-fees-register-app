package uk.gov.hmcts.fees.register.api.componenttests;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import uk.gov.hmcts.fees.register.api.componenttests.backdoors.UserResolverBackdoor;
import uk.gov.hmcts.fees.register.api.componenttests.sugar.CustomResultMatcher;
import uk.gov.hmcts.fees.register.api.componenttests.sugar.RestActions;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = MOCK)
@ActiveProfiles({"embedded", "idam-backdoor"})
@Transactional
public class SecurityComponentTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    protected UserResolverBackdoor userRequestAuthorizer;

    @Autowired
    private WebApplicationContext webApplicationContext;

    RestActions restActions;

    @Before
    public void setUp() {
        MockMvc mvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        this.restActions = new RestActions(mvc, userRequestAuthorizer, objectMapper);
    }

    CustomResultMatcher body() {
        return new CustomResultMatcher(objectMapper);
    }

    @Test
    public void anonymousRetrieveFeeAllowed() throws Exception {
        restActions.get("/fees").andExpect(status().isOk());
        restActions.get("/fees/X0433").andExpect(status().isOk());
    }

    @Test
    public void anonymousRetrieveCategoryAllowed() throws Exception {
        restActions.get("/categories").andExpect(status().isOk());
        restActions.get("/categories/cmc-online").andExpect(status().isOk());
    }

    @Test
    public void anonymousRetrieveRangeGroupAllowed() throws Exception {
        restActions.get("/range-groups").andExpect(status().isOk());
        restActions.get("/range-groups/cmc-online").andExpect(status().isOk());
    }

    @Test
    public void anonymousUpdateFeeForbidden() throws Exception {
        restActions
            .put("/fees/X0433", "any body")
            .andExpect(status().isBadRequest());
    }

    @Test
    public void anonymousUpdateRangeGroupForbidden() throws Exception {
        restActions
            .put("/range-groups/cmc-online", "any body")
            .andExpect(status().isBadRequest());
    }

    @Test
    public void anonymousUpdateCategoryForbidden() throws Exception {
        restActions
            .withUser("admin")
            .put("/categories/cmc-online", "any body")
            .andExpect(status().isBadRequest());
    }
}
