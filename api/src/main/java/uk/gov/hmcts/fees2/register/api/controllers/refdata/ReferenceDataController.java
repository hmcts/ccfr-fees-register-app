package uk.gov.hmcts.fees2.register.api.controllers.refdata;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.fees2.register.api.contract.*;
import uk.gov.hmcts.fees2.register.data.model.*;
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
public class ReferenceDataController {

    private final AmountTypeService amountTypeService;

    private final ChannelTypeService channelTypeService;

    private final DirectionTypeService directionTypeService;

    private final EventTypeService eventTypeService;

    private final FeeTypeService feeTypeService;

    private final Jurisdiction1Service jurisdiction1Service;

    private final Jurisdiction2Service jurisdiction2Service;

    private final ServiceTypeService serviceTypeService;

    private ReferenceDataDtoMapper referenceDataDtoMapper;

    @Autowired
    public ReferenceDataController(AmountTypeService amountTypeService, ChannelTypeService channelTypeService,
                                   DirectionTypeService directionTypeService, EventTypeService eventTypeService,
                                   FeeTypeService feeTypeService, Jurisdiction1Service jurisdiction1Service,
                                   Jurisdiction2Service jurisdiction2Service, ServiceTypeService serviceTypeService,
                                   ReferenceDataDtoMapper referenceDataDtoMapper) {
        this.amountTypeService = amountTypeService;
        this.channelTypeService = channelTypeService;
        this.directionTypeService = directionTypeService;
        this.eventTypeService = eventTypeService;
        this.feeTypeService = feeTypeService;
        this.jurisdiction1Service = jurisdiction1Service;
        this.jurisdiction2Service = jurisdiction2Service;
        this.serviceTypeService = serviceTypeService;
        this.referenceDataDtoMapper = referenceDataDtoMapper;
    }

    @GetMapping("/amounttypes")
    @ResponseStatus(HttpStatus.OK)
    public List<AmountTypeDto> getAllAmountTypes() {
        return amountTypeService.findAll().stream().map(referenceDataDtoMapper::toAmountTypeDto).collect(toList());
    }

    @GetMapping("/amounttypes/{name}")
    @ResponseStatus(HttpStatus.OK)
    @Deprecated
    public AmountTypeDto getAmountTypeByName(@NotEmpty @PathVariable(name = "name") String name) {
        AmountType amountType = amountTypeService.findByNameOrThrow(name);
        return referenceDataDtoMapper.toAmountTypeDto(amountType);
    }

    @GetMapping("/channeltypes")
    @ResponseStatus(HttpStatus.OK)
    public List<ChannelTypeDto> getAllChannelTypes() {
        return channelTypeService.findAll().stream().map(referenceDataDtoMapper::toChannelTypeDto).collect(toList());
    }

    @GetMapping("/channeltypes/{name}")
    @ResponseStatus(HttpStatus.OK)
    @Deprecated
    public ChannelTypeDto getChannelTypeByName(@NotEmpty @PathVariable(name = "name") String name) {
        ChannelType channelType = channelTypeService.findByNameOrThrow(name);
        return referenceDataDtoMapper.toChannelTypeDto(channelType);
    }

    @GetMapping("/directiontypes")
    @ResponseStatus(HttpStatus.OK)
    public List<DirectionTypeDto> getAllDirectionTypes() {
        return directionTypeService.findAll().stream().map(referenceDataDtoMapper::toDirectionTypeDto).collect(toList());
    }

    @GetMapping("/directiontypes/{name}")
    @ResponseStatus(HttpStatus.OK)
    @Deprecated
    public DirectionTypeDto getDirectionTypeByName(@NotEmpty @PathVariable(name = "name") String name) {
        DirectionType directionType = directionTypeService.findByNameOrThrow(name);
        return referenceDataDtoMapper.toDirectionTypeDto(directionType);
    }

    @GetMapping("/eventtypes")
    @ResponseStatus(HttpStatus.OK)
    public List<EventTypeDto> getAllEventTypes() {
        return eventTypeService.findAll().stream().map(referenceDataDtoMapper::toEventTypeDto).collect(toList());
    }

    @GetMapping("/eventtypes/{name}")
    @ResponseStatus(HttpStatus.OK)
    @Deprecated
    public EventTypeDto getEventTypeByName(@NotEmpty @PathVariable(name = "name") String name) {
        EventType eventType = eventTypeService.findByNameOrThrow(name);
        return referenceDataDtoMapper.toEventTypeDto(eventType);
    }

    @GetMapping("/feetypes")
    @ResponseStatus(HttpStatus.OK)
    public List<FeeTypeDto> getAllFeeTypes() {
        return feeTypeService.findAll().stream().map(referenceDataDtoMapper::toFeeTypeDto).collect(toList());
    }

    @GetMapping("/feetypes/{name}")
    @ResponseStatus(HttpStatus.OK)
    @Deprecated
    public FeeTypeDto getFeeTypeByName(@NotEmpty @PathVariable(name = "name") String name) {
        FeeType feeType = feeTypeService.findByNameOrThrow(name);
        return referenceDataDtoMapper.toFeeTypeDto(feeType);
    }

    @GetMapping(path = "/jurisdictions1")
    @ResponseStatus(HttpStatus.OK)
    public List<Jurisdiction1Dto> getAllJurisdictions1() {
        return jurisdiction1Service.findAll().stream().map(referenceDataDtoMapper::toJuridiction1Dto).collect(toList());
    }

    @GetMapping("/jurisdictions1/{name}")
    @ResponseStatus(HttpStatus.OK)
    @Deprecated
    public Jurisdiction1Dto getJurisdiction1ByName(@NotEmpty @PathVariable(name = "name") String name) {
        Jurisdiction1 jurisdiction1 = jurisdiction1Service.findByNameOrThrow(name);
        return referenceDataDtoMapper.toJuridiction1Dto(jurisdiction1);
    }

    @GetMapping("/jurisdictions2")
    @ResponseStatus(HttpStatus.OK)
    public List<Jurisdiction2Dto> getAllJurisdictions2() {
        return jurisdiction2Service.findAll().stream().map(referenceDataDtoMapper::toJurisdiction2Dto).collect(toList());
    }

    @GetMapping("/jurisdictions2/{name}")
    @ResponseStatus(HttpStatus.OK)
    @Deprecated
    public Jurisdiction2Dto getJurisdiction2ByName(@NotEmpty @PathVariable(name = "name") String name) {
        Jurisdiction2 jurisdiction2 = jurisdiction2Service.findByNameOrThrow(name);
        return referenceDataDtoMapper.toJurisdiction2Dto(jurisdiction2);
    }

    @GetMapping("/servicetypes")
    @ResponseStatus(HttpStatus.OK)
    public List<ServiceTypeDto> getAllServiceTypes() {
        return serviceTypeService.findAll().stream().map(referenceDataDtoMapper::toServiceTypeDto).collect(toList());
    }

    @GetMapping("/servicetypes/{name}")
    @ResponseStatus(HttpStatus.OK)
    @Deprecated
    public ServiceTypeDto getServiceTypeByName(@NotEmpty @PathVariable(name = "name") String name) {
        ServiceType serviceType = serviceTypeService.findByNameOrThrow(name);
        return referenceDataDtoMapper.toServiceTypeDto(serviceType);
    }
}
