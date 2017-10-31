package uk.gov.hmcts.fees2.register.api.contract;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 *
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Builder(builderMethodName = "feeDtoWith")
public class Fee2Dto {

    private String code;

    private String memoLine;

    private AmountTypeDto amountTypeDto;

    private ChannelTypeDto channelTypeDto;

    private DirectionTypeDto directionTypeDto;

    private EventTypeDto eventTypeDto;

    private FeeTypeDto feeTypeDto;

    private Jurisdiction1Dto jurisdiction1Dto;

    private Jurisdiction2Dto jurisdiction2Dto;

    private ServiceTypeDto serviceTypeDto;

}
