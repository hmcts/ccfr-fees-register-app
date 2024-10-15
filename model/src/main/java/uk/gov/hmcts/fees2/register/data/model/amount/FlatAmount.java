package uk.gov.hmcts.fees2.register.data.model.amount;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

    @Override
    public void setAmountValue(BigDecimal amount) {
        this.amount = amount;
    }

}
