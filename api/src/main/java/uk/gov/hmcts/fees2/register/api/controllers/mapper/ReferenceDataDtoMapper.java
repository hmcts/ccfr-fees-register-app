package uk.gov.hmcts.fees2.register.api.controllers.mapper;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees2.register.api.contract.*;
import uk.gov.hmcts.fees2.register.data.model.*;

/**
 * ReferenceData entity to DTO mapper
 *
 * @author Tarun Palisetty
 */

@Component
public class ReferenceDataDtoMapper {

    /**
     *
     * @param channelType
     * @return
     */
    public ChannelTypeDto toChannelTypeDto(ChannelType channelType) {
        return ChannelTypeDto.channelTypeDtoWith()
                .name(channelType.getName())
                .build();
    }

    /**
     *
     * @param directionType
     * @return
     */
    public DirectionTypeDto toDirectionTypeDto(DirectionType directionType) {
        return DirectionTypeDto.directionTypeDtoWith()
                .name(directionType.getName())
                .build();
    }

    /**
     *
     * @param eventType
     * @return
     */
    public EventTypeDto toEventTypeDto(EventType eventType) {
        return EventTypeDto.eventTypeDtoWith()
                .name(eventType.getName())
                .build();
    }

    /**
     *
     * @param jurisdiction1
     * @return
     */
    public Jurisdiction1Dto toJuridiction1Dto(Jurisdiction1 jurisdiction1) {
        return Jurisdiction1Dto.jurisdiction1TypeDtoWith()
                .name(jurisdiction1.getName())
                .build();
    }

    /**
     *
     * @param jurisdiction2
     * @return
     */
    public Jurisdiction2Dto toJurisdiction2Dto(Jurisdiction2 jurisdiction2) {
        return Jurisdiction2Dto.jurisdiction2TypeDtoWith()
                .name(jurisdiction2.getName())
                .build();
    }

    /**
     *
     * @param serviceType
     * @return
     */
    public ServiceTypeDto toServiceTypeDto(ServiceType serviceType) {
        return ServiceTypeDto.serviceTypeDtoWith()
                .name(serviceType.getName())
                .build();
    }
 }
