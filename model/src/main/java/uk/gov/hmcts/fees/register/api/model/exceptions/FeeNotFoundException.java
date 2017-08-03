package uk.gov.hmcts.fees.register.api.model.exceptions;

public class FeeNotFoundException extends EntityNotFoundException {
    public FeeNotFoundException(String code) {
        super(code);
    }
}
