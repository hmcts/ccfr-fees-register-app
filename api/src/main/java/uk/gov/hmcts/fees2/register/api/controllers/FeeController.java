package uk.gov.hmcts.fees2.register.api.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.fees2.register.api.contract.request.RangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.service.FeeService;

@RestController
@Validated
public class FeeController {

    private FeeService feeService;
    private FeeDtoMapper feeDtoMapper;

    @Autowired
    public FeeController(FeeService feeService, FeeDtoMapper feeDtoMapper) {
        this.feeService = feeService;
        this.feeDtoMapper = feeDtoMapper;
    }

    @PostMapping("/fee")
    @ResponseStatus(HttpStatus.CREATED)
    public void createFee(@RequestBody final RangedFeeDto request){

        feeService.save(feeDtoMapper.toFee(request));

    }

}
