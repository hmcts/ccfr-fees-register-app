package uk.gov.hmcts.fees2.register.api.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.request.ApproveFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.RangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.service.FeeService;

@RestController
@Validated
public class FeeController {

    private final FeeService feeService;
    private final FeeDtoMapper feeDtoMapper;

    @Autowired
    public FeeController(FeeService feeService, FeeDtoMapper feeDtoMapper) {
        this.feeService = feeService;
        this.feeDtoMapper = feeDtoMapper;
    }

    @PostMapping("/rangedfees")
    @ResponseStatus(HttpStatus.CREATED)
    public void createRangedFee(@RequestBody final RangedFeeDto request){
        feeService.save(feeDtoMapper.toFee(request));
    }

    @GetMapping("/fee/{code}")
    public Fee2Dto getFee(@PathVariable("code") String code) {
        Fee fee = feeService.get(code);
        return feeDtoMapper.toFeeDto(fee);
    }

    @PatchMapping("/fees/approve")
    public void approve(@RequestBody ApproveFeeDto dto){
        feeService.approve(dto.getFeeCode(), dto.getFeeVersion());
    }

}
