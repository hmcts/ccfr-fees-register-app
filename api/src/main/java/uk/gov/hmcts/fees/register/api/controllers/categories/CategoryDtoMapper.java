package uk.gov.hmcts.fees.register.api.controllers.categories;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees.register.api.contract.CategoryDto;
import uk.gov.hmcts.fees.register.api.controllers.fees.FeesDtoMapper;
import uk.gov.hmcts.fees.register.api.controllers.rangegroups.RangeGroupsDtoMapper;
import uk.gov.hmcts.fees.register.api.model.Category;

import static java.util.stream.Collectors.toList;

@Component
public class CategoryDtoMapper {
    private final RangeGroupsDtoMapper rangeGroupsDtoMapper;
    private final FeesDtoMapper feesDtoMapper;

    @Autowired
    public CategoryDtoMapper(RangeGroupsDtoMapper rangeGroupsDtoMapper, FeesDtoMapper feesDtoMapper) {
        this.rangeGroupsDtoMapper = rangeGroupsDtoMapper;
        this.feesDtoMapper = feesDtoMapper;
    }

    public CategoryDto toCategoryDto(Category category) {
        return CategoryDto.categoryDtoWith()
            .id(category.getId())
            .code(category.getCode())
            .description(category.getDescription())
            .rangeGroup(rangeGroupsDtoMapper.toRangeGroupDto(category.getRangeGroup()))
            .fees(category.getFees().stream().map(feesDtoMapper::toFeeDto).collect(toList()))
            .build();
    }
}

