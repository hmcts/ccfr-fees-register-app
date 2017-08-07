package uk.gov.hmcts.fees.register.api.componenttests;

import org.junit.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecurityComponentTest extends ComponentTestBase {

    @Test
    public void anonymousUpdateFeeForbidden() throws Exception {
        restActions
            .put("/fees/X0433", "any body")
            .andExpect(status().isForbidden());
    }

    @Test
    public void anonymousUpdateRangeGroupForbidden() throws Exception {
        restActions
            .put("/range-groups/cmc-online", "any body")
            .andExpect(status().isForbidden());
    }

    @Test
    public void anonymousUpdateCategoryForbidden() throws Exception {
        restActions
            .put("/categories/cmc-online", "any body")
            .andExpect(status().isForbidden());
    }
}
