package uk.gov.hmcts.fees2.register.data.model.amount;

import jakarta.persistence.Column;
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
@Table(name = "rateable_amount")
@EqualsAndHashCode(callSuper = false)
public class RateableAmount extends Amount {

    @Column(name = "min_value")
    private BigDecimal minValue;

    @Column(name = "max_value")
    private BigDecimal maxValue;

    @Column(name = "rateable_value")
    private BigDecimal rateableValue;

    @Override
    public BigDecimal calculateFee(BigDecimal amountOrVolume) {
        return null;
    }

    @Override
    public boolean acceptsUnspecifiedFees() {
        return false;
    }

    @Override
    public void setAmountValue(BigDecimal amount) {

    }
}
