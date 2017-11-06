package uk.gov.hmcts.fees2.register.data.model;

import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
public class FixedFee extends Fee{

    @Override
    public boolean isInRange(BigDecimal amount) {
        return true;
    }
}
