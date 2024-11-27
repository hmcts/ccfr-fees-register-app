package uk.gov.hmcts.fees2.register.api.contract.amount;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PercentageAmountDto {

    @JsonFormat(shape = JsonFormat.Shape.NUMBER_INT)
    private BigDecimal percentage;

}
