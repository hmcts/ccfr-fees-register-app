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
import uk.gov.hmcts.fees.register.api.contract.CalculationDto;
import uk.gov.hmcts.fees.register.api.contract.ErrorDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = MOCK)
@ActiveProfiles({"embedded", "idam-backdoor"})
@Transactional
public class RangeGroupsCalculationsComponentTest {

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
    public void calculateInclusive() throws Exception {
        restActions
            .get("/range-groups/cmc-online/calculations?value=50000")
            .andExpect(status().isOk())
            .andExpect(body().as(CalculationDto.class, dto -> {
                assertThat(dto.getAmount()).isEqualTo(3500);
                assertThat(dto.getFee().getCode()).isEqualTo("X0025");
            }));

        restActions
            .get("/range-groups/cmc-online/calculations?value=50001")
            .andExpect(status().isOk())
            .andExpect(body().as(CalculationDto.class, dto -> {
                assertThat(dto.getAmount()).isEqualTo(6000);
                assertThat(dto.getFee().getCode()).isEqualTo("X0026");
            }));
    }

    @Test
    public void maxFeesForUnspecifiedClaimAmount() throws Exception {
        restActions
            .get("/range-groups/cmc-paper/calculations/unspecified")
            .andExpect(status().isOk())
            .andExpect(body().as(CalculationDto.class, dto -> {
                assertThat(dto.getAmount()).isEqualTo(1000000);
            }));
    }


    @Test
    public void calculateOutOfRange() throws Exception {
        restActions
            .get("/range-groups/cmc-online/calculations?value=999999999")
            .andExpect(status().isNotFound())
            .andExpect(body().as(ErrorDto.class, dto -> {
                assertThat(dto.getMessage()).isEqualTo("range for value=999999999 was not found");
            }));
    }
}
