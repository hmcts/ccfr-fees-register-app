package uk.gov.hmcts.fees.register.api.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uk.gov.hmcts.fees.register.legacymodel.EntityNotFoundException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<HttpStatus> handleEntityNotFoundException() {
        return new ResponseEntity<>(NOT_FOUND);
    }
}
