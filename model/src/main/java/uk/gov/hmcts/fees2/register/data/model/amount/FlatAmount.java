package uk.gov.hmcts.fees2.register.data.model.amount;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.data.model.Unit;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "flat_amount")
public class FlatAmount extends Amount {

    private BigDecimal amount;

    @Override
    public BigDecimal calculateFee(BigDecimal amount) {
        return this.amount;
    }
}
