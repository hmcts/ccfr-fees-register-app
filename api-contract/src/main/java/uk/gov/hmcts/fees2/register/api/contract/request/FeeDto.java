package uk.gov.hmcts.fees2.register.api.contract.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;

import javax.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public abstract class FeeDto {

    @JsonIgnore
    protected String code;

    @JsonIgnore
    protected String newCode;

    @NotNull
    protected FeeVersionDto version;

    @NotNull
    protected String jurisdiction1;

    @NotNull
    protected String jurisdiction2;

    @NotNull
    protected String service;

    protected String channel;

    @NotNull
    protected String event;

    @JsonProperty("applicant_type")
    protected String applicantType;

    @JsonProperty("unspecified_claim_amount")
    protected Boolean unspecifiedClaimAmount;

}
