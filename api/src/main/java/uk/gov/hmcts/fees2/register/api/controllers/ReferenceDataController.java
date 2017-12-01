package uk.gov.hmcts.fees2.register.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.fees2.register.api.contract.*;
import uk.gov.hmcts.fees2.register.api.controllers.mapper.ReferenceDataDtoMapper;
import uk.gov.hmcts.fees2.register.data.service.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Reference data Rest controller for all the reference data types
 *
 * @author Tarun Palisetty
 */

@RestController
@Validated
@RequestMapping("/fees-register")
public class ReferenceDataController {

    private final ChannelTypeService channelTypeService;

    private final DirectionTypeService directionTypeService;

    private final EventTypeService eventTypeService;

    private final Jurisdiction1Service jurisdiction1Service;

    private final Jurisdiction2Service jurisdiction2Service;

    private final ServiceTypeService serviceTypeService;

    private ReferenceDataDtoMapper referenceDataDtoMapper;

    @Autowired
    public ReferenceDataController(ChannelTypeService channelTypeService,
                                   DirectionTypeService directionTypeService, EventTypeService eventTypeService,
                                   Jurisdiction1Service jurisdiction1Service,
                                   Jurisdiction2Service jurisdiction2Service, ServiceTypeService serviceTypeService,
                                   ReferenceDataDtoMapper referenceDataDtoMapper) {
        this.channelTypeService = channelTypeService;
        this.directionTypeService = directionTypeService;
        this.eventTypeService = eventTypeService;
        this.jurisdiction1Service = jurisdiction1Service;
        this.jurisdiction2Service = jurisdiction2Service;
        this.serviceTypeService = serviceTypeService;
        this.referenceDataDtoMapper = referenceDataDtoMapper;
    }

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

        return dto;

    }

    @GetMapping("/channeltypes")
    @ResponseStatus(HttpStatus.OK)
    public List<ChannelTypeDto> getAllChannelTypes() {
        return channelTypeService.findAll().stream().map(referenceDataDtoMapper::toChannelTypeDto).collect(toList());
    }

    @GetMapping("/directiontypes")
    @ResponseStatus(HttpStatus.OK)
    public List<DirectionTypeDto> getAllDirectionTypes() {
        return directionTypeService.findAll().stream().map(referenceDataDtoMapper::toDirectionTypeDto).collect(toList());
    }

    @GetMapping("/eventtypes")
    @ResponseStatus(HttpStatus.OK)
    public List<EventTypeDto> getAllEventTypes() {
        return eventTypeService.findAll().stream().map(referenceDataDtoMapper::toEventTypeDto).collect(toList());
    }

    @GetMapping(path = "/jurisdictions1")
    @ResponseStatus(HttpStatus.OK)
    public List<Jurisdiction1Dto> getAllJurisdictions1() {
        return jurisdiction1Service.findAll().stream().map(referenceDataDtoMapper::toJuridiction1Dto).collect(toList());
    }

    @GetMapping("/jurisdictions2")
    @ResponseStatus(HttpStatus.OK)
    public List<Jurisdiction2Dto> getAllJurisdictions2() {
        return jurisdiction2Service.findAll().stream().map(referenceDataDtoMapper::toJurisdiction2Dto).collect(toList());
    }

    @GetMapping("/servicetypes")
    @ResponseStatus(HttpStatus.OK)
    public List<ServiceTypeDto> getAllServiceTypes() {
        return serviceTypeService.findAll().stream().map(referenceDataDtoMapper::toServiceTypeDto).collect(toList());
    }
}
