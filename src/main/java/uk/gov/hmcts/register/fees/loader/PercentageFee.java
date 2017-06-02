package uk.gov.hmcts.register.fees.loader;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(NON_NULL)
public class PercentageFee extends Fee implements SpecifiedFee {
    @NonNull
    private final BigDecimal percentage;

    @Builder(builderMethodName = "percentageFeeWith")
    public PercentageFee(String id, String description, BigDecimal percentage) {
        super(id, description);
        this.percentage = percentage;
    }

    @Override
    public int calculate(int amount) {
        return new BigDecimal(amount).multiply(percentage).divide(BigDecimal.valueOf(100)).intValue();
    }
}
