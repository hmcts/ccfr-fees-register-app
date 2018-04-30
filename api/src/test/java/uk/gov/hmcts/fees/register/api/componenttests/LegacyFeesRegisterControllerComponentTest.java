package uk.gov.hmcts.fees.register.api.componenttests;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import uk.gov.hmcts.fees.register.legacymodel.FixedFee;

import javax.transaction.Transactional;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = MOCK)
@ActiveProfiles({"embedded", "idam-backdoor"})
@Transactional
public class LegacyFeesRegisterControllerComponentTest {

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
    public void categoriesShouldResultIn200() throws Exception {
        restActions
            .get("/fees-register/categories")
            .andExpect(status().isOk());
    }

    @Test
    public void unknownCategoryShouldResultIn404() throws Exception {
        restActions
            .get("/fees-register/categories/unknown")
            .andExpect(status().isNotFound());
    }

    @Test
    public void knownCategoryShouldResultIn200() throws Exception {
        restActions
            .get("/fees-register/categories/onlinefees")
            .andExpect(status().isOk());
    }

    @Test
    public void invalidRangeOnlineFeesShouldResultIn404() throws Exception {
        restActions
            .get("/fees-register/categories/onlinefees/ranges/-1/fees")
            .andExpect(status().isNotFound());
    }

    @Test
    public void invalidRangeOnlineFeesClaimAmountZeroShouldResultIn404() throws Exception {
        restActions
            .get("/fees-register/categories/onlinefees/ranges/0/fees")
            .andExpect(status().isNotFound());
    }

    @Test
    public void validRangeOnlineFeesShouldResultIn200() throws Exception {
        restActions
            .get("/fees-register/categories/onlinefees/ranges/1/fees")
            .andExpect(status().isOk())
            .andExpect(body().isEqualTo(new FixedFee("X0024", "Civil Court fees - Money Claims Online - Claim Amount - 0.01 upto 300 GBP", 2500)));
    }

    @Test
    public void invalidRangeHearingFeesClaimAmountZeroShouldResultIn404() throws Exception {
        restActions
            .get("/fees-register/categories/hearingfees/ranges/0/fees")
            .andExpect(status().isNotFound());
    }

    @Test
    public void validRangeHearingFeesForClaimAmount3000PoundsShouldResultIn200() throws Exception {
        restActions
            .get("/fees-register/categories/hearingfees/ranges/300000/fees")
            .andExpect(status().isOk())
            .andExpect(content().json("" +
                "{\"chargeableFee\":17000,\"id\":\"X0052\",\"description\":\"Civil Court fees - Hearing fees - Claim Amount - 1500.01 upto 3000 GBP\",\"amount\":17000,\"type\":\"fixed\"}"));
    }

    @Test
    public void rangeWithPercentageFeeShouldReturnCalculatedFee() throws Exception {
        restActions
            .get("/fees-register/categories/onlinefees/ranges/1500000/fees")
            .andExpect(status().isOk())
            .andExpect(content().json("{ \"chargeableFee\" : 67500}"));
    }

    @Test
    public void invalidRangeHearingFeesShouldResultIn404() throws Exception {
        restActions
            .get("/fees-register/categories/hearingfees/ranges/-1/fees")
            .andExpect(status().isNotFound());
    }


    @Test
    public void validFlatShouldResultIn200() throws Exception {
        restActions
            .get("/fees-register/categories/hearingfees/flat/X0046")
            .andExpect(status().isOk())
            .andExpect(body().isEqualTo(new FixedFee("X0046", "Civil Court fees - Hearing fees - Multi track claim", 109000)));
    }

    @Test
    public void invalidFlatShouldResultIn404() throws Exception {
        restActions
            .get("/fees-register/categories/hearingfees/flat/X0000")
            .andExpect(status().isNotFound());

    }
}

