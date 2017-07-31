package uk.gov.hmcts.fees.register.api.controllers.categories;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees.register.api.contract.CategoryDto;
import uk.gov.hmcts.fees.register.api.controllers.rangegroups.RangeGroupsDtoMapper;
import uk.gov.hmcts.fees.register.api.model.Category;

@Component
public class CategoryDtoMapper {
    private final RangeGroupsDtoMapper rangeGroupsDtoMapper;

    @Autowired
    public CategoryDtoMapper(RangeGroupsDtoMapper rangeGroupsDtoMapper) {
        this.rangeGroupsDtoMapper = rangeGroupsDtoMapper;
    }

    public CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
            category.getId(),
            category.getCode(),
            category.getDescription(),
            rangeGroupsDtoMapper.toRangeGroupDto(category.getRangeGroup())
        );
    }
}

