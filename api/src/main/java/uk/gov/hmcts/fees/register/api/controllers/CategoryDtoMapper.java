package uk.gov.hmcts.fees.register.api.controllers;


import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees.register.api.contract.CategoryDto;
import uk.gov.hmcts.fees.register.api.model.Category;

@Component
public class CategoryDtoMapper {

    public CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(),category.getTitle(),category.getRangeGroupId());
    }
}

