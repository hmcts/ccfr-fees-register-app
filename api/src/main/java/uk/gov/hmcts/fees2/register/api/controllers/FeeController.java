package uk.gov.hmcts.fees2.register.api.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.request.ApproveFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.RangedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;
import uk.gov.hmcts.fees2.register.data.service.FeeService;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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
    public void createRangedFee(@RequestBody final RangedFeeDto request, HttpServletResponse response) {
        Fee fee = feeService.save(feeDtoMapper.toFee(request));
        response.setHeader("Location", "/fee/" + fee.getCode());
    }

    @GetMapping("/fee/{code}")
    public Fee2Dto getFee(@PathVariable("code") String code) {
        Fee fee = feeService.get(code);
        return feeDtoMapper.toFeeDto(fee);
    }

    @GetMapping("/fees/search")
    public List<Fee2Dto> search(@RequestParam String service,
                                @RequestParam String jurisdiction1,
                                @RequestParam String jurisdiction2,
                                @RequestParam String channel,
                                @RequestParam String event,
                                @RequestParam String direction,
                                @RequestParam BigDecimal amount) {
        return feeService
            .search(new LookupFeeDto(service, jurisdiction1, jurisdiction2, channel, event, direction, amount))
            .stream()
            .map(feeDtoMapper::toFeeDto)
            .collect(Collectors.toList());
    }

    @GetMapping("/fees/lookup")
    public FeeLookupResponseDto lookup(@RequestParam String service,
                                       @RequestParam String jurisdiction1,
                                       @RequestParam String jurisdiction2,
                                       @RequestParam String channel,
                                       @RequestParam String event,
                                       @RequestParam String direction,
                                       @RequestParam BigDecimal amount) {

        Fee fee = feeService.lookup(new LookupFeeDto(service, jurisdiction1, jurisdiction2, channel, event, direction, amount));

        FeeVersion version = fee.getCurrentVersion();

        return new FeeLookupResponseDto(fee.getCode(), version.getVersion(), version.calculateFee(amount));
    }


    @PatchMapping("/fees/approve")
    public void approve(@RequestBody ApproveFeeDto dto) {
        feeService.approve(dto.getFeeCode(), dto.getFeeVersion());
    }

}
