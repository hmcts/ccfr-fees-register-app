package uk.gov.hmcts.fees2.register.api.contract.request;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.api.contract.FeeVersionDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.FlatAmountDto;
import uk.gov.hmcts.fees2.register.api.contract.amount.PercentageAmountDto;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class RangedFeeDto {

    //private Long id; // Cannot be specified

    private BigDecimal minRange;

    private BigDecimal maxRange;

    private String code;

    private FeeVersionDto version;

    private String jurisdiction1;

    private String jurisdiction2;

    private String service;

    private String channel;

    private String event;

    private String memoLine;

    // private String feeOrderName; Whats this ??

    // private String naturalAccountCode; Whats this ??

}
