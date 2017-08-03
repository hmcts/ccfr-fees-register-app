package uk.gov.hmcts.fees.register.api.controllers.rangegroups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees.register.api.contract.RangeGroupDto;
import uk.gov.hmcts.fees.register.api.contract.RangeGroupDto.RangeDto;
import uk.gov.hmcts.fees.register.api.contract.RangeGroupUpdateDto;
import uk.gov.hmcts.fees.register.api.controllers.fees.FeesDtoMapper;
import uk.gov.hmcts.fees.register.api.model.FeeRepository;
import uk.gov.hmcts.fees.register.api.model.Range;
import uk.gov.hmcts.fees.register.api.model.RangeGroup;

import static java.util.stream.Collectors.toList;

@Component
public class RangeGroupsDtoMapper {

    private final FeesDtoMapper feesDtoMapper;
    private final FeeRepository feeRepository;

    @Autowired
    public RangeGroupsDtoMapper(FeesDtoMapper feesDtoMapper, FeeRepository feeRepository) {
        this.feesDtoMapper = feesDtoMapper;
        this.feeRepository = feeRepository;
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

    public RangeGroup toRangeGroup(String code, RangeGroupUpdateDto dto) {
        return RangeGroup.rangeGroupWith()
            .code(code)
            .description(dto.getDescription())
            .ranges(dto.getRanges().stream().map(this::toRange).collect(toList()))
            .build();
    }

    private Range toRange(RangeGroupUpdateDto.RangeUpdateDto dto) {
        return Range.rangeWith()
            .from(dto.getFrom())
            .to(dto.getTo())
            .fee(feeRepository.findByCodeOrThrow(dto.getFeeCode()))
            .build();
    }
}
