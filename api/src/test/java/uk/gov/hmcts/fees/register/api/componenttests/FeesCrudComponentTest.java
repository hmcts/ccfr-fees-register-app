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
import uk.gov.hmcts.fees.register.api.contract.FeeDto;
import uk.gov.hmcts.fees.register.api.contract.FixedFeeDto;
import uk.gov.hmcts.fees.register.api.contract.FixedFeeDto.FixedFeeDtoBuilder;
import uk.gov.hmcts.fees.register.api.contract.PercentageFeeDto;
import uk.gov.hmcts.fees.register.api.contract.PercentageFeeDto.PercentageFeeDtoBuilder;

import javax.transaction.Transactional;
import java.math.BigDecimal;

import static java.lang.String.join;
import static java.util.Collections.nCopies;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static uk.gov.hmcts.fees.register.api.contract.FixedFeeDto.fixedFeeDtoWith;
import static uk.gov.hmcts.fees.register.api.contract.PercentageFeeDto.percentageFeeDtoWith;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = MOCK)
@ActiveProfiles({"embedded", "idam-backdoor"})
@Transactional
public class FeesCrudComponentTest {

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
    public void retrieveAll() throws Exception {
        restActions
            .get("/fees")
            .andExpect(status().isOk())
            .andExpect(body().asListOf(FeeDto.class, fees -> {
                assertThat(fees).contains(
                    fixedFeeDtoWith()
                        .code("X0433")
                        .description("Civil Court fees - Money Claims Online - Claim Amount - 5000.01 upto 10000 GBP")
                        .amount(41000)
                        .build(),

                    percentageFeeDtoWith()
                        .code("X0434")
                        .description("Civil Court fees - Money Claims Online - Claim Amount - 10000.01 upto 15000 GBP. Fees are 4.5% of the claim value")
                        .percentage(BigDecimal.valueOf(4.5))
                        .build()
                );
            }));
    }

    @Test
    public void retrieveOne() throws Exception {
        restActions
            .get("/fees/X0434")
            .andExpect(status().isOk())
            .andExpect(body().as(FeeDto.class, fee -> {
                assertThat(fee).isEqualTo(
                    percentageFeeDtoWith()
                        .code("X0434")
                        .description("Civil Court fees - Money Claims Online - Claim Amount - 10000.01 upto 15000 GBP. Fees are 4.5% of the claim value")
                        .percentage(BigDecimal.valueOf(4.5))
                        .build()
                );
            }));
    }

    @Test
    public void retrieveNonExisting() throws Exception {
        restActions
            .get("/fees/-1")
            .andExpect(status().isNotFound());
    }

    @Test
    public void updatePercentageFee() throws Exception {
        PercentageFeeDtoBuilder proposedFee = percentageFeeDtoWith()
            .code("ignored")
            .percentage(BigDecimal.valueOf(10.15))
            .description("New Description");

        restActions
            .withUser("admin")
            .put("/fees/X0434", proposedFee.build())
            .andExpect(status().isOk())
            .andExpect(body().as(PercentageFeeDto.class, fee -> {
                assertThat(fee).isEqualTo(proposedFee.code("X0434").build());
            }));
    }

    @Test
    public void updateFixedFee() throws Exception {
        FixedFeeDtoBuilder proposedFee = fixedFeeDtoWith()
            .code("ignored")
            .amount(10000)
            .description("New Description");

        restActions
            .withUser("admin")
            .put("/fees/X0433", proposedFee.build())
            .andExpect(status().isOk())
            .andExpect(body().as(FixedFeeDto.class, fee -> {
                assertThat(fee).isEqualTo(proposedFee.code("X0433").build());
            }));
    }

    @Test
    public void createFee() throws Exception {
        FixedFeeDtoBuilder proposedFee = fixedFeeDtoWith()
            .amount(10000)
            .description("New Description");

        restActions
            .withUser("admin")
            .put("/fees/X9999", proposedFee.build())
            .andExpect(status().isOk())
            .andExpect(body().as(FixedFeeDto.class, fee -> {
                assertThat(fee).isEqualTo(proposedFee.code("X9999").build());
            }));
    }

    @Test
    public void prohibitTypeChange() throws Exception {
        FixedFeeDtoBuilder proposedFee = fixedFeeDtoWith().code("ignored").amount(10000).description("any");
        assertValidationMessage("/fees/X0434", proposedFee.build(), "FeeOld type can't be changed");
    }

    @Test
    public void validateCode() throws Exception {
        assertValidationMessage("/fees/" + join("", nCopies(51, "A")), validFixedFeeDto().build(), "code: length must be between 0 and 50");
    }

    @Test
    public void validateDescription() throws Exception {
        assertValidationMessage("/fees/X0433", validFixedFeeDto().description(null).build(), "description: may not be empty");
        assertValidationMessage("/fees/X0433", validFixedFeeDto().description("").build(), "description: may not be empty");
        assertValidationMessage("/fees/X0433", validFixedFeeDto().description(join("", nCopies(2001, "A"))).build(), "description: length must be between 0 and 2000");
    }

    @Test
    public void validateAmount() throws Exception {
        assertValidationMessage("/fees/X0433", validFixedFeeDto().amount(null).build(), "amount: may not be null");
        assertValidationMessage("/fees/X0433", validFixedFeeDto().amount(-1).build(), "amount: must be greater than or equal to 0");
    }

    @Test
    public void validatePercentage() throws Exception {
        assertValidationMessage("/fees/X0434", validPercentageFeeDto().percentage(null).build(), "percentage: may not be null");
        assertValidationMessage("/fees/X0434", validPercentageFeeDto().percentage(BigDecimal.valueOf(-1)).build(), "percentage: must be greater than or equal to 0.01");
        assertValidationMessage("/fees/X0434", validPercentageFeeDto().percentage(BigDecimal.valueOf(0)).build(), "percentage: must be greater than or equal to 0.01");
        assertValidationMessage("/fees/X0434", validPercentageFeeDto().percentage(BigDecimal.valueOf(100.01)).build(), "percentage: must be less than or equal to 100.00");
    }

    private PercentageFeeDtoBuilder validPercentageFeeDto() {
        return percentageFeeDtoWith().percentage(BigDecimal.valueOf(1.01)).description("any");
    }

    private FixedFeeDtoBuilder validFixedFeeDto() {
        return fixedFeeDtoWith().amount(10).description("any");
    }

    private void assertValidationMessage(String urlTemplate, FeeDto feeDto, String message) throws Exception {
        restActions
            .withUser("admin")
            .put(urlTemplate, feeDto)
            .andExpect(status().isBadRequest())
            .andExpect(body().isErrorWithMessage(message));
    }

}
