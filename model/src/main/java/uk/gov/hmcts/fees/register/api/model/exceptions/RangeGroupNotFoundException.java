package uk.gov.hmcts.fees.register.api.model.exceptions;

public class RangeGroupNotFoundException extends ResourceNotFoundException {
    public RangeGroupNotFoundException(String code) {
        super("range", "code", code);
    }
}
