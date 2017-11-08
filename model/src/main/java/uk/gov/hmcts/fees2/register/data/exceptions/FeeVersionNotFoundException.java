package uk.gov.hmcts.fees2.register.data.exceptions;

import uk.gov.hmcts.fees.register.api.model.exceptions.ResourceNotFoundException;

public class FeeVersionNotFoundException extends ResourceNotFoundException {
    public FeeVersionNotFoundException(String code) {
        super("fee-version", "code", code);
    }
}
