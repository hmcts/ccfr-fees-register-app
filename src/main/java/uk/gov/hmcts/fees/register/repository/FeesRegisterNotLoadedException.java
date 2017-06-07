package uk.gov.hmcts.fees.register.repository;

public class FeesRegisterNotLoadedException extends RuntimeException {

    public FeesRegisterNotLoadedException(String message, Throwable cause) {
        super(message, cause);
    }

}
