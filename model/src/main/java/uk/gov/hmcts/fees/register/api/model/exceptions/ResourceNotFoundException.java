package uk.gov.hmcts.fees.register.api.model.exceptions;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    protected final String resourceName;
    protected final String idName;
    protected final Object idValue;

    public ResourceNotFoundException(String resourceName, String idName, Object idValue) {
        this.resourceName = resourceName;
        this.idName = idName;
        this.idValue = idValue;
    }
}
