package uk.gov.hmcts.fees2.register.data.model;

import uk.gov.hmcts.fees2.register.data.service.validator.validators.IFeeValidator;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

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

    @Override
    public List<Class<? extends IFeeValidator>> getValidators() {
        return null;
    }
}
