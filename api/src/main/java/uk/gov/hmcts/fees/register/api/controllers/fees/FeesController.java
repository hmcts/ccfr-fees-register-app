package uk.gov.hmcts.fees.register.api.controllers.fees;

import java.util.List;
import javax.validation.Valid;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
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
import uk.gov.hmcts.fees.register.api.contract.FeeDto;
import uk.gov.hmcts.fees.register.api.model.Fee;
import uk.gov.hmcts.fees.register.api.model.FeeRepository;

import static java.util.stream.Collectors.toList;

import static org.springframework.beans.BeanUtils.copyProperties;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@Validated
public class FeesController {

    private final FeesDtoMapper feesDtoMapper;
    private final FeeRepository feeRepository;

    @Autowired
    public FeesController(FeesDtoMapper feesDtoMapper, FeeRepository feeRepository) {
        this.feesDtoMapper = feesDtoMapper;
        this.feeRepository = feeRepository;
    }

    @GetMapping("/fees")
    public List<FeeDto> getFees() {
        return feeRepository.findAll().stream().map(feesDtoMapper::toFeeDto).collect(toList());
    }

    @GetMapping("/fees/{code}")
    public FeeDto getFee(@NotEmpty @PathVariable("code") String code) {
        Fee fee = feeRepository.findByCodeOrThrow(code);
        return feesDtoMapper.toFeeDto(fee);
    }

    @PutMapping("/fees/{code}")
    public FeeDto updateFee(@Length(max = 50) @PathVariable("code") String code,
                            @Valid @RequestBody FeeDto feeDto) {
        Fee newFeeModel = feesDtoMapper.toFee(code, feeDto);
        Fee existingFee = feeRepository.findByCodeOrThrow(code);

        if (!existingFee.getClass().equals(newFeeModel.getClass())) {
            throw new FeeTypeUnchangeableException();
        }

        copyProperties(newFeeModel, existingFee, "id");

        feeRepository.save(existingFee);

        return feesDtoMapper.toFeeDto(existingFee);
    }

    @GetMapping("/fees/{code}/calculations")
    public CalculationDto getCategoryRange(@PathVariable("code") String code,
                                           @RequestParam("value") int value) {
        Fee fee = feeRepository.findByCodeOrThrow(code);
        int amount = fee.calculate(value);
        return new CalculationDto(amount);
    }

    @ExceptionHandler(FeeTypeUnchangeableException.class)
    public ResponseEntity<ErrorDto> feeTypeUnchangeableException() {
        return new ResponseEntity<>(new ErrorDto("Fee type can't be changed"), BAD_REQUEST);
    }
}
