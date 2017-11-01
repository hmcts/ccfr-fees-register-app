package uk.gov.hmcts.fees2.register.api.contract.request;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.PercentageAmountDto;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class CreateRangedFeeRequest {

    private String code;

    private String memoLine;

    private String channelType;

    private String directionType;

    private String eventType;

    private String feeType;

    private String jurisdiction1;

    private String jurisdiction2;

    private String serviceType;

    private BigDecimal fromAmount;

    private BigDecimal toAmount;

    private FlatAmountDto flatAmount;

    private PercentageAmountDto percentageAmount;

}
