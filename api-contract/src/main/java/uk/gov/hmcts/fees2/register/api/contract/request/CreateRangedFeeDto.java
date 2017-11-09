package uk.gov.hmcts.fees2.register.api.contract.request;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.PercentageAmountDto;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class CreateRangedFeeDto extends CreateFeeDto{

    public CreateRangedFeeDto(String code, FeeVersionDto version, String jurisdiction1, String jurisdiction2, String service, String channel, String direction, String event, String memoLine, String feeOrderName, String naturalAccountCode, BigDecimal maxRange) {
        super(code, version, jurisdiction1, jurisdiction2, service, channel, direction, event, memoLine, feeOrderName, naturalAccountCode);
        this.maxRange = maxRange;
        this.minRange = minRange;
    }

    private BigDecimal minRange;

    private BigDecimal maxRange;

}
