package uk.gov.hmcts.fees2.register.data.exceptions;

import java.io.Serializable;

public class GatewayTimeoutException extends RuntimeException implements Serializable {

    public static final long serialVersionUID = 43287432;

    public GatewayTimeoutException(final String message) {
        super(message);
    }
}
