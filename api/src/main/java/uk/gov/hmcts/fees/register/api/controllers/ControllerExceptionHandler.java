package uk.gov.hmcts.fees.register.api.controllers;


import com.google.common.collect.ImmutableMap;
import java.util.Locale;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uk.gov.hmcts.fees.register.api.controllers.fees.FeeTypeUnchangeableException;
import uk.gov.hmcts.fees.register.legacymodel.EntityNotFoundException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity entityNotFoundException() {
        return new ResponseEntity<>(NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError firstFieldError = e.getBindingResult().getFieldErrors().get(0);
        String message = firstFieldError.getField() + ": " + messageSource.getMessage(firstFieldError, Locale.getDefault());
        return new ResponseEntity<>(errorWithMessage(message), BAD_REQUEST);
    }

    @ExceptionHandler(FeeTypeUnchangeableException.class)
    public ResponseEntity<Map> feeTypeUnchangeableException() {
        return new ResponseEntity<>(errorWithMessage("Fee type can't be changed"), BAD_REQUEST);
    }

    private ImmutableMap<String, String> errorWithMessage(String message) {
        return ImmutableMap.of("message", message);
    }


}
