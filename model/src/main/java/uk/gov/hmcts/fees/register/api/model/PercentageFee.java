package uk.gov.hmcts.fees.register.api.model;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@DiscriminatorValue("percentage")
public class PercentageFee extends FeeOld {
    @NonNull
    private BigDecimal percentage;

    public PercentageFee() {
    }

    @Builder(builderMethodName = "percentageFeeWith")
    public PercentageFee(Integer id, String code, String description, BigDecimal percentage) {
        super(id, code, description);
        this.percentage = percentage;
    }

    @Override
    public int calculate(int value) {
        return new BigDecimal(value).multiply(percentage).divide(BigDecimal.valueOf(100)).intValue();
    }
}
