package uk.gov.hmcts.fees.register.api.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.fees.register.api.contract.FeeDto;
import uk.gov.hmcts.fees.register.api.model.Fee;
import uk.gov.hmcts.fees.register.api.model.FeeRepository;
import uk.gov.hmcts.fees.register.legacymodel.EntityNotFoundException;

import static java.util.stream.Collectors.toList;

@RestController()
@RequestMapping("/fees-register")
public class FeesRegisterController {

    private final FeeDtoMapper feeDtoMapper;
    private final FeeRepository feeRepository;

    @Autowired
    public FeesRegisterController(FeeDtoMapper feeDtoMapper, FeeRepository feeRepository) {
        this.feeDtoMapper = feeDtoMapper;
        this.feeRepository = feeRepository;
    }

    @GetMapping("/fees")
    public List<FeeDto> getFees() {
        return feeRepository.findAll().stream().map(feeDtoMapper::toFeeDto).collect(toList());
    }

    @GetMapping("/fees/{id}")
    public FeeDto getFee(@PathVariable("id") Integer id) {
        Fee fee = feeRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Fee not found. Id: " + id));

        return feeDtoMapper.toFeeDto(fee);
    }

}
