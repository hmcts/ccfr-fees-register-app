package uk.gov.hmcts.fees2.register.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.request.*;
import uk.gov.hmcts.fees2.register.api.controllers.exceptions.ForbiddenException;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.SearchFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.SearchFeeVersionDto;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.exceptions.BadRequestException;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.model.FixedFee;
import uk.gov.hmcts.fees2.register.data.model.RangedFee;
import uk.gov.hmcts.fees2.register.data.service.FeeSearchService;
import uk.gov.hmcts.fees2.register.data.service.FeeService;
import uk.gov.hmcts.fees2.register.util.URIUtils;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "FeesRegister", description = "Operations pertaining to fees")
@RestController
@RequestMapping(value = "/fees-register")
@AllArgsConstructor
@Validated
public class FeeController {
    private static final Logger LOG = LoggerFactory.getLogger(FeeController.class);

    public static final String LOCATION = "Location";

    @Autowired
    private final FeeService feeService;

    @Autowired
    private final FeeDtoMapper feeDtoMapper;

    @Autowired
    private final FeeSearchService feeSearchService;

    @ApiOperation(value = "Create ranged fee")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized, invalid user IDAM token"),
        @ApiResponse(code = 403, message = "Forbidden")
    })
    @PostMapping("/ranged-fees")
    @ResponseStatus(HttpStatus.CREATED)
    public void createRangedFee(
        @RequestBody @Validated final RangedFeeDto request,
        HttpServletResponse response,
        Principal principal) {

        Fee fee = feeService.saveAndGenerateFeeCode(
            feeDtoMapper.toFee(request, principal != null ? principal.getName() : null)
        );

        if (response != null) {
            response.setHeader(LOCATION, getResourceLocation(fee));
        }
    }


    @ApiOperation(value = "Update ranged fee")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Updated"),
        @ApiResponse(code = 401, message = "Unauthorized, invalid user IDAM token"),
        @ApiResponse(code = 403, message = "Forbidden")
    })
    @PutMapping("/ranged-fees/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void updateRangedFee(@PathVariable("code") String code,
                                @RequestBody @Validated final RangedFeeDto request,
                                HttpServletResponse response,
                                Principal principal) {
        RangedFee fee = (RangedFee) feeService.get(code);
        feeDtoMapper.updateRangedFee(request, fee, principal != null ? principal.getName() : null);
    }


    @ApiOperation(value = "Update fixed fee")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Updated"),
        @ApiResponse(code = 401, message = "Unauthorized, invalid user IDAM token"),
        @ApiResponse(code = 403, message = "Forbidden")
    })
    @PutMapping("/fixed-fees/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void updateFixedFee(@PathVariable("code") String code,
                               @RequestBody @Validated final FixedFeeDto request,
                               HttpServletResponse response,
                               Principal principal) {
        FixedFee fee = (FixedFee) feeService.get(code);
        feeDtoMapper.updateFixedFee(request, fee, principal != null ? principal.getName() : null);
    }


    @ApiOperation(value = "Create fixed fee")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized, invalid user IDAM token"),
        @ApiResponse(code = 403, message = "Forbidden")
    })
    @PostMapping(value = "/fixed-fees")
    @ResponseStatus(HttpStatus.CREATED)
    public void createFixedFee(@RequestBody @Validated final FixedFeeDto request,
                               HttpServletResponse response,
                               Principal principal) {

        Fee fee = feeDtoMapper.toFee(request, principal != null ? principal.getName() : null);

        fee = feeService.saveAndGenerateFeeCode(fee);

        if (response != null) {
            response.setHeader(LOCATION, getResourceLocation(fee));
        }
    }
    @ApiOperation(value = "Create rateable fee")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized, invalid user IDAM token"),
        @ApiResponse(code = 403, message = "Forbidden")
    })
    @PostMapping(value = "/rateable-fees")
    @ResponseStatus(HttpStatus.CREATED)
    public void createRateableFee(@RequestBody @Validated final RateableFeeDto request,
                             HttpServletResponse response,
                             Principal principal) {

        Fee fee = feeDtoMapper.toFee(request, principal != null ? principal.getName() : null);

        fee = feeService.saveAndGenerateFeeCode(fee);

        if (response != null) {
            response.setHeader(LOCATION, getResourceLocation(fee));
        }
    }


    @ApiOperation(value = "Create relational fee")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized, invalid user IDAM token"),
        @ApiResponse(code = 403, message = "Forbidden")
    })
    @PostMapping(value = "/relational-fees")
    @ResponseStatus(HttpStatus.CREATED)
    public void createRelationalFee(@RequestBody @Validated final RelationalFeeDto request,
                             HttpServletResponse response,
                             Principal principal) {

        Fee fee = feeDtoMapper.toFee(request, principal != null ? principal.getName() : null);

        fee = feeService.saveAndGenerateFeeCode(fee);

        if (response != null) {
            response.setHeader(LOCATION, getResourceLocation(fee));
        }
    }

    @ApiOperation(value = "Create banded fee")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Created"),
        @ApiResponse(code = 401, message = "Unauthorized, invalid user IDAM token"),
        @ApiResponse(code = 403, message = "Forbidden")
    })
    @PostMapping(value = "/banded-fees")
    @ResponseStatus(HttpStatus.CREATED)
    public void createBandedFee(@RequestBody @Validated final BandedFeeDto request,
                             HttpServletResponse response,
                             Principal principal) {

        Fee fee = feeDtoMapper.toFee(request, principal != null ? principal.getName() : null);

        fee = feeService.saveAndGenerateFeeCode(fee);

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
    public void createFixedFees(@RequestBody final List<FixedFeeDto> fixedFeeDtos, Principal principal) {
        LOG.info("No. of csv import fees: " + fixedFeeDtos.size());

        List<Fee> fixedFees = fixedFeeDtos
            .stream()
            .map(fixedFeeDto -> feeDtoMapper.toFee(fixedFeeDto, principal != null ? principal.getName() : null))
            .collect(Collectors.toList());

        feeService.saveAndGenerateFeeCode(fixedFees);
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
        @ApiResponse(code = 403, message = "Unable to delete fee due to an existing approved version")
    })
    @DeleteMapping("/fees/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFee(@PathVariable("code") String code, HttpServletResponse response) {
        // check if fee has any approved versions before deleting
        if (!feeService.safeDelete(code)) {
            throw new ForbiddenException();
        }
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
                                @RequestParam(required = false, name = "applicant_type") String applicantType,
                                @RequestParam(required = false) BigDecimal amount,
                                @RequestParam(required = false) Boolean unspecifiedClaimAmounts,
                                @RequestParam(required = false) FeeVersionStatus feeVersionStatus,
                                @RequestParam(required = false) String approvedBy,
                                @RequestParam(required = false) String author,
                                @RequestParam(required = false) Boolean isDraft,
                                @RequestParam(required = false) Boolean isActive,
                                @RequestParam(required = false) Boolean isExpired,
                                @RequestParam(required = false) String description,
                                @RequestParam(required = false) String siRefId,
                                @RequestParam(required = false) BigDecimal feeVersionAmount,
                                HttpServletResponse response) {
        List<Fee2Dto> result;
        SearchFeeDto searchFeeDto = new SearchFeeDto(amount, service, jurisdiction1, jurisdiction2, channel, event, applicantType, unspecifiedClaimAmounts, isDraft);
        SearchFeeVersionDto searchFeeVersionDto = new SearchFeeVersionDto(author, approvedBy, isActive, isExpired, feeVersionStatus, description, siRefId, feeVersionAmount);

        if (searchFeeVersionDto.isNoFieldSet()) {
            result = feeSearchService.search(searchFeeDto)
                .stream()
                .map(feeDtoMapper::toFeeDto)
                .collect(Collectors.toList());
        } else {
            result = feeSearchService
                .search(searchFeeDto, searchFeeVersionDto)
                .stream()
                .map(feeDtoMapper::toFeeDto)
                .collect(Collectors.toList());
        }

        return result;
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
                                                       @RequestParam(required = false, name = "applicant_type") String applicantType,
                                                       @RequestParam(required = false, name = "amount_or_volume") BigDecimal amountOrVolume,
                                                       HttpServletResponse response) {

        if (amountOrVolume != null && amountOrVolume.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Amount or volume should be greater than or equal to zero.");
        }

        if ((event != null && event.equals("copies")) && (amountOrVolume != null && amountOrVolume.scale() >= 1)) {
            throw new BadRequestException("Volume cannot be in fractions.");
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
                                                  @RequestParam(required = false, name = "applicant_type") String applicantType,
                                                  HttpServletResponse response) {
        return feeService.lookup(new LookupFeeDto(service, jurisdiction1, jurisdiction2, channel, event, applicantType, null, true, FeeVersionStatus.approved, null));
    }

    /* --- */

    private String getResourceLocation(Fee fee) {
        return URIUtils.getUrlForGetMethod(this.getClass(), "getFee").replace("{code}", fee.getCode());
    }

}
