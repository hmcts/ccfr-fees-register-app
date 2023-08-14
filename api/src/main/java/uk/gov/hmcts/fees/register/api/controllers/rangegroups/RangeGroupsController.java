package uk.gov.hmcts.fees.register.api.controllers.rangegroups;

import io.swagger.v3.oas.annotations.Operation;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.fees.register.api.contract.CalculationDto;
import uk.gov.hmcts.fees.register.api.contract.ErrorDto;
import uk.gov.hmcts.fees.register.api.contract.RangeGroupDto;
import uk.gov.hmcts.fees.register.api.contract.RangeGroupUpdateDto;
import uk.gov.hmcts.fees.register.api.controllers.fees.FeesDtoMapper;
import uk.gov.hmcts.fees.register.api.model.*;
import uk.gov.hmcts.fees.register.api.model.exceptions.FeeNotFoundException;

import javax.validation.Valid;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.beans.BeanUtils.copyProperties;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@Validated
public class RangeGroupsController {

    private final FeesDtoMapper feesDtoMapper;
    private final RangeGroupsDtoMapper rangeGroupsDtoMapper;
    private final RangeGroupRepository rangeGroupRepository;

    /** Constant for rangeGroupCode cmc-paper */
    private static final String CMC_PAPER = "cmc-paper";

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

    @Operation(summary = "Find appropriate fees amount for given claim. The endpoint returns the fee for specified amount")
    @GetMapping("/range-groups/{code}/calculations")
    public CalculationDto getCategoryRange(@PathVariable("code") String code, @RequestParam(value = "value") int value) {
        RangeGroup rangeGroup = rangeGroupRepository.findByCodeOrThrow(code);
        FeeOld fee = rangeGroup.findFeeForValue(value);

        return new CalculationDto(fee.calculate(value), feesDtoMapper.toFeeDto(fee));
    }

    @Operation(summary = "Find max fees amount for an unspecified value. The endpoint returns the max fee for the unspecified amount")
    @GetMapping("/range-groups/cmc-paper/calculations/unspecified")
    public CalculationDto getMaxFeeForUnspecifiedRange() {
        RangeGroup rangeGroup = rangeGroupRepository.findByCodeOrThrow(CMC_PAPER);
        FeeOld fee = rangeGroup.findFeeForValue(rangeGroup.findMaxRangeValue());

        return new CalculationDto(fee.calculate(0), feesDtoMapper.toFeeDto(fee));
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
