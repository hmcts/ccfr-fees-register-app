package uk.gov.hmcts.fees.register.api.controllers.rangegroups;

import java.util.List;
import javax.validation.Valid;

import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.fees.register.api.contract.CalculationDto;
import uk.gov.hmcts.fees.register.api.contract.ErrorDto;
import uk.gov.hmcts.fees.register.api.contract.RangeGroupDto;
import uk.gov.hmcts.fees.register.api.contract.RangeGroupUpdateDto;
import uk.gov.hmcts.fees.register.api.controllers.fees.FeesDtoMapper;
import uk.gov.hmcts.fees.register.api.model.Fee;
import uk.gov.hmcts.fees.register.api.model.RangeEmptyException;
import uk.gov.hmcts.fees.register.api.model.RangeGroup;
import uk.gov.hmcts.fees.register.api.model.RangeGroupNotContinuousException;
import uk.gov.hmcts.fees.register.api.model.RangeGroupRepository;
import uk.gov.hmcts.fees.register.api.model.exceptions.FeeNotFoundException;

import static java.util.stream.Collectors.toList;

import static org.springframework.beans.BeanUtils.copyProperties;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@Validated
public class RangeGroupsController {

    private final FeesDtoMapper feesDtoMapper;
    private final RangeGroupsDtoMapper rangeGroupsDtoMapper;
    private final RangeGroupRepository rangeGroupRepository;

    @Autowired
    public RangeGroupsController(FeesDtoMapper feesDtoMapper, RangeGroupsDtoMapper rangeGroupsDtoMapper, RangeGroupRepository rangeGroupRepository) {
        this.feesDtoMapper = feesDtoMapper;
        this.rangeGroupsDtoMapper = rangeGroupsDtoMapper;
        this.rangeGroupRepository = rangeGroupRepository;
    }

    @GetMapping("/range-groups")
    public List<RangeGroupDto> getRangeGroups() {
        return rangeGroupRepository.findAll().stream().map(rangeGroupsDtoMapper::toRangeGroupDto).collect(toList());
    }

    @GetMapping("/range-groups/{code}")
    public RangeGroupDto getRangeGroup(@PathVariable("code") String code) {
        RangeGroup rangeGroup = rangeGroupRepository.findByCodeOrThrow(code);
        return rangeGroupsDtoMapper.toRangeGroupDto(rangeGroup);
    }

    @PutMapping("/range-groups/{code}")
    public RangeGroupDto createOrUpdateRangeGroup(@Length(max = 50) @PathVariable("code") String code,
                                                  @Valid @RequestBody RangeGroupUpdateDto rangeGroupDto) {
        RangeGroup newRangeGroupModel = rangeGroupsDtoMapper.toRangeGroup(code, rangeGroupDto);
        RangeGroup existingRangeGroup = rangeGroupRepository.findByCode(code).orElse(new RangeGroup());

        copyProperties(newRangeGroupModel, existingRangeGroup, "id");

        rangeGroupRepository.save(existingRangeGroup);

        return rangeGroupsDtoMapper.toRangeGroupDto(existingRangeGroup);
    }

    @ApiOperation(value = "Find appropriate fees amount for given claim.",
                    notes="The endpoint returns the fee for specified amount and max fee/percentage for the unclaimed amount", response = CalculationDto.class)
    @GetMapping("/range-groups/{code}/calculations")
    public CalculationDto getCategoryRange(@PathVariable("code") String code,
                                           @RequestParam(value = "value", required = false, defaultValue = "0") int value) {
        RangeGroup rangeGroup = rangeGroupRepository.findByCodeOrThrow(code);
        Fee fee = rangeGroup.findFeeForValue(value > 0 ? value : rangeGroup.findMaxRangeValue());

        return new CalculationDto(fee.calculate(value), feesDtoMapper.toFeeDto(fee));
    }

    @ExceptionHandler(FeeNotFoundException.class)
    public ResponseEntity rangeFeeNotFound() {
        return new ResponseEntity<>(new ErrorDto("ranges: one of the ranges contains unknown fee code"), BAD_REQUEST);
    }

    @ExceptionHandler(RangeEmptyException.class)
    public ResponseEntity rangeEmpty() {
        return new ResponseEntity<>(new ErrorDto("ranges: one of the ranges is empty"), BAD_REQUEST);
    }

    @ExceptionHandler(RangeGroupNotContinuousException.class)
    public ResponseEntity rangeGroupNotContinuous() {
        return new ResponseEntity<>(new ErrorDto("ranges: provided set of ranges contains gaps or overlaps"), BAD_REQUEST);
    }
}
