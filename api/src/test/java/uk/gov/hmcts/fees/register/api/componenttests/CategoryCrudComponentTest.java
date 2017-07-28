package uk.gov.hmcts.fees.register.api.componenttests;


import org.junit.Test;
import uk.gov.hmcts.fees.register.api.contract.CategoryDto;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.hmcts.fees.register.api.contract.CategoryDto.categoryDtoWith;


public class CategoryCrudComponentTest extends ComponentTestBase{


    @Test
    public void retrieveAllCategories() throws Exception {
        restActions
            .get("/categories")
            .andExpect(status().isOk())
            .andExpect(body().isListContaining(
                CategoryDto.class,
                categoryDtoWith()
                    .id(1)
                    .title("onlinefees")
                    .rangeGroupId(1)
                    .build()

            ));
    }

    @Test
    public void retrieveById() throws Exception {
       restActions
            .get("/categories/1")
            .andExpect(status().isOk())
            .andExpect(body().isEqualTo(
                categoryDtoWith()
                    .id(1)
                    .title("onlinefees")
                    .rangeGroupId(1)
                    .build()
            ));
    }

    @Test
    public void retrieveByUnknownId() throws Exception {
        restActions
            .get("/categories/-1")
            .andExpect(status().isNotFound());
    }
}
