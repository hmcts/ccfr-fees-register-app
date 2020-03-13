package uk.gov.hmcts.fees.register.api.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Data
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("fixed")
public class FixedFeeOld extends FeeOld {
    private int amount;

    public FixedFeeOld() {
    }

    @Builder(builderMethodName = "fixedFeeWith")
    public FixedFeeOld(Integer id, String code, String description, int amount) {
        super(id, code, description);
        this.amount = amount;
    }

    @Override
    public int calculate(int value) {
        return amount;
    }
}
