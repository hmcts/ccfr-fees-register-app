package uk.gov.hmcts.fees2.register.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.fees2.register.api.contract.Fee2Dto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionStatusDto;
import uk.gov.hmcts.fees2.register.api.contract.request.BandedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.FixedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.RangedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.RateableFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.RelationalFeeDto;
import uk.gov.hmcts.fees2.register.api.controllers.exceptions.ForbiddenException;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.FeeDtoMapper;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.SearchFeeDto;
import uk.gov.hmcts.fees2.register.data.dto.SearchFeeVersionDto;
import uk.gov.hmcts.fees2.register.data.dto.response.FeeLookupResponseDto;
import uk.gov.hmcts.fees2.register.data.exceptions.BadRequestException;
import uk.gov.hmcts.fees2.register.data.exceptions.GatewayTimeoutException;
import uk.gov.hmcts.fees2.register.data.exceptions.UserNotFoundException;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;
import uk.gov.hmcts.fees2.register.data.model.FixedFee;
import uk.gov.hmcts.fees2.register.data.model.RangedFee;
import uk.gov.hmcts.fees2.register.data.service.FeeSearchService;
import uk.gov.hmcts.fees2.register.data.service.FeeService;
import uk.gov.hmcts.fees2.register.util.SecurityUtil;
import uk.gov.hmcts.fees2.register.util.URIUtils;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "FeesRegister")
@RestController
@RequestMapping(value = "/fees-register")
@AllArgsConstructor
@Validated
public class FeeController {
    private static final Logger LOG = LoggerFactory.getLogger(FeeController.class);

    public static final String LOCATION = "Location";
    public static final String FREG_ADMIN = "freg-admin";

    @Autowired
    private final FeeService feeService;

    @Autowired
    private final FeeDtoMapper feeDtoMapper;

    @Autowired
    private final FeeSearchService feeSearchService;

