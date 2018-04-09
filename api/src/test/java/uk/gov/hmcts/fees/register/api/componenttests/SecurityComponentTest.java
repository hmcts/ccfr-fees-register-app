package uk.gov.hmcts.fees.register.api.componenttests;

import org.junit.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecurityComponentTest extends ComponentTestBase {

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
