package uk.gov.hmcts.fees.register.api.controllers.rangegroups;

import java.util.List;
import javax.validation.Valid;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
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
import uk.gov.hmcts.fees.register.api.model.RangeEmptyException;
import uk.gov.hmcts.fees.register.api.model.RangeGroup;
import uk.gov.hmcts.fees.register.api.model.RangeGroupNotContinuousException;
import uk.gov.hmcts.fees.register.api.model.RangeGroupRepository;
import uk.gov.hmcts.fees.register.api.model.exceptions.RangeGroupNotFoundException;
import uk.gov.hmcts.fees.register.api.model.exceptions.ResourceNotFoundException;
import uk.gov.hmcts.fees.register.api.model.exceptions.FeeNotFoundException;

import static java.util.stream.Collectors.toList;

import static org.springframework.beans.BeanUtils.copyProperties;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@Validated
public class RangeGroupsController {

    private final RangeGroupsDtoMapper rangeGroupsDtoMapper;
    private final RangeGroupRepository rangeGroupRepository;

    @Autowired
    public RangeGroupsController(RangeGroupsDtoMapper rangeGroupsDtoMapper, RangeGroupRepository rangeGroupRepository) {
        this.rangeGroupsDtoMapper = rangeGroupsDtoMapper;
        this.rangeGroupRepository = rangeGroupRepository;
    }

    @GetMapping("/range-groups")
    public List<RangeGroupDto> getRangeGroups() {
        return rangeGroupRepository.findAll().stream().map(rangeGroupsDtoMapper::toRangeGroupDto).collect(toList());
    }

    @GetMapping("/range-groups/{code}")
    public RangeGroupDto getRangeGroup(@PathVariable("code") String code) {
        RangeGroup rangeGroup = findByCode(code);
        return rangeGroupsDtoMapper.toRangeGroupDto(rangeGroup);
    }

    @PutMapping("/range-groups/{code}")
    public RangeGroupDto updateRangeGroup(@Length(max = 50) @PathVariable("code") String code,
                                          @Valid @RequestBody RangeGroupUpdateDto rangeGroupDto) {
        RangeGroup newRangeGroupModel = rangeGroupsDtoMapper.toRangeGroup(code, rangeGroupDto);
        RangeGroup existingRangeGroup = findByCode(code);

        copyProperties(newRangeGroupModel, existingRangeGroup, "id");

        rangeGroupRepository.save(existingRangeGroup);

        return rangeGroupsDtoMapper.toRangeGroupDto(existingRangeGroup);
    }

    @GetMapping("/range-groups/{code}/calculations")
    public CalculationDto getCategoryRange(@PathVariable("code") String code,
                                           @RequestParam("value") int value) {
        RangeGroup rangeGroup = findByCode(code);
        int amount = rangeGroup.calculate(value);
        return new CalculationDto(amount);
    }

    private RangeGroup findByCode(@PathVariable("code") String code) {
        return rangeGroupRepository
            .findByCode(code)
            .orElseThrow(() -> new RangeGroupNotFoundException(code));
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
        return new ResponseEntity<>(new ErrorDto("ranges: provides set of ranges contains gaps or overlaps"), BAD_REQUEST);
    }
}
