package uk.gov.hmcts.fees2.register.api.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.service.FeeService;

import java.util.List;

@RestController
@Validated
public class LookupController {

    private FeeService feeService;
    private FeeDtoMapper feeDtoMapper;

    @Autowired
    public LookupController(FeeService feeService) {
        this.feeService = feeService;
    }

    @GetMapping("/fee/lookup")
    @ResponseStatus(HttpStatus.OK)
    public List<Fee> lookup(@RequestBody final LookupFeeDto request){
        return feeService.lookup(request);
    }

}
