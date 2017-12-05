package uk.gov.hmcts.fees2.register.api.contract.request;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    protected String direction;

    @NotNull
    protected String event;

    protected String memoLine;

    protected String feeOrderName;

    protected String naturalAccountCode;

    protected String statutoryInstrument;

    protected String siRefId;

    protected Boolean unspecifiedClaimAmount;

}
