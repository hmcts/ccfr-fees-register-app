package uk.gov.hmcts.fees2.register.api.contract;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.data.model.*;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "fee2DtoWith")
public class Fee2Dto {

    private String code;

    @JsonProperty("fee_type")
    private String feeType;

    @JsonProperty("channel_type")
    private ChannelType channelTypeDto;

    @JsonProperty("event_type")
    private EventType eventTypeDto;

    @JsonProperty("jurisdiction1")
    private Jurisdiction1 jurisdiction1Dto;

    @JsonProperty("jurisdiction2")
    private Jurisdiction2 jurisdiction2Dto;

    @JsonProperty("service_type")
    private ServiceType serviceTypeDto;

    @JsonProperty("applicant_type")
    private ApplicantType applicantTypeDto;

    @JsonProperty("fee_versions")
    private List<FeeVersionDto> feeVersionDtos;

    @JsonProperty("current_version")
    private FeeVersionDto currentVersion;
    // only ranged fee
    @JsonProperty("min_range")
    private BigDecimal minRange;

    @JsonProperty("max_range")
    private BigDecimal maxRange;

    @JsonProperty("range_unit")
    private String rangeUnit;

    @JsonProperty("unspecified_claim_amount")
    private boolean unspecifiedClaimAmount;
}
