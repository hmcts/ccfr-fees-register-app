package uk.gov.hmcts.fees2.register.data.exceptions;

import java.io.Serializable;

public class InternalServerException extends RuntimeException implements Serializable {

    public static final long serialVersionUID = 43287432;

    public InternalServerException(String message) {
        super(message);
    }
}