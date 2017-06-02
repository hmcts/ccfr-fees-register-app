package uk.gov.hmcts.register.fees.componenttests;

import org.junit.Test;
import uk.gov.hmcts.register.fees.model.FixedFee;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FeesControllerComponentTest extends ComponentTestBase {

    @Test
    public void unknownCategoryShouldResultIn404() throws Exception {
        restActions
                .get("/fees-register/cmc/unknown/fees?amount=100")
                .andExpect(status().isNotFound());
    }

    @Test
    public void invalidRangeShouldResultIn404() throws Exception {
        restActions
                .get("/fees-register/cmc/onlinefees/fees?amount=0")
                .andExpect(status().isNotFound());
    }

    @Test
    public void validCategoryAndAmountShouldReturnFee() throws Exception {
        restActions
                .get("/fees-register/cmc/onlinefees/fees?amount=1")
                .andExpect(status().isOk())
                .andExpect(body().isEqualTo(new FixedFee("X0024", "Civil Court fees - Money Claims Online - Claim Amount - 0 upto 300", 2500)));
    }

}
