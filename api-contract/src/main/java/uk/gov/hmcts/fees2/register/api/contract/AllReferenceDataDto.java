package uk.gov.hmcts.fees2.register.api.contract;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class AllReferenceDataDto {

    private List<ChannelTypeDto> channelTypes;

    private List<DirectionTypeDto> directionTypes;

    private List<EventTypeDto> eventTypes;

    private List<ServiceTypeDto> serviceTypes;

    private List<Jurisdiction1Dto> jurisdictions1;

    private List<Jurisdiction2Dto> jurisdictions2;

    private List<RangeUnitDto> rangeUnits;

    private List<ApplicantTypeDto> applicantTypes;
}
