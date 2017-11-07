package uk.gov.hmcts.fees.register.api.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("fixed")
public class FixedFee extends FeeOld {
    private int amount;

    public FixedFee() {
    }

    @Builder(builderMethodName = "fixedFeeWith")
    public FixedFee(Integer id, String code, String description, int amount) {
        super(id, code, description);
        this.amount = amount;
    }

    @Override
    public int calculate(int value) {
        return amount;
    }
}
