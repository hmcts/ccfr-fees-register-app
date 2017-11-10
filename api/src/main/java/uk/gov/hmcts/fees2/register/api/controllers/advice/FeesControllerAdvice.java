package uk.gov.hmcts.fees2.register.api.controllers.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import uk.gov.hmcts.fees2.register.api.controllers.advice.exception.BadRequestException;
import uk.gov.hmcts.fees2.register.data.exceptions.ReferenceDataNotFoundException;
import uk.gov.hmcts.fees2.register.data.exceptions.TooManyResultsException;

import java.util.Collections;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class FeesControllerAdvice {

    private static final Logger LOG = LoggerFactory.getLogger(FeesControllerAdvice.class);

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<?> badRequest(BadRequestException e){
        LOG.error("Bad request: " + e.getMessage());
        return new ResponseEntity<>(Collections.singletonMap("cause", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ReferenceDataNotFoundException.class})
    public ResponseEntity<?> referenceDataNotFound(ReferenceDataNotFoundException e){
        LOG.error(e.toString());
        return new ResponseEntity<>(Collections.singletonMap("cause", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({TooManyResultsException.class})
    public ResponseEntity<?> tooManyResults(TooManyResultsException e) {
        return new ResponseEntity<Object>(HttpStatus.MULTIPLE_CHOICES);
    }

}
