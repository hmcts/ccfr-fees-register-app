package uk.gov.hmcts.fees.register.api.componenttests;

import org.junit.Test;
import uk.gov.hmcts.fees.register.legacymodel.FixedFee;
import uk.gov.hmcts.fees.register.legacymodel.PercentageFee;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.hmcts.fees.register.api.controllers.ChargeableFeeWrapperDto.chargeableFeeDtoWith;

public class LegacyFeesRegisterControllerComponentTest extends ComponentTestBase {

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
            .andExpect(body().isEqualTo(
                chargeableFeeDtoWith()
                    .fee(new FixedFee("X0052", "Civil Court fees - Hearing fees - Claim Amount - 1500.01 upto 3000 GBP", 17000))
                    .chargeableFee(17000)
                    .build(),
                FixedFee.class
            ));
    }

    @Test
    public void rangeWithPercentageFeeShouldReturnCalculatedFee() throws Exception {
        restActions
            .get("/fees-register/categories/onlinefees/ranges/1500000/fees")
            .andExpect(status().isOk())
            .andExpect(body().isEqualTo(
                chargeableFeeDtoWith()
                    .fee(new PercentageFee("X0434", "Civil Court fees - Money Claims Online - Claim Amount - 10000.01 upto 15000 GBP. Fees are 4.5% of the claim value", BigDecimal.valueOf(4.5)))
                    .chargeableFee(67500)
                    .build(),
                PercentageFee.class
            ));
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

