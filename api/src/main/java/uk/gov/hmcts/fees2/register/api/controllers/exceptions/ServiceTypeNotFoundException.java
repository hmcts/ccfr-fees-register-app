package uk.gov.hmcts.fees2.register.api.controllers.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ServiceTypeNotFoundException extends RuntimeException {
}
