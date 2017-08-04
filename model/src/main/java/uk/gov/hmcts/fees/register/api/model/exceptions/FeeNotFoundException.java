package uk.gov.hmcts.fees.register.api.model.exceptions;

public class FeeNotFoundException extends ResourceNotFoundException {
    public FeeNotFoundException(String code) {
        super("fee", "code", code);
    }
}
