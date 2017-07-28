package uk.gov.hmcts.fees.register.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.fees.register.api.contract.FeeDto;
import uk.gov.hmcts.fees.register.api.model.Fee;
import uk.gov.hmcts.fees.register.api.model.FeeRepository;
import uk.gov.hmcts.fees.register.legacymodel.EntityNotFoundException;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController()
@RequestMapping()
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

    @GetMapping("/fees/{id}")
    public FeeDto getFee(@PathVariable("id") Integer id) {
        Fee fee = feeRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Fee not found. Id: " + id));

        return feesDtoMapper.toFeeDto(fee);
    }

}
