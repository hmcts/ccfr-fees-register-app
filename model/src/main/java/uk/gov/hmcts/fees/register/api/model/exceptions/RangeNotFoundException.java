package uk.gov.hmcts.fees.register.api.model.exceptions;

public class RangeNotFoundException extends ResourceNotFoundException {
    public RangeNotFoundException(Integer value) {
        super("range", "value", value);
    }
}
