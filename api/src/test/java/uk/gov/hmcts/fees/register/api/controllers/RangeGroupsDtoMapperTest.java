package uk.gov.hmcts.fees.register.api.controllers;

import org.junit.Test;
import uk.gov.hmcts.fees.register.api.contract.FeeDto;
import uk.gov.hmcts.fees.register.api.controllers.fees.FeesDtoMapper;
import uk.gov.hmcts.fees.register.api.controllers.rangegroups.RangeGroupsDtoMapper;
import uk.gov.hmcts.fees.register.api.model.FeeOld;
import uk.gov.hmcts.fees.register.api.model.FixedFeeOld;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.fees.register.api.contract.RangeGroupDto.RangeDto.rangeDtoWith;
import static uk.gov.hmcts.fees.register.api.contract.RangeGroupDto.rangeGroupDtoWith;
import static uk.gov.hmcts.fees.register.api.model.Range.rangeWith;
import static uk.gov.hmcts.fees.register.api.model.RangeGroup.rangeGroupWith;

public class RangeGroupsDtoMapperTest {

    private static final FeeOld ANY_FEE = new FixedFeeOld();
    private static final FeeDto MAPPED_FEE_DTO = new FeeDto("ANY", "ANY");
    private RangeGroupsDtoMapper rangeGroupsDtoMapper = new RangeGroupsDtoMapper(new FeesDtoMapper() {
        @Override
        public FeeDto toFeeDto(FeeOld fee) {
            return MAPPED_FEE_DTO;
        }
    }, null);

    @Test
    public void convertsToRangeGroupDto() {
        assertThat(rangeGroupsDtoMapper.toRangeGroupDto(
            rangeGroupWith()
                .id(9)
                .code("range code")
                .description("range description")
                .ranges(Arrays.asList(
                    rangeWith().from(0).to(1000).fee(ANY_FEE).build(),
                    rangeWith().from(1001).fee(ANY_FEE).build())
                )
                .build())
        ).isEqualTo(
            rangeGroupDtoWith()
                .code("range code")
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
