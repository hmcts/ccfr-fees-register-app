package uk.gov.hmcts.fees.register.api.controllers.rangegroups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees.register.api.contract.RangeDto;
import uk.gov.hmcts.fees.register.api.contract.RangeGroupDto;
import uk.gov.hmcts.fees.register.api.controllers.fees.FeesDtoMapper;
import uk.gov.hmcts.fees.register.api.model.Range;
import uk.gov.hmcts.fees.register.api.model.RangeGroup;

import static java.util.stream.Collectors.toList;

@Component
public class RangeGroupsDtoMapper {

    private final FeesDtoMapper feesDtoMapper;

    @Autowired
    public RangeGroupsDtoMapper(FeesDtoMapper feesDtoMapper) {
        this.feesDtoMapper = feesDtoMapper;
    }

    public RangeGroupDto toRangeGroupDto(RangeGroup rangeGroup) {
        if (rangeGroup == null) {
            return null;
        }

        return RangeGroupDto.rangeGroupDtoWith()
            .code(rangeGroup.getCode())
            .description(rangeGroup.getDescription())
            .ranges(rangeGroup.getRanges().stream().map(this::toRangeDto).collect(toList()))
            .build();
    }

    private RangeDto toRangeDto(Range range) {
        return RangeDto.rangeDtoWith()
            .from(range.getFrom())
            .to(range.getTo())
            .fee(feesDtoMapper.toFeeDto(range.getFee()))
            .build();
    }
}
