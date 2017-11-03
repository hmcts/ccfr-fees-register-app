package uk.gov.hmcts.fees2.register.data.exceptions;

import uk.gov.hmcts.fees.register.api.model.exceptions.ResourceNotFoundException;

public class FeeNotFoundException extends ResourceNotFoundException {

    public FeeNotFoundException(String code) {
        super("fee", "code", code);
    }
}
