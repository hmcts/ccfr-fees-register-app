package uk.gov.hmcts.fees2.register.api.controllers;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateFixedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.CreateRangedFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.exceptions.BadRequestException;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.service.FeeService;
import uk.gov.hmcts.fees2.register.data.service.FeeVersionService;
import uk.gov.hmcts.fees2.register.util.URIUtils;

import javax.annotation.RegEx;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "FeesRegister", description = "Operations pertaining to fees")
@RestController
@RequestMapping(value = "/fees-register")

@Validated
public class FeeController {
    private static final Logger LOG = LoggerFactory.getLogger(FeeController.class);

    public static final String LOCATION = "Location";

    private final FeeService feeService;

    private final FeeVersionService feeVersionService;

    private final FeeDtoMapper feeDtoMapper;

    @Autowired
    public FeeController(FeeService feeService, FeeVersionService feeVersionService, FeeDtoMapper feeDtoMapper) {
        this.feeService = feeService;
        this.feeVersionService = feeVersionService;
        this.feeDtoMapper = feeDtoMapper;
    }

    @ApiOperation(value = "Create ranged fee")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized, invalid user IDAM token"),
        @ApiResponse(code = 403, message = "Forbidden")
    })
    @PostMapping("/ranged-fees")
    @ResponseStatus(HttpStatus.CREATED)
    public void createRangedFee(
        @RequestBody @Validated final CreateRangedFeeDto request,
        HttpServletResponse response,
        Principal principal) {
        Fee fee = feeService.save(
            feeDtoMapper.toFee(request, principal != null ? principal.getName() : null));

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
    @PostMapping(value = "/fixed-fees")
    @ResponseStatus(HttpStatus.CREATED)
    public void createFixedFee(@RequestBody @Validated final CreateFixedFeeDto request,
                               HttpServletResponse response,
                               Principal principal) {

        Fee fee = feeDtoMapper.toFee(request, principal != null ? principal.getName() : null);

        fee = feeService.save(fee);

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
    @PostMapping(value = "/bulk-fixed-fees")
    @ResponseStatus(HttpStatus.CREATED)
    public void createFixedFees(@RequestBody final List<CreateFixedFeeDto> createFixedFeeDtos, Principal principal) {
        LOG.info("No. of csv import fees: " + createFixedFeeDtos.size());

        List<Fee> fixedFees = createFixedFeeDtos
            .stream()
            .map(fixedFeeDto -> feeDtoMapper.toFee(fixedFeeDto, principal != null ? principal.getName() : null))
            .collect(Collectors.toList());

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
    @Transactional
    public Fee2Dto getFee(@PathVariable("code") String code, HttpServletResponse response) {
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
                                @RequestParam(required = false, name = "applicant_type") String applicantType,
                                @RequestParam(required = false) BigDecimal amount,
                                @RequestParam(required = false) Boolean unspecifiedClaimAmounts,
                                @RequestParam(required = false) FeeVersionStatus feeVersionStatus,
                                @RequestParam(required = false) String author,
                                                                HttpServletResponse response) {
        /* These are provisional hacks, in reality we need to lookup versions not fees so we require a massive refactor of search */

        return feeService
            .search(new LookupFeeDto(service, jurisdiction1, jurisdiction2, channel, event, applicantType, amount, unspecifiedClaimAmounts, feeVersionStatus, author))
            .stream()
            .filter(f -> {
                if (feeVersionStatus!=null) {
                    return f.getFeeVersions().stream().anyMatch(v -> v.getStatus().equals(feeVersionStatus));
                }
                return true;
            })
            .map(feeDtoMapper::toFeeDto)
            .collect(Collectors.toList());
    }

    @ApiOperation(value = "Fee lookup based on reference data and amount", response = FeeLookupResponseDto.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Found"),
        @ApiResponse(code = 204, message = "Found, but fee amount is zero"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Not found")
    })
    @GetMapping("/fees/lookup")
    public ResponseEntity<FeeLookupResponseDto> lookup(@RequestParam String service,
                                                       @RequestParam String jurisdiction1,
                                                       @RequestParam String jurisdiction2,
                                                       @RequestParam String channel,
                                                       @RequestParam String event,
                                                       @RequestParam(required = false, defaultValue = "all", name = "applicant_type") String applicantType,
                                                       @RequestParam(required = false, name = "amount_or_volume") BigDecimal amountOrVolume,
                                                       HttpServletResponse response) {

        if (amountOrVolume != null && amountOrVolume.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Amount or volume should be greater than or equal to zero.");
        }

        final FeeLookupResponseDto responseDto = feeService.lookup(new LookupFeeDto(service, jurisdiction1, jurisdiction2, channel, event, applicantType, amountOrVolume, false, FeeVersionStatus.approved, null));

        if (responseDto.getFeeAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @ApiOperation(value = "Lookup for unspecified fee based on reference data", response = FeeLookupResponseDto.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Found"),
        @ApiResponse(code = 400, message = "Bad request"),
        @ApiResponse(code = 404, message = "Not found")
    })
    @GetMapping("/fees/lookup-unspecified")
    @ResponseStatus(HttpStatus.OK)
    public FeeLookupResponseDto lookupUnspecified(@RequestParam String service,
                                                  @RequestParam String jurisdiction1,
                                                  @RequestParam String jurisdiction2,
                                                  @RequestParam String channel,
                                                  @RequestParam String event,
                                                  @RequestParam(name = "applicant_type") String applicantType,
                                                  HttpServletResponse response) {
        return feeService.lookup(new LookupFeeDto(service, jurisdiction1, jurisdiction2, channel, event, applicantType, null, true, FeeVersionStatus.approved, null));
    }

    /* --- */

    private String getResourceLocation(Fee fee) {
        return URIUtils.getUrlForGetMethod(this.getClass(), "getFee").replace("{code}", fee.getCode());
    }

}
