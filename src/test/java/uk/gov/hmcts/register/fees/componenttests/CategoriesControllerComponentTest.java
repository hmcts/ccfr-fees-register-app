package uk.gov.hmcts.register.fees.componenttests;

import org.junit.Test;
import uk.gov.hmcts.register.fees.model.FixedFee;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CategoriesControllerComponentTest extends ComponentTestBase {

    @Test
    public void categoriesShouldResultIn200() throws Exception {
        restActions
                .get("/fees-register/cmc/categories")
                .andExpect(status().isOk());
    }

    @Test
    public void unknownCategoryShouldResultIn404() throws Exception {
        restActions
                .get("/fees-register/cmc/categories/unknown")
                .andExpect(status().isNotFound());
    }

    @Test
    public void knownCategoryShouldResultIn200() throws Exception {
        restActions
                .get("/fees-register/cmc/categories/onlinefees")
                .andExpect(status().isOk());
    }

    @Test
    public void invalidRangeShouldResultIn404() throws Exception {
        restActions
                .get("/fees-register/cmc/categories/onlinefees/ranges/-1/fees")
                .andExpect(status().isNotFound());
    }

    @Test
    public void validRangeShouldResultIn200() throws Exception {
        restActions
                .get("/fees-register/cmc/categories/onlinefees/ranges/1/fees")
                .andExpect(status().isOk())
                .andExpect(body().isEqualTo(new FixedFee("X0024", "Civil Court fees - Money Claims Online - Claim Amount - 0 upto 300", 2500)));
    }
}

