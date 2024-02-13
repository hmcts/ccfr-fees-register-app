package uk.gov.hmcts.fees2.register.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.fees2.register.api.contract.*;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.ReferenceDataDtoMapper;
import uk.gov.hmcts.fees2.register.data.model.RangeUnit;
import uk.gov.hmcts.fees2.register.data.repository.RangeUnitRepository;
import uk.gov.hmcts.fees2.register.data.service.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Reference data Rest controller for all the reference data types
 *
 * @author Tarun Palisetty
 */

@Tag(name = "ReferenceData")
@RestController
@Validated
public class ReferenceDataController {

    private final ChannelTypeService channelTypeService;

    private final DirectionTypeService directionTypeService;

    private final EventTypeService eventTypeService;

    private final Jurisdiction1Service jurisdiction1Service;

    private final Jurisdiction2Service jurisdiction2Service;

    private final ServiceTypeService serviceTypeService;

    private final RangeUnitRepository rangeUnitRepository;

    private ReferenceDataDtoMapper referenceDataDtoMapper;

    private final ApplicantTypeService applicantTypeService;


    @Autowired
    public ReferenceDataController(ChannelTypeService channelTypeService,
                                   DirectionTypeService directionTypeService, EventTypeService eventTypeService,
                                   Jurisdiction1Service jurisdiction1Service,
                                   Jurisdiction2Service jurisdiction2Service, ServiceTypeService serviceTypeService,
                                   RangeUnitRepository rangeUnitRepository, ApplicantTypeService applicantTypeService,
                                   ReferenceDataDtoMapper referenceDataDtoMapper) {
        this.channelTypeService = channelTypeService;
        this.directionTypeService = directionTypeService;
        this.eventTypeService = eventTypeService;
        this.jurisdiction1Service = jurisdiction1Service;
        this.jurisdiction2Service = jurisdiction2Service;
        this.serviceTypeService = serviceTypeService;
        this.rangeUnitRepository = rangeUnitRepository;
        this.referenceDataDtoMapper = referenceDataDtoMapper;
        this.applicantTypeService = applicantTypeService;
    }

    @Operation(summary = "Get all reference data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/referenceData")
    @ResponseStatus(HttpStatus.OK)
    public AllReferenceDataDto getAllReferenceData(){

        AllReferenceDataDto dto = new AllReferenceDataDto();

        dto.setChannelTypes(getAllChannelTypes());
        dto.setDirectionTypes(getAllDirectionTypes());
        dto.setServiceTypes(getAllServiceTypes());
        dto.setEventTypes(getAllEventTypes());
        dto.setJurisdictions1(getAllJurisdictions1());
        dto.setJurisdictions2(getAllJurisdictions2());
        dto.setRangeUnits(getAllRangeUnits());
        dto.setApplicantTypes(getAllApplicantTypes());
        return dto;

    }

    @Operation(summary = "Get application types reference data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/applicant-types")
    @ResponseStatus(HttpStatus.OK)
    public List<ApplicantTypeDto> getAllApplicantTypes() {
        return applicantTypeService.findAll().stream().map(referenceDataDtoMapper::toApplicantTypeDto).collect(toList());
    }

    @Operation(summary = "Get channel types reference data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/channel-types")
    @ResponseStatus(HttpStatus.OK)
    public List<ChannelTypeDto> getAllChannelTypes() {
        return channelTypeService.findAll().stream().map(referenceDataDtoMapper::toChannelTypeDto).collect(toList());
    }

    @Operation(summary = "Get direction types reference data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/direction-types")
    @ResponseStatus(HttpStatus.OK)
    public List<DirectionTypeDto> getAllDirectionTypes() {
        return directionTypeService.findAll().stream().map(referenceDataDtoMapper::toDirectionTypeDto).collect(toList());
    }

    @Operation(summary = "Get event types reference data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/event-types")
    @ResponseStatus(HttpStatus.OK)
    public List<EventTypeDto> getAllEventTypes() {
        return eventTypeService.findAll().stream().map(referenceDataDtoMapper::toEventTypeDto).collect(toList());
    }

    @Operation(summary = "Get jurisdiction1 types reference data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/jurisdictions1")
    @ResponseStatus(HttpStatus.OK)
    public List<Jurisdiction1Dto> getAllJurisdictions1() {
        return jurisdiction1Service.findAll().stream().map(referenceDataDtoMapper::toJuridiction1Dto).collect(toList());
    }

    @Operation(summary = "Get jurisdiction2 types reference data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/jurisdictions2")
    @ResponseStatus(HttpStatus.OK)
    public List<Jurisdiction2Dto> getAllJurisdictions2() {
        return jurisdiction2Service.findAll().stream().map(referenceDataDtoMapper::toJurisdiction2Dto).collect(toList());
    }

    @Operation(summary = "Get service types reference data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/service-types")
    @ResponseStatus(HttpStatus.OK)
    public List<ServiceTypeDto> getAllServiceTypes() {
        return serviceTypeService.findAll().stream().map(referenceDataDtoMapper::toServiceTypeDto).collect(toList());
    }

    @Operation(summary = "Get range units reference data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found"),
        @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping("/range-units")
    @ResponseStatus(HttpStatus.OK)
    public List<RangeUnitDto> getAllRangeUnits() {
        List<RangeUnit> units = rangeUnitRepository.findAll();
        return units.stream()
            .map(e -> new RangeUnitDto(e.getName()))
            .collect(toList());
    }

}
