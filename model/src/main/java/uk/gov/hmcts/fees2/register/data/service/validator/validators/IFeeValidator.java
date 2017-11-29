package uk.gov.hmcts.fees2.register.data.service.validator.validators;

import uk.gov.hmcts.fees2.register.data.model.Fee;

public interface IFeeValidator<T extends Fee> {

    void validateFee(T fee);

}
