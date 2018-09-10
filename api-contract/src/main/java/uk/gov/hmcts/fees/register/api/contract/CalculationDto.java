package uk.gov.hmcts.fees.register.api.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculationDto {
    private int amount;
    private FeeDto fee;
}
