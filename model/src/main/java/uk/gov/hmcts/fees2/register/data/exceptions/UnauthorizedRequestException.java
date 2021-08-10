package uk.gov.hmcts.fees2.register.data.exceptions;

public class UnauthorizedRequestException extends RuntimeException {

    public UnauthorizedRequestException(String message) {
        super(message);
    }
}
