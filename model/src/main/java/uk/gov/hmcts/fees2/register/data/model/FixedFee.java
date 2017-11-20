package uk.gov.hmcts.fees2.register.data.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "fixed_fee")
public class FixedFee extends Fee{

    @Override
    public String getTypeCode() {
        return "fixed";
    }

    @Override
    public boolean isInRange(BigDecimal amount) {
        return true;
    }
}
