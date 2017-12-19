package uk.gov.hmcts.fees2.register.data.model.amount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flat_amount")
@EqualsAndHashCode(callSuper = false)
public class FlatAmount extends Amount {

    private BigDecimal amount;

    @Override
    public BigDecimal calculateFee(BigDecimal amountOrVolume) {
        return this.amount;
    }

    @Override
    public boolean acceptsUnspecifiedFees() {
        return true;
    }

}
