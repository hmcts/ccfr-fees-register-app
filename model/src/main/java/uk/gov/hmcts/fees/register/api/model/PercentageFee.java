package uk.gov.hmcts.fees.register.api.model;

import java.math.BigDecimal;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@DiscriminatorValue("percentage")
public class PercentageFee extends Fee {
    @NonNull
    private BigDecimal percentage;

    public PercentageFee() {
    }

    public PercentageFee(Integer id, String code, String description, BigDecimal percentage) {
        super(id, code, description);
        this.percentage = percentage;
    }
}
