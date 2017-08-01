package uk.gov.hmcts.fees.register.api.controllers;


import java.util.Arrays;
import org.junit.Test;
import uk.gov.hmcts.fees.register.api.contract.CategoryDto;
import uk.gov.hmcts.fees.register.api.contract.FeeDto;
import uk.gov.hmcts.fees.register.api.contract.FixedFeeDto;
import uk.gov.hmcts.fees.register.api.contract.RangeGroupDto;
import uk.gov.hmcts.fees.register.api.controllers.categories.CategoryDtoMapper;
import uk.gov.hmcts.fees.register.api.controllers.fees.FeesDtoMapper;
import uk.gov.hmcts.fees.register.api.controllers.rangegroups.RangeGroupsDtoMapper;
import uk.gov.hmcts.fees.register.api.model.Category;
import uk.gov.hmcts.fees.register.api.model.Fee;
import uk.gov.hmcts.fees.register.api.model.FixedFee;
import uk.gov.hmcts.fees.register.api.model.RangeGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.fees.register.api.contract.FixedFeeDto.fixedFeeDtoWith;
import static uk.gov.hmcts.fees.register.api.contract.RangeGroupDto.rangeGroupDtoWith;
import static uk.gov.hmcts.fees.register.api.model.FixedFee.fixedFeeWith;

public class CategoryDtoMapperTest {

    private static final RangeGroup SOME_RANGE_GROUP = RangeGroup.rangeGroupWith().id(123).build();
    private static final RangeGroupDto MAPPED_RANGE_GROUP_DTO = rangeGroupDtoWith().id(123).build();
    private static final FixedFee SOME_FEE = fixedFeeWith().code("any").description("any").build();
    private static final FixedFeeDto MAPPED_FEE_DTO = fixedFeeDtoWith().code("any").description("any").build();

    private final CategoryDtoMapper categoryDtoMapper = new CategoryDtoMapper(
        new RangeGroupsDtoMapper(null) {
            @Override
            public RangeGroupDto toRangeGroupDto(RangeGroup rangeGroup) {
                return MAPPED_RANGE_GROUP_DTO;
            }
        },
        new FeesDtoMapper() {
            @Override
            public FeeDto toFeeDto(Fee fee) {
                return MAPPED_FEE_DTO;
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
                .fees(Arrays.asList(SOME_FEE, SOME_FEE))
                .build()
            )
        ).isEqualTo(
            CategoryDto.categoryDtoWith()
                .id(1)
                .code("code")
                .description("description")
                .rangeGroup(MAPPED_RANGE_GROUP_DTO)
                .fees(Arrays.asList(MAPPED_FEE_DTO, MAPPED_FEE_DTO))
                .build());
    }

}
