package uk.gov.hmcts.fees2.register.api.controllers.mapper;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees2.register.api.contract.*;
import uk.gov.hmcts.fees2.register.data.model.*;

/**
 * ReferenceData entity to DTO mapper.
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
        return channelType == null ? null : ChannelTypeDto.channelTypeDtoWith()
                .name(channelType.getName())
                .build();
    }

    /**
     *
     * @param directionType
     * @return
     */
    public DirectionTypeDto toDirectionTypeDto(DirectionType directionType) {
        return directionType == null ? null : DirectionTypeDto.directionTypeDtoWith()
                .name(directionType.getName())
                .build();
    }

    /**
     *
     * @param eventType
     * @return
     */
    public EventTypeDto toEventTypeDto(EventType eventType) {
        return eventType == null ? null : EventTypeDto.eventTypeDtoWith()
                .name(eventType.getName())
                .build();
    }

    /**
     *
     * @param jurisdiction1
     * @return
     */
    public Jurisdiction1Dto toJuridiction1Dto(Jurisdiction1 jurisdiction1) {
        return jurisdiction1 == null ? null : Jurisdiction1Dto.jurisdiction1TypeDtoWith()
                .name(jurisdiction1.getName())
                .build();
    }

    /**
     *
     * @param jurisdiction2
     * @return
     */
    public Jurisdiction2Dto toJurisdiction2Dto(Jurisdiction2 jurisdiction2) {
        return jurisdiction2 == null ? null : Jurisdiction2Dto.jurisdiction2TypeDtoWith()
                .name(jurisdiction2.getName())
                .build();
    }

    /**
     *
     * @param serviceType
     * @return
     */
    public ServiceTypeDto toServiceTypeDto(ServiceType serviceType) {
        return serviceType == null ? null : ServiceTypeDto.serviceTypeDtoWith()
                .name(serviceType.getName())
                .build();
    }

    /**
     *
     * @param applicantType
     * @return
     */
    public ApplicantTypeDto toApplicantTypeDto(ApplicantType applicantType) {
        return applicantType == null ? null : ApplicantTypeDto.applicantTypeDtoWith()
            .name(applicantType.getName())
            .build();
    }

 }
