package uk.gov.hmcts.fees2.register.data.exceptions;

import uk.gov.hmcts.fees.register.api.model.exceptions.ResourceNotFoundException;
import uk.gov.hmcts.fees2.register.data.dto.LookupFeeDto;

public class FeeNotFoundException extends ResourceNotFoundException {

    public FeeNotFoundException(String code) {
        super("fee", "code", code);
    }

    public FeeNotFoundException(LookupFeeDto dto) {
        super("fee", "code", dto.toString());
    }

}
