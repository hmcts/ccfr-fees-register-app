package uk.gov.hmcts.fees2.register.data.exceptions;

import java.io.Serializable;

public class UserNotFoundException extends RuntimeException implements Serializable {

    public static final long serialVersionUID = 43287431;

    public UserNotFoundException(final String message) {
        super(message);
    }

}
