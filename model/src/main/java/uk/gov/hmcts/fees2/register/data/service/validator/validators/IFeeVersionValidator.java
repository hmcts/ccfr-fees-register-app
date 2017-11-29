package uk.gov.hmcts.fees2.register.data.service.validator.validators;

import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;

public interface IFeeVersionValidator {

    void onCreate(Fee fee, FeeVersion version);

}
