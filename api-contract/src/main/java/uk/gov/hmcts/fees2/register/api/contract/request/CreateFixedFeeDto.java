package uk.gov.hmcts.fees2.register.api.contract.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class CreateFixedFeeDto extends CreateFeeDto{

    public CreateFixedFeeDto(String code, FeeVersionDto version, String jurisdiction1, String jurisdiction2, String service, String channel, String direction, String event, String memoLine, String feeOrderName, String naturalAccountCode) {
        super(code, version, jurisdiction1, jurisdiction2, service, channel, direction, event, memoLine, feeOrderName, naturalAccountCode);
    }
}
