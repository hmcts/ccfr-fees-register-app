package uk.gov.hmcts.fees.register.legacymodel;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
