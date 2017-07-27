package uk.gov.hmcts.fees.register.api.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("fixed")
public class FixedFee extends Fee {
    private int amount;

    public FixedFee() {
    }

    public FixedFee(Integer id, String code, String description, int amount) {
        super(id, code, description);
        this.amount = amount;
    }
}
