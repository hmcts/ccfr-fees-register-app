package uk.gov.hmcts.fees2.register.api.contract;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private ChannelTypeDto channelTypeDto;

    @JsonProperty("event_type")
    private EventTypeDto eventTypeDto;

    @JsonProperty("jurisdiction1")
    private Jurisdiction1Dto jurisdiction1Dto;

    @JsonProperty("jurisdiction2")
    private Jurisdiction2Dto jurisdiction2Dto;

    @JsonProperty("service_type")
    private ServiceTypeDto serviceTypeDto;

    @JsonProperty("applicant_type")
    private ApplicantTypeDto applicantTypeDto;

    @JsonProperty("keyword")
    private String keyword;

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
    private Boolean unspecifiedClaimAmount;

    @JsonProperty("matching_version")
    private FeeVersionDto matchingVersion;

    @JsonProperty("amount_type")
    private String amountType;
}
