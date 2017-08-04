package uk.gov.hmcts.fees.register.api.controllers.categories;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees.register.api.contract.CategoryDto;
import uk.gov.hmcts.fees.register.api.contract.CategoryUpdateDto;
import uk.gov.hmcts.fees.register.api.controllers.fees.FeesDtoMapper;
import uk.gov.hmcts.fees.register.api.controllers.rangegroups.RangeGroupsDtoMapper;
import uk.gov.hmcts.fees.register.api.model.Category;
import uk.gov.hmcts.fees.register.api.model.FeeRepository;
import uk.gov.hmcts.fees.register.api.model.RangeGroupRepository;

import static java.util.stream.Collectors.toList;

@Component
public class CategoryDtoMapper {
    private final RangeGroupsDtoMapper rangeGroupsDtoMapper;
    private final RangeGroupRepository rangeGroupRepository;
    private final FeesDtoMapper feesDtoMapper;
    private final FeeRepository feeRepository;

    @Autowired
    public CategoryDtoMapper(RangeGroupsDtoMapper rangeGroupsDtoMapper, RangeGroupRepository rangeGroupRepository, FeesDtoMapper feesDtoMapper, FeeRepository feeRepository) {
        this.rangeGroupsDtoMapper = rangeGroupsDtoMapper;
        this.rangeGroupRepository = rangeGroupRepository;
        this.feesDtoMapper = feesDtoMapper;
        this.feeRepository = feeRepository;
    }

    public CategoryDto toCategoryDto(Category category) {
        return CategoryDto.categoryDtoWith()
            .code(category.getCode())
            .description(category.getDescription())
            .rangeGroup(rangeGroupsDtoMapper.toRangeGroupDto(category.getRangeGroup()))
            .fees(category.getFees().stream().map(feesDtoMapper::toFeeDto).collect(toList()))
            .build();
    }

    public Category toCategory(String code, CategoryUpdateDto dto) {
        return Category.categoryWith()
            .code(code)
            .description(dto.getDescription())
            .fees(dto.getFeeCodes().stream().map(feeRepository::findByCodeOrThrow).collect(toList()))
            .rangeGroup(rangeGroupRepository.findByCodeOrThrow(dto.getRangeGroupCode()))
            .build();
    }
}

