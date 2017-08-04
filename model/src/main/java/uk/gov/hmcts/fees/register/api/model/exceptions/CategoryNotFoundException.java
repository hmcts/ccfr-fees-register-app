package uk.gov.hmcts.fees.register.api.model.exceptions;

public class CategoryNotFoundException extends ResourceNotFoundException {
    public CategoryNotFoundException(String code) {
        super("category", "code", code);
    }
}
