package uk.gov.hmcts.fees.register.api.controllers;


import org.junit.Test;
import uk.gov.hmcts.fees.register.api.contract.CategoryDto;
import uk.gov.hmcts.fees.register.api.contract.RangeGroupDto;
import uk.gov.hmcts.fees.register.api.controllers.categories.CategoryDtoMapper;
import uk.gov.hmcts.fees.register.api.controllers.rangegroups.RangeGroupsDtoMapper;
import uk.gov.hmcts.fees.register.api.model.Category;
import uk.gov.hmcts.fees.register.api.model.RangeGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.fees.register.api.contract.RangeGroupDto.rangeGroupDtoWith;

public class CategoryDtoMapperTest {

    private static final RangeGroup SOME_RANGE_GROUP = RangeGroup.rangeGroupWith().id(123).build();
    private static final RangeGroupDto MAPPED_RANGE_GROUP_DTO = rangeGroupDtoWith().id(123).build();

    private final CategoryDtoMapper categoryDtoMapper = new CategoryDtoMapper(new RangeGroupsDtoMapper(null) {
        @Override
        public RangeGroupDto toRangeGroupDto(RangeGroup rangeGroup) {
            return MAPPED_RANGE_GROUP_DTO;
        }
    });

    @Test
    public void convertsToCategoryDto() {
        assertThat(categoryDtoMapper.toCategoryDto(
            Category.categoryWith()
                .id(1)
                .code("code")
                .description("description")
                .rangeGroup(SOME_RANGE_GROUP)
                .build()
            )
        ).isEqualTo(
            CategoryDto.categoryDtoWith()
                .id(1)
                .code("code")
                .description("description")
                .rangeGroupDto(MAPPED_RANGE_GROUP_DTO)
                .build());
    }

}
