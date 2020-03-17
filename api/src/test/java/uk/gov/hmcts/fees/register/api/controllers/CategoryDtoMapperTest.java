package uk.gov.hmcts.fees.register.api.controllers;


import org.junit.Test;
import uk.gov.hmcts.fees.register.api.contract.CategoryDto;
import uk.gov.hmcts.fees.register.api.contract.FeeDto;
import uk.gov.hmcts.fees.register.api.contract.FixedFeeDto;
import uk.gov.hmcts.fees.register.api.contract.RangeGroupDto;
import uk.gov.hmcts.fees.register.api.controllers.categories.CategoryDtoMapper;
import uk.gov.hmcts.fees.register.api.controllers.fees.FeesDtoMapper;
import uk.gov.hmcts.fees.register.api.controllers.rangegroups.RangeGroupsDtoMapper;
import uk.gov.hmcts.fees.register.api.model.Category;
import uk.gov.hmcts.fees.register.api.model.FeeOld;
import uk.gov.hmcts.fees.register.api.model.FixedFeeOld;
import uk.gov.hmcts.fees.register.api.model.RangeGroup;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.fees.register.api.contract.FixedFeeDto.fixedFeeDtoWith;
import static uk.gov.hmcts.fees.register.api.contract.RangeGroupDto.rangeGroupDtoWith;
import static uk.gov.hmcts.fees.register.api.model.FixedFeeOld.fixedFeeWith;

public class CategoryDtoMapperTest {

    private static final RangeGroup SOME_RANGE_GROUP = RangeGroup.rangeGroupWith().code("any").description("any").ranges(Collections.emptyList()).build();
    private static final RangeGroupDto MAPPED_RANGE_GROUP_DTO = rangeGroupDtoWith().code("any").description("any").build();
    private static final FixedFeeOld SOME_FEE = fixedFeeWith().code("any").description("any").build();
    private static final FixedFeeDto MAPPED_FEE_DTO = fixedFeeDtoWith().code("any").description("any").build();

    private final CategoryDtoMapper categoryDtoMapper = new CategoryDtoMapper(
        new RangeGroupsDtoMapper(null, null) {
            @Override
            public RangeGroupDto toRangeGroupDto(RangeGroup rangeGroup) {
                return MAPPED_RANGE_GROUP_DTO;
            }
        },
        null, new FeesDtoMapper() {
            @Override
            public FeeDto toFeeDto(FeeOld fee) {
                return MAPPED_FEE_DTO;
            }
        }, null);

    @Test
    public void convertsToCategoryDto() {
        assertThat(categoryDtoMapper.toCategoryDto(
            Category.categoryWith()
                .code("code")
                .description("description")
                .rangeGroup(SOME_RANGE_GROUP)
                .fees(Arrays.asList(SOME_FEE, SOME_FEE))
                .build()
            )
        ).isEqualTo(
            CategoryDto.categoryDtoWith()
                .code("code")
                .description("description")
                .rangeGroup(MAPPED_RANGE_GROUP_DTO)
                .fees(Arrays.asList(MAPPED_FEE_DTO, MAPPED_FEE_DTO))
                .build());
    }

}
