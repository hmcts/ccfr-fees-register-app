package uk.gov.hmcts.fees2.register.api.controllers.advice.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
