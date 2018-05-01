package uk.gov.hmcts.fees2.register.data.model.amount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.MathContext;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "volume_amount")
@EqualsAndHashCode(callSuper = false)
public class VolumeAmount extends Amount {

    private BigDecimal amount;

    @Override
    public BigDecimal calculateFee(BigDecimal amountOrVolume) {
        return this.amount.multiply(amountOrVolume, MathContext.DECIMAL32).setScale(2, BigDecimal.ROUND_DOWN);
    }

    @Override
    public boolean acceptsUnspecifiedFees() {
        return false;
    }

    @Override
    public void setAmountValue(BigDecimal amount) {
        this.amount  = amount;
    }
}
