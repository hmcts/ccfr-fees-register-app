package uk.gov.hmcts.fees.register.api.componenttests;


import org.junit.Test;
import uk.gov.hmcts.fees.register.api.contract.CategoryDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class CategoryCrudComponentTest extends ComponentTestBase {

    @Test
    public void retrieveAllCategories() throws Exception {
        restActions
            .get("/categories")
            .andExpect(status().isOk())
            .andExpect(body().asListOf(CategoryDto.class, (categories) -> {
                assertThat(categories).anySatisfy(category -> {
                    assertThat(category.getId()).isEqualTo(4);
                    assertThat(category.getCode()).isEqualTo("probate-copies");
                    assertThat(category.getDescription()).isEqualTo("Probate - Copies");
                    assertThat(category.getRangeGroup().getId()).isEqualTo(4);
                    assertThat(category.getRangeGroup().getRanges()).hasSize(2);
                });
            }));
    }

    @Test
    public void retrieveById() throws Exception {
        restActions
            .get("/categories/1")
            .andExpect(status().isOk())
            .andExpect(body().as(CategoryDto.class, (category) -> {
                assertThat(category.getId()).isEqualTo(1);
                assertThat(category.getCode()).isEqualTo("cmc-online");
                assertThat(category.getDescription()).isEqualTo("CMC - Online fees");
                assertThat(category.getRangeGroup().getId()).isEqualTo(1);
                assertThat(category.getRangeGroup().getRanges()).hasSize(8);
            }));
    }

    @Test
    public void retrieveByUnknownId() throws Exception {
        restActions
            .get("/categories/-1")
            .andExpect(status().isNotFound());
    }
}
