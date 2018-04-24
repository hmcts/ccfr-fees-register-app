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
import uk.gov.hmcts.fees.register.api.contract.CalculationDto;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = MOCK)
@ActiveProfiles({"embedded", "idam-backdoor"})
@Transactional
public class FeesCalculationsComponentTest {

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
    public void calculateFixed() throws Exception {
        restActions
            .get("/fees/X0433/calculations?value=999999999")
            .andExpect(status().isOk())
            .andExpect(body().as(CalculationDto.class, dto -> {
                assertThat(dto.getAmount()).isEqualTo(41000);
                assertThat(dto.getFee().getCode()).isEqualTo("X0433");
            }));
    }

    @Test
    public void calculatePercentage() throws Exception {
        restActions
            .get("/fees/X0434/calculations?value=1000")
            .andExpect(status().isOk())
            .andExpect(body().as(CalculationDto.class, dto -> {
                assertThat(dto.getAmount()).isEqualTo(45);
                assertThat(dto.getFee().getCode()).isEqualTo("X0434");
            }));
    }
}
