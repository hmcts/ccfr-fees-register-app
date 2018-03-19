package uk.gov.hmcts.fees2.register.api.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

@Api(value = "ReferenceData", description = "Operations pertaining to fees reference data")
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

    @ApiOperation(value = "Get all reference data", response = AllReferenceDataDto.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Found"),
        @ApiResponse(code = 404, message = "Not found")
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
        dto.setApplicationTypes(getAllApplicantTypes());
        return dto;

    }

    @ApiOperation(value = "Get application types reference data", response = ApplicantTypeDto.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Found"),
        @ApiResponse(code = 404, message = "Not found")
    })
    @GetMapping("/applicant-types")
    @ResponseStatus(HttpStatus.OK)
    public List<ApplicantTypeDto> getAllApplicantTypes() {
        return applicantTypeService.findAll().stream().map(referenceDataDtoMapper::toApplicantTypeDto).collect(toList());
    }

    @ApiOperation(value = "Get channel types reference data", response = ChannelTypeDto.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Found"),
        @ApiResponse(code = 404, message = "Not found")
    })
    @GetMapping("/channel-types")
    @ResponseStatus(HttpStatus.OK)
    public List<ChannelTypeDto> getAllChannelTypes() {
        return channelTypeService.findAll().stream().map(referenceDataDtoMapper::toChannelTypeDto).collect(toList());
    }

    @ApiOperation(value = "Get direction types reference data", response = DirectionTypeDto.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Found"),
        @ApiResponse(code = 404, message = "Not found")
    })
    @GetMapping("/direction-types")
    @ResponseStatus(HttpStatus.OK)
    public List<DirectionTypeDto> getAllDirectionTypes() {
        return directionTypeService.findAll().stream().map(referenceDataDtoMapper::toDirectionTypeDto).collect(toList());
    }

    @ApiOperation(value = "Get event types reference data", response = EventTypeDto.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Found"),
        @ApiResponse(code = 404, message = "Not found")
    })
    @GetMapping("/event-types")
    @ResponseStatus(HttpStatus.OK)
    public List<EventTypeDto> getAllEventTypes() {
        return eventTypeService.findAll().stream().map(referenceDataDtoMapper::toEventTypeDto).collect(toList());
    }

    @ApiOperation(value = "Get jurisdiction1 types reference data", response = Jurisdiction1Dto.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Found"),
        @ApiResponse(code = 404, message = "Not found")
    })
    @GetMapping("/jurisdictions1")
    @ResponseStatus(HttpStatus.OK)
    public List<Jurisdiction1Dto> getAllJurisdictions1() {
        return jurisdiction1Service.findAll().stream().map(referenceDataDtoMapper::toJuridiction1Dto).collect(toList());
    }

    @ApiOperation(value = "Get jurisdiction2 types reference data", response = Jurisdiction2Dto.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Found"),
        @ApiResponse(code = 404, message = "Not found")
    })
    @GetMapping("/jurisdictions2")
    @ResponseStatus(HttpStatus.OK)
    public List<Jurisdiction2Dto> getAllJurisdictions2() {
        return jurisdiction2Service.findAll().stream().map(referenceDataDtoMapper::toJurisdiction2Dto).collect(toList());
    }

    @ApiOperation(value = "Get service types reference data", response = ServiceTypeDto.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Found"),
        @ApiResponse(code = 404, message = "Not found")
    })
    @GetMapping("/service-types")
    @ResponseStatus(HttpStatus.OK)
    public List<ServiceTypeDto> getAllServiceTypes() {
        return serviceTypeService.findAll().stream().map(referenceDataDtoMapper::toServiceTypeDto).collect(toList());
    }

    @ApiOperation(value = "Get range units reference data", response = RangeUnit.class, responseContainer = "List")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Found"),
        @ApiResponse(code = 404, message = "Not found")
    })
    @GetMapping("/range-units")
    @ResponseStatus(HttpStatus.OK)
    public List<RangeUnit> getAllRangeUnits() {
        return rangeUnitRepository.findAll();
    }

}
