package uk.gov.hmcts.fees2.register.data.exceptions;

import uk.gov.hmcts.fees.register.api.model.exceptions.ResourceNotFoundException;

/**
 * ReferenceDataNotFound exception
 *
 */

public class ReferenceDataNotFoundException extends ResourceNotFoundException {

    public ReferenceDataNotFoundException(String msg1, String msg2) {
        super("Reference data", msg1, msg2);
    }

    @Override
    public String toString() {
        return "ReferenceDataNotFoundException {" +
            "resourceName='" + resourceName + '\'' +
            ", idName='" + idName + '\'' +
            ", idValue=" + idValue +
            '}';
    }
}
