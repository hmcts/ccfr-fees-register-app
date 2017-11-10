package uk.gov.hmcts.fees2.register.api.contract.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;

@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public abstract class CreateFeeDto {

    protected String code;

    protected FeeVersionDto version;

    protected String jurisdiction1;

    protected String jurisdiction2;

    protected String service;

    protected String channel;

    protected String direction;

    protected String event;

    protected String memoLine;

    protected String feeOrderName;

    protected String naturalAccountCode;

}
