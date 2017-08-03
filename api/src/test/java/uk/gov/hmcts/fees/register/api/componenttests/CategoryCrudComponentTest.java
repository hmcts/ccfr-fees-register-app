package uk.gov.hmcts.fees.register.api.componenttests;


import org.junit.Test;
import uk.gov.hmcts.fees.register.api.contract.CategoryDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.hmcts.fees.register.api.contract.FixedFeeDto.fixedFeeDtoWith;


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
                    assertThat(category.getRangeGroup().getCode()).isEqualTo("probate-copies");
                    assertThat(category.getRangeGroup().getRanges()).hasSize(2);
                    assertThat(category.getFees()).hasSize(2);
                    assertThat(category.getFees()).contains(fixedFeeDtoWith()
                        .code("X0251-4")
                        .description("‘Sealed and certified copy’ – if assets are held abroad you may need one of these. Please check with the appropriate organisations before ordering.")
                        .amount(50)
                        .build());
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
                assertThat(category.getRangeGroup().getCode()).isEqualTo("cmc-online");
                assertThat(category.getRangeGroup().getRanges()).hasSize(8);
                assertThat(category.getFees()).isEmpty();
            }));
    }

    @Test
    public void retrieveByUnknownId() throws Exception {
        restActions
            .get("/categories/-1")
            .andExpect(status().isNotFound());
    }
}
