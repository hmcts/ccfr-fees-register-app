package uk.gov.hmcts.fees2.register.api.contract.request;

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
public abstract class CreateFeeDto {

    @NotNull
    protected String code;

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

    @JsonProperty("unspecified_claim_amount")
    protected Boolean unspecifiedClaimAmount;

}
