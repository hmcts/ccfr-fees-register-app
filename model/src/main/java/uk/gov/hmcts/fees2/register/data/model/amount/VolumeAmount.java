package uk.gov.hmcts.fees2.register.data.model.amount;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
        return null != amountOrVolume ? this.amount.multiply(amountOrVolume, MathContext.DECIMAL32).setScale(2, BigDecimal.ROUND_DOWN) : null;
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
