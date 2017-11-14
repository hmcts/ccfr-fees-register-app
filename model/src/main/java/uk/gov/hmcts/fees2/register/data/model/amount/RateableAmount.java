package uk.gov.hmcts.fees2.register.data.model.amount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rateable_amount")
public class RateableAmount extends Amount {

    @Column(name = "min_value")
    private BigDecimal minValue;

    @Column(name = "max_value")
    private BigDecimal maxValue;

    @Column(name = "rateable_value")
    private BigDecimal rateableValue;

    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        return null;
    }

    @Override
    public boolean acceptsUnspecifiedFees() {
        return false;
    }
}
