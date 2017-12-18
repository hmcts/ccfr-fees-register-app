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

    @JsonProperty("memo_line")
    private String memoLine;

    @JsonProperty("channel_type")
    private ChannelType channelTypeDto;

    @JsonProperty("direction_type")
    private DirectionType directionTypeDto;

    @JsonProperty("event_type")
    private EventType eventTypeDto;

    @JsonProperty("jurisdiction1")
    private Jurisdiction1 jurisdiction1Dto;

    @JsonProperty("jurisdiction2")
    private Jurisdiction2 jurisdiction2Dto;

    @JsonProperty("service_type")
    private ServiceType serviceTypeDto;

    @JsonProperty("natural_account_code")
    private String naturalAccountCode;

    @JsonProperty("fee_order_name")
    private String feeOrderName;

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

    @JsonProperty("statutory_instrument")
    private String statutoryInstrument;

    @JsonProperty("si_ref_id")
    private String siRefId;

}
