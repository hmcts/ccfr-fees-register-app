package uk.gov.hmcts.fees2.register.data.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import uk.gov.hmcts.fees2.register.data.service.validator.validators.GenericFeeValidator;
import uk.gov.hmcts.fees2.register.data.service.validator.validators.IFeeValidator;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "fixed_fee")
public class FixedFee extends Fee {

    private final static List<Class<? extends IFeeValidator>> VALIDATORS = Arrays.asList(GenericFeeValidator.class);

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
        return VALIDATORS;
    }

    @Override
    public String toString() {
        return "FixedFee{}";
    }

}
