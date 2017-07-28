package uk.gov.hmcts.fees.register.api.controllers;


import org.junit.Test;
import uk.gov.hmcts.fees.register.api.contract.CategoryDto;
import uk.gov.hmcts.fees.register.api.model.Category;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryDtoMapperTest {


    private final CategoryDtoMapper mapper = new CategoryDtoMapper();

    @Test
    public void convertsCategory() {
        assertThat(mapper.toCategoryDto(new Category(1, "onlinefees", 1)))
            .isEqualTo(new CategoryDto(1, "onlinefees", 1));
    }

}
