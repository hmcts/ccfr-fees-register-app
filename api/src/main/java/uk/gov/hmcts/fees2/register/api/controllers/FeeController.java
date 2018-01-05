package uk.gov.hmcts.fees2.register.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.fees.register.api.model.exceptions.ResourceNotFoundException;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.request.*;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.exceptions.BadRequestException;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.service.FeeService;
import uk.gov.hmcts.fees2.register.util.URIUtils;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Api(value = "FeesRegister", description = "Operations pertaining to fees")
@RestController
@Validated
@RequestMapping("/fees-register")
public class FeeController {
    private static final Logger LOG = LoggerFactory.getLogger(FeeController.class);

    public static final String LOCATION = "Location";

    private final FeeService feeService;

    private final FeeDtoMapper feeDtoMapper;

    @Autowired
    public FeeController(FeeService feeService, FeeDtoMapper feeDtoMapper) {
        this.feeService = feeService;
        this.feeDtoMapper = feeDtoMapper;
    }

    @ApiOperation(value = "Create ranged fee")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized, invalid user IDAM token"),
        @ApiResponse(code = 403, message = "Forbidden")
    })
    @PostMapping("/rangedfees")
    @ResponseStatus(HttpStatus.CREATED)
    public void createRangedFee(@RequestBody @Validated final CreateRangedFeeDto request, HttpServletResponse response) {
        Fee fee = feeService.save(feeDtoMapper.toFee(request));

        if (response != null) {
            response.setHeader(LOCATION, getResourceLocation(fee));
        }
    }

    @ApiOperation(value = "Create fixed fee")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized, invalid user IDAM token"),
        @ApiResponse(code = 403, message = "Forbidden")
    })
    @PostMapping(value = "/fixedfees")
    @ResponseStatus(HttpStatus.CREATED)
    public void createFixedFee(@RequestBody @Validated final CreateFixedFeeDto request, HttpServletResponse response) {
        Fee fee = feeService.save(feeDtoMapper.toFee(request));

        if (response != null) {
            response.setHeader(LOCATION, getResourceLocation(fee));
        }
    }

    @ApiOperation(value = "Create bulk fees")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized, invalid user IDAM token"),
        @ApiResponse(code = 403, message = "Forbidden")
    })
    @Transactional
    @PostMapping(value = "/bulkfixedfees")
    @ResponseStatus(HttpStatus.CREATED)
    public void createFixedFees(@RequestBody final List<CreateFixedFeeDto> createFixedFeeDtos) {
        LOG.info("No. of csv import fees: " + createFixedFeeDtos.size());

        List<Fee> fixedFees = createFixedFeeDtos.stream().map(fixedFeeDto -> feeDtoMapper.toFee(fixedFeeDto)).collect(Collectors.toList());

        feeService.save(fixedFees);
    }

    @ApiOperation(value = "Get a fee for the given fee code", response = Fee2Dto.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Found"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Not Found")
    })
    @GetMapping("/fees/{code}")
    @ResponseStatus(HttpStatus.OK)
    public Fee2Dto getFee(@PathVariable("code") String code) {
        Fee fee = feeService.get(code);
        return feeDtoMapper.toFeeDto(fee);
    }

    @RequestMapping(method = RequestMethod.HEAD, path = "/fees/{code}")
    @ResponseStatus(HttpStatus.OK)
    public void doesFeeExist(@PathVariable("code") String code) {
        feeService.get(code);
    }

    @ApiOperation(value = "Delete a fee for the given fee code")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Successfully deleted the fee for the given fee code."),
    })
    @DeleteMapping("/fees/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFee(@PathVariable("code") String code) {
        feeService.delete(code);
    }

    @ApiOperation(value = "Search for fees based on criteria")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Found"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Not found")
    })
    @GetMapping("/fees")
    @ResponseStatus(HttpStatus.OK)
    public List<Fee2Dto> search(@RequestParam(required = false) String service,
                                @RequestParam(required = false) String jurisdiction1,
                                @RequestParam(required = false) String jurisdiction2,
                                @RequestParam(required = false) String channel,
                                @RequestParam(required = false) String event,
                                @RequestParam(required = false) String direction,
                                @RequestParam(required = false) BigDecimal amount,
                                @RequestParam(required = false) Boolean unspecifiedClaimAmounts,
                                @RequestParam(required = false) FeeVersionStatus feeVersionStatus) {

        if(feeVersionStatus != null && feeVersionStatus.equals(FeeVersionStatus.draft)) { /* Limited for now to required functionality */
            return feeService.getUnapprovedVersions().stream().map(feeDtoMapper::toFeeDto).collect(Collectors.toList());
        }

        return feeService
            .search(new LookupFeeDto(service, jurisdiction1, jurisdiction2, channel, event, direction, amount, unspecifiedClaimAmounts, feeVersionStatus /* Not used for now*/))
            .stream()
            .map(feeDtoMapper::toFeeDto)
            .collect(Collectors.toList());
    }

    @ApiOperation(value = "Fee lookup based on reference data and amount", response = FeeLookupResponseDto.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Found"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Not found")
    })
    @GetMapping("/lookup")
    @ResponseStatus(HttpStatus.OK)
    public FeeLookupResponseDto lookup(@RequestParam String service,
                                       @RequestParam String jurisdiction1,
                                       @RequestParam String jurisdiction2,
                                       @RequestParam(required = false) String channel,
                                       @RequestParam String event,
                                       @RequestParam(required = false, name = "amount_or_volume") BigDecimal amountOrVolume) {

        if (amountOrVolume != null && amountOrVolume.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Amount or volume should be greater than or equal to zero.");
        }

        return feeService.lookup(new LookupFeeDto(service, jurisdiction1, jurisdiction2, channel, event, null, amountOrVolume, false, FeeVersionStatus.approved));
    }

    @ApiOperation(value = "Lookup for unspecified fee based on reference data", response = FeeLookupResponseDto.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Found"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Not found")
    })
    @GetMapping("/lookup/unspecified")
    @ResponseStatus(HttpStatus.OK)
    public FeeLookupResponseDto lookupUnspecified(@RequestParam String service,
                                                  @RequestParam String jurisdiction1,
                                                  @RequestParam String jurisdiction2,
                                                  @RequestParam(required = false) String channel,
                                                  @RequestParam String event) {

        return feeService.lookup(new LookupFeeDto(service, jurisdiction1, jurisdiction2, channel, event, null, null, true, FeeVersionStatus.approved));
    }

    @ApiOperation(value = "Approve a draft fee")
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "No content")
    })
    @PatchMapping("/fees/approve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void approve(@RequestBody @Validated ApproveFeeDto dto) {
        feeService.approve(dto.getFeeCode(), dto.getFeeVersion());
    }

    /* --- */

    private String getResourceLocation(Fee fee) {
        return URIUtils.getUrlForGetMethod(this.getClass(), "getFee").replace("{code}", fee.getCode());
    }

}
