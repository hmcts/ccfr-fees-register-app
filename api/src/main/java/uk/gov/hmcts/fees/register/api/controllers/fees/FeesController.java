package uk.gov.hmcts.fees.register.api.controllers.fees;

import java.util.List;
import javax.validation.Valid;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.fees.register.api.contract.FeeDto;
import uk.gov.hmcts.fees.register.api.model.Fee;
import uk.gov.hmcts.fees.register.api.model.FeeRepository;
import uk.gov.hmcts.fees.register.legacymodel.EntityNotFoundException;

import static java.util.stream.Collectors.toList;

import static org.springframework.beans.BeanUtils.copyProperties;

@RestController
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
        Fee fee = findByCode(code);

        return feesDtoMapper.toFeeDto(fee);
    }

    @PutMapping("/fees/{code}")
    public FeeDto updateFee(@NotEmpty @PathVariable("code") String code, @Valid @RequestBody FeeDto feeDto) {
        Fee newFeeModel = feesDtoMapper.toFee(code, feeDto);
        Fee existingFee = findByCode(code);

        if (!existingFee.getClass().equals(newFeeModel.getClass())) {
            throw new FeeTypeUnchangeableException();
        }

        copyProperties(newFeeModel, existingFee, "id");

        feeRepository.save(existingFee);

        return feesDtoMapper.toFeeDto(existingFee);
    }

    private Fee findByCode(@NotEmpty @PathVariable("code") String code) {
        return feeRepository
            .findByCode(code)
            .orElseThrow(() -> new EntityNotFoundException("Fee not found. Code: " + code));
    }

}
