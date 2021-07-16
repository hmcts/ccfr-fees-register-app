package uk.gov.hmcts.fees2.register.api.controllers.exceptions;

public class FeesException extends RuntimeException {

    public FeesException(final Throwable cause) {
        super(cause);
    }
}
