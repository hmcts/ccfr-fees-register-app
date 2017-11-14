package uk.gov.hmcts.fees2.register.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.request.ApproveFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateFixedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateRangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.service.FeeService;
import uk.gov.hmcts.fees2.register.util.URIUtils;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/fees-register")
public class FeeController {

    public static final String LOCATION = "Location";

    private final FeeService feeService;

    private final FeeDtoMapper feeDtoMapper;

    @Autowired
    public FeeController(FeeService feeService, FeeDtoMapper feeDtoMapper) {
        this.feeService = feeService;
        this.feeDtoMapper = feeDtoMapper;
    }

    @PostMapping("/rangedfees")
    @ResponseStatus(HttpStatus.CREATED)
    public void createRangedFee(@RequestBody final CreateRangedFeeDto request, HttpServletResponse response) {
        Fee fee = feeService.save(feeDtoMapper.toFee(request));

        if (response != null) {
            response.setHeader(LOCATION, getResourceLocation(fee));
        }
    }

    @PostMapping("/fixedfees")
    @ResponseStatus(HttpStatus.CREATED)
    public void createFixedFee(@RequestBody final CreateFixedFeeDto request, HttpServletResponse response) {
        Fee fee = feeService.save(feeDtoMapper.toFee(request));

        if (response != null) {
            response.setHeader(LOCATION, getResourceLocation(fee));
        }
    }

    @GetMapping("/fees/{code}")
    public Fee2Dto getFee(@PathVariable("code") String code) {
        Fee fee = feeService.get(code);
        return feeDtoMapper.toFeeDto(fee);
    }

    @DeleteMapping("/fees/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFee(@PathVariable("code") String code) {
        feeService.delete(code);
    }

    @GetMapping("/fees")
    public List<Fee2Dto> search(@RequestParam(required = false) String service,
                                @RequestParam(required = false) String jurisdiction1,
                                @RequestParam(required = false) String jurisdiction2,
                                @RequestParam(required = false) String channel,
                                @RequestParam(required = false) String event,
                                @RequestParam(required = false) String direction,
                                @RequestParam(required = false) BigDecimal amount,
                                @RequestParam(required = false) Boolean unspecifiedClaimAmounts) {
        return feeService
            .search(new LookupFeeDto(service, jurisdiction1, jurisdiction2, channel, event, direction, amount, unspecifiedClaimAmounts))
            .stream()
            .map(feeDtoMapper::toFeeDto)
            .collect(Collectors.toList());
    }

    @GetMapping("/lookup")
    public FeeLookupResponseDto lookup(@RequestParam String service,
                                       @RequestParam String jurisdiction1,
                                       @RequestParam String jurisdiction2,
                                       @RequestParam(required = false) String channel,
                                       @RequestParam String event,
                                       @RequestParam(required = false) BigDecimal amount) {

        return feeService.lookup(new LookupFeeDto(service, jurisdiction1, jurisdiction2, channel, event, null, amount, false));
    }

    @GetMapping("/lookup/unspecified")
    public FeeLookupResponseDto lookupUnspecified(@RequestParam String service,
                                                  @RequestParam String jurisdiction1,
                                                  @RequestParam String jurisdiction2,
                                                  @RequestParam(required = false) String channel,
                                                  @RequestParam String event) {

        return feeService.lookup(new LookupFeeDto(service, jurisdiction1, jurisdiction2, channel, event, null, null, true));
    }


    @PatchMapping("/fees/approve")
    public void approve(@RequestBody ApproveFeeDto dto) {
        feeService.approve(dto.getFeeCode(), dto.getFeeVersion());
    }

    /* --- */

    private String getResourceLocation(Fee fee) {
        return URIUtils.getUrlForGetMethod(this.getClass(), "getFee").replace("{code}", fee.getCode());
    }

}