    @Operation(summary = "Create ranged fee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created"),
        @ApiResponse(responseCode = "401", description = "Unauthorized, invalid user IDAM token"),
        @ApiResponse(responseCode = "403", description = "Forbidden"),
        @ApiResponse(responseCode = "409", description = "Conflict")
    })
    @PostMapping("/ranged-fees")
    @ResponseStatus(HttpStatus.CREATED)
    public void createRangedFee(
        @RequestBody @Validated final RangedFeeDto request,
        HttpServletResponse response,
        Principal principal) {

        Encode.forHtml(request.getVersion().getReasonForUpdate());
        Fee fee = feeService.save(
            feeDtoMapper.toFee(request, principal != null ? principal.getName() : null)
        );

        if (response != null) {
            response.setHeader(LOCATION, getResourceLocation(fee));
        }
    }


    @Operation(summary = "Update ranged fee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Updated"),
        @ApiResponse(responseCode = "401", description = "Unauthorized, invalid user IDAM token"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PutMapping("/ranged-fees/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void updateRangedFee(@PathVariable("code") String code,
                                @RequestBody @Validated final RangedFeeDto request,
                                HttpServletResponse response,
                                Principal principal) {
        Encode.forHtml(request.getVersion().getReasonForUpdate());
        RangedFee fee = (RangedFee) feeService.getFee(code);
        feeDtoMapper.updateRangedFee(request, fee, principal != null ? principal.getName() : null);
    }


    @Operation(summary = "Update fixed fee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Updated"),
        @ApiResponse(responseCode = "401", description = "Unauthorized, invalid user IDAM token"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PutMapping("/fixed-fees/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void updateFixedFee(@PathVariable("code") String code,
                               @RequestBody @Validated final FixedFeeDto request,
                               HttpServletResponse response,
                               Principal principal) {
        Encode.forHtml(request.getVersion().getReasonForUpdate());
        FixedFee fee = (FixedFee) feeService.getFee(code);
        feeDtoMapper.updateFixedFee(request, fee, principal != null ? principal.getName() : null);
    }


    @Operation(summary = "Create fixed fee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created"),
        @ApiResponse(responseCode = "401", description = "Unauthorized, invalid user IDAM token"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping(value = "/fixed-fees")
    @ResponseStatus(HttpStatus.CREATED)
    public void createFixedFee(@RequestBody @Validated final FixedFeeDto request,
                               HttpServletResponse response,
                               Principal principal) {

        Encode.forHtml(request.getVersion().getReasonForUpdate());
        Fee fee = feeDtoMapper.toFee(request, principal != null ? principal.getName() : null);

        fee = feeService.save(fee);

        if (response != null) {
            response.setHeader(LOCATION, getResourceLocation(fee));
        }
    }

    @Operation(summary = "Create rateable fee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created"),
        @ApiResponse(responseCode = "401", description = "Unauthorized, invalid user IDAM token"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping(value = "/rateable-fees")
    @ResponseStatus(HttpStatus.CREATED)
    public void createRateableFee(@RequestBody @Validated final RateableFeeDto request,
                                  HttpServletResponse response,
                                  Principal principal) {

        Encode.forHtml(request.getVersion().getReasonForUpdate());
        Fee fee = feeDtoMapper.toFee(request, principal != null ? principal.getName() : null);

        fee = feeService.save(fee);

        if (response != null) {
            response.setHeader(LOCATION, getResourceLocation(fee));
        }
    }


    @Operation(summary = "Create relational fee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created"),
        @ApiResponse(responseCode = "401", description = "Unauthorized, invalid user IDAM token"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping(value = "/relational-fees")
    @ResponseStatus(HttpStatus.CREATED)
    public void createRelationalFee(@RequestBody @Validated final RelationalFeeDto request,
                                    HttpServletResponse response,
                                    Principal principal) {

        Encode.forHtml(request.getVersion().getReasonForUpdate());
        Fee fee = feeDtoMapper.toFee(request, principal != null ? principal.getName() : null);

        fee = feeService.save(fee);

        if (response != null) {
            response.setHeader(LOCATION, getResourceLocation(fee));
        }
    }

    @Operation(summary = "Create banded fee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created"),
        @ApiResponse(responseCode = "401", description = "Unauthorized, invalid user IDAM token"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping(value = "/banded-fees")
    @ResponseStatus(HttpStatus.CREATED)
    public void createBandedFee(@RequestBody @Validated final BandedFeeDto request,
                                HttpServletResponse response,
                                Principal principal) {

        Encode.forHtml(request.getVersion().getReasonForUpdate());
        Fee fee = feeDtoMapper.toFee(request, principal != null ? principal.getName() : null);

        fee = feeService.save(fee);

        if (response != null) {
            response.setHeader(LOCATION, getResourceLocation(fee));
        }
    }


    @Operation(summary = "Create bulk fees")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Created"),
        @ApiResponse(responseCode = "401", description = "Unauthorized, invalid user IDAM token"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @Transactional
    @PostMapping(value = "/bulk-fixed-fees")
    @ResponseStatus(HttpStatus.CREATED)
    public void createFixedFees(@RequestBody final List<FixedFeeDto> fixedFeeDtos, Principal principal) {
        final String encodedDtoSize = Encode.forJava(String.valueOf(fixedFeeDtos.size()));
        LOG.info("No. of csv import fees: {}", encodedDtoSize);

        List<Fee> fixedFees = fixedFeeDtos
            .stream()
            .map(fixedFeeDto -> feeDtoMapper.toFee(fixedFeeDto, principal != null ? principal.getName() : null))
            .collect(Collectors.toList());

        feeService.save(fixedFees);
    }

    @Operation(summary = "Get a fee for the given fee code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "404", description = "Not Found")
    })
    @GetMapping("/fees/{code}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public Fee2Dto getFee(@PathVariable("code") String code, HttpServletResponse response,
                          @RequestHeader(required = false) MultiValueMap<String, String> headers) {
        Fee fee = feeService.getFee(code);
        return feeDtoMapper.toFeeDto(fee, headers);
    }

    @RequestMapping(method = RequestMethod.HEAD, path = "/fees/{code}")
    @ResponseStatus(HttpStatus.OK)
    public void doesFeeExist(@PathVariable("code") String code) {
        feeService.getFee(code);
    }

    @Operation(summary = "Delete a fee for the given fee code")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted the fee for the given fee code."),
        @ApiResponse(responseCode = "403", description = "Unable to delete fee due to an existing approved version")
    })
    @DeleteMapping("/fees/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFee(@PathVariable("code") String code) {
        if (SecurityUtil.hasRole(FREG_ADMIN)) { // force delete
            LOG.info("Force deleting a fee with admin role");
            feeService.delete(code);
        } else if (!feeService.safeDelete(code)) { // check if fee has any approved versions before deleting
            throw new ForbiddenException("Cannot delete a fee with an approved version");
        }
    }

    @Operation(summary = "Search for fees based on criteria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "404", description = "Not found")
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
                                @RequestParam(required = false) Boolean discontinued) {
        List<Fee2Dto> result;
        SearchFeeDto searchFeeDto = new SearchFeeDto(amount, service, jurisdiction1, jurisdiction2, channel, event, applicantType, unspecifiedClaimAmounts, isDraft);
        SearchFeeVersionDto searchFeeVersionDto = new SearchFeeVersionDto(author, approvedBy, isActive, isExpired, discontinued, feeVersionStatus, description, siRefId, feeVersionAmount);

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

    @Operation(summary = "Fee lookup based on reference data and amount")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "204", description = "Found, but fee amount is zero"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/fees/lookup")
    public ResponseEntity<FeeLookupResponseDto> lookup(@RequestParam String service,
                                                       @RequestParam String jurisdiction1,
                                                       @RequestParam String jurisdiction2,
                                                       @RequestParam String channel,
                                                       @RequestParam String event,
                                                       @RequestParam(required = false, name = "applicant_type") String applicantType,
                                                       @RequestParam(required = false, name = "amount_or_volume") BigDecimal amountOrVolume,
                                                       @RequestParam(required = false, name = "keyword") String keyword) {

        if (amountOrVolume != null && amountOrVolume.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Amount or volume should be greater than or equal to zero.");
        }

        if ((event != null && event.equals("copies")) && (amountOrVolume != null && amountOrVolume.scale() >= 1)) {
            throw new BadRequestException("Volume cannot be in fractions.");
        }

        LookupFeeDto lookupFeeDto = LookupFeeDto.lookupWith()
            .service(service)
            .jurisdiction1(jurisdiction1)
            .jurisdiction2(jurisdiction2)
            .channel(channel)
            .event(event)
            .applicantType(applicantType)
            .amountOrVolume(amountOrVolume)
            .unspecifiedClaimAmount(false)
            .versionStatus(FeeVersionStatus.approved)
            .keyword(keyword)
            .build();

        final FeeLookupResponseDto responseDto = feeService.lookup(lookupFeeDto);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "Lookup for unspecified fee based on reference data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/fees/lookup-unspecified")
    @ResponseStatus(HttpStatus.OK)
    public FeeLookupResponseDto lookupUnspecified(@RequestParam String service,
                                                  @RequestParam String jurisdiction1,
                                                  @RequestParam String jurisdiction2,
                                                  @RequestParam String channel,
                                                  @RequestParam String event,
                                                  @RequestParam(required = false, name = "applicant_type") String applicantType,
                                                  @RequestParam(required = false, name = "keyword") String keyword) {
        LookupFeeDto lookupFeeDto = LookupFeeDto.lookupWith()
            .service(service)
            .jurisdiction1(jurisdiction1)
            .jurisdiction2(jurisdiction2)
            .channel(channel)
            .event(event)
            .applicantType(applicantType)
            .unspecifiedClaimAmount(true)
            .versionStatus(FeeVersionStatus.approved)
            .keyword(keyword)
            .build();
        return feeService.lookup(lookupFeeDto);
    }

    @Operation(summary = "Prevalidates a fee based on its reference data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "409", description = "Fee conflicts with one or more existing fees")
    })
    @GetMapping("/fees/prevalidate")
    @ResponseStatus(HttpStatus.OK)
    public void prevalidate(@RequestParam String service,
                            @RequestParam String jurisdiction1,
                            @RequestParam String jurisdiction2,
                            @RequestParam String channel,
                            @RequestParam String event,
                            @RequestParam String keyword,
                            @RequestParam(required = false) BigDecimal rangeFrom,
                            @RequestParam(required = false) BigDecimal rangeTo
    ) {

        feeService.prevalidate(
            Fee.fromMetadata(service, channel, event, jurisdiction1, jurisdiction2, keyword, rangeFrom, rangeTo)
        );
    }

    /* --- */

    private String getResourceLocation(Fee fee) {
        return URIUtils.getUrlForGetMethod(this.getClass(), "getFee").replace("{code}", fee.getCode());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(UserNotFoundException.class)
    public String return500(UserNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    @ExceptionHandler(GatewayTimeoutException.class)
    public String return504(GatewayTimeoutException ex) {
        return ex.getMessage();
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/approvedFees")
    @ResponseStatus(HttpStatus.OK)
    public List<Fee2Dto> approvedFees() {
        List<Fee2Dto> result =  search(null, null, null, null, null,
            null, null, null, FeeVersionStatus.approved, null,
            null, false, true, null, null, null, null, null);
        result = result
            .stream()
            .filter(c -> c.getCurrentVersion()!=null)
            .filter(c -> c.getCurrentVersion().getStatus().equals(FeeVersionStatusDto.approved))
            .collect(Collectors.toList());

        // return only approved versions of the approved fees
        for (Fee2Dto fee2Dto : result) {
            if (fee2Dto.getFeeVersionDtos() != null) {
                List<FeeVersionDto> approvedVersions = fee2Dto.getFeeVersionDtos()
                    .stream()
                    .filter(fv -> FeeVersionStatusDto.approved.equals(fv.getStatus()))
                    .collect(Collectors.toList());
                fee2Dto.setFeeVersionDtos(approvedVersions);
            }
        }

        // remove sensitive info
        for (Fee2Dto fee2Dto : result) {
            for (FeeVersionDto feeVersionDto : fee2Dto.getFeeVersionDtos()) {
                feeVersionDto.setApprovedBy(null);
                feeVersionDto.setAuthor(null);
                feeVersionDto.setLastAmendingSi(null);
                feeVersionDto.setStatutoryInstrument(null);
                feeVersionDto.setSiRefId(null);
                feeVersionDto.setDirection(null);

            }
            fee2Dto.getCurrentVersion().setAuthor(null);
            fee2Dto.getCurrentVersion().setApprovedBy(null);
            fee2Dto.getCurrentVersion().setLastAmendingSi(null);
            fee2Dto.getCurrentVersion().setStatutoryInstrument(null);
            fee2Dto.getCurrentVersion().setSiRefId(null);
            fee2Dto.getCurrentVersion().setDirection(null);
            fee2Dto.setApplicantTypeDto(null);


            if (fee2Dto.getCurrentVersion().getFlatAmount() != null) {
                fee2Dto.setAmountType("FLAT");
            } else {
                fee2Dto.setAmountType("VOLUME");
            }

        }

        return result;
    }

}
