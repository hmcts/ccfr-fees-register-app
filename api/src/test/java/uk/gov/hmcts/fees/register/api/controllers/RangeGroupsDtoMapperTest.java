package uk.gov.hmcts.fees.register.api.controllers;

import java.util.Arrays;
import org.junit.Test;
import uk.gov.hmcts.fees.register.api.contract.FeeDto;
import uk.gov.hmcts.fees.register.api.controllers.fees.FeesDtoMapper;
import uk.gov.hmcts.fees.register.api.controllers.rangegroups.RangeGroupsDtoMapper;
import uk.gov.hmcts.fees.register.api.model.Fee;
import uk.gov.hmcts.fees.register.api.model.FixedFee;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.fees.register.api.contract.RangeDto.rangeDtoWith;
import static uk.gov.hmcts.fees.register.api.contract.RangeGroupDto.rangeGroupDtoWith;
import static uk.gov.hmcts.fees.register.api.model.Range.rangeWith;
import static uk.gov.hmcts.fees.register.api.model.RangeGroup.rangeGroupWith;

public class RangeGroupsDtoMapperTest {

    private static final Fee ANY_FEE = new FixedFee();
    private static final FeeDto MAPPED_FEE_DTO = new FeeDto("ANY", "ANY");
    private RangeGroupsDtoMapper rangeGroupsDtoMapper = new RangeGroupsDtoMapper(new FeesDtoMapper() {
        @Override
        public FeeDto toFeeDto(Fee fee) {
            return MAPPED_FEE_DTO;
        }
    });

    @Test
    public void convertsToRangeGroupDto() {
        assertThat(rangeGroupsDtoMapper.toRangeGroupDto(
            rangeGroupWith()
                .id(9)
                .description("range description")
                .ranges(Arrays.asList(
                    rangeWith().rangeGroupId(123).from(0).to(1000).fee(ANY_FEE).build(),
                    rangeWith().rangeGroupId(456).from(1001).fee(ANY_FEE).build())
                )
                .build())
        ).isEqualTo(
            rangeGroupDtoWith()
                .id(9)
                .description("range description")
                .ranges(Arrays.asList(
                    rangeDtoWith().from(0).to(1000).fee(MAPPED_FEE_DTO).build(),
                    rangeDtoWith().from(1001).fee(MAPPED_FEE_DTO).build())
                )
                .build());
    }

    @Test
    public void nullConvertsToNull() {
        assertThat(rangeGroupsDtoMapper.toRangeGroupDto(null)).isNull();
    }
}
