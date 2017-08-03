package uk.gov.hmcts.fees.register.api.model.exceptions;

public class EntityNotFoundException extends RuntimeException {
    private final String code;

    public EntityNotFoundException(String code) {
        this.code = code;
    }
}
