package uk.gov.hmcts.fees2.register.data.model.amount;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "percentage_amount")
@EqualsAndHashCode(callSuper = false)
public class PercentageAmount extends Amount{

    private final static BigDecimal HUNDRED = new BigDecimal(100);

    private BigDecimal percentage;

    @Override
    public BigDecimal calculateFee(BigDecimal amountOrVolume) {
        return amountOrVolume.multiply(
            percentage.divide(HUNDRED, MathContext.DECIMAL32)
        ).setScale(2, RoundingMode.DOWN);
    }

    @Override
    public boolean acceptsUnspecifiedFees() {
        return false;
    }

    @Override
    public void setAmountValue(BigDecimal percentage) {
        this.percentage = percentage;
    }
}
