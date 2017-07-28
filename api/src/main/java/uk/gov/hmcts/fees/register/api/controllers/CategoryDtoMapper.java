package uk.gov.hmcts.fees.register.api.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees.register.api.contract.CategoryDto;
import uk.gov.hmcts.fees.register.api.model.Category;

@Component
public class CategoryDtoMapper {

    @Autowired
    private RangeGroupsDtoMapper rangeGroupsDtoMapper;

    public CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getTitle(),
            rangeGroupsDtoMapper.toRangeGroupDto(category.getRangeGroup()));
    }
}

