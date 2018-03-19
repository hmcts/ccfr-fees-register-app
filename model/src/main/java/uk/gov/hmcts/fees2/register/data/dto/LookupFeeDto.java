package uk.gov.hmcts.fees2.register.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import uk.gov.hmcts.fees2.register.data.model.FeeVersionStatus;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LookupFeeDto {

    private String service;

    private String jurisdiction1;

    private String jurisdiction2;

    private String channel;

    private String event;

    //private String direction;

    private String applicantType;

    private BigDecimal amountOrVolume;

    @JsonProperty("unspecified_claim_amount")
    private Boolean unspecifiedClaimAmount;

    @JsonProperty("version_status")
    private FeeVersionStatus versionStatus;

    private String author;
}
