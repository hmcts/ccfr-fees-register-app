package uk.gov.hmcts.fees2.register.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ranged_fee")
public class RangedFee extends Fee{

    @Column(name = "min_range")
    private BigDecimal minRange;

    @Column(name = "max_range")
    private BigDecimal maxRange;

    @Override
    public String getTypeCode() {
        return "ranged";
    }

    @Override
    public boolean isInRange(BigDecimal amount) {
        return amount != null &&
            (minRange == null || amount.compareTo(minRange) > 0)
            && (maxRange == null || amount.compareTo(maxRange) <= 0);
    }
}
