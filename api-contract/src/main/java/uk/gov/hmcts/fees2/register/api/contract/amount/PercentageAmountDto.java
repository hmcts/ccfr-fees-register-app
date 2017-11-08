package uk.gov.hmcts.fees2.register.api.contract.amount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PercentageAmountDto {

    private BigDecimal percentage;

}
