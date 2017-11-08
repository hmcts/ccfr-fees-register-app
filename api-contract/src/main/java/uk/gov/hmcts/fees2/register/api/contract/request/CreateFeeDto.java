package uk.gov.hmcts.fees2.register.api.contract.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class CreateFeeDto {

    private String code;

    private FeeVersionDto version;

    private String jurisdiction1;

    private String jurisdiction2;

    private String service;

    private String channel;

    private String direction;

    private String event;

    private String memoLine;

    private String feeOrderName;

    private String naturalAccountCode;

}
