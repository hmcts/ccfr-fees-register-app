package uk.gov.hmcts.fees.register.api.model;

@FunctionalInterface
public interface Calculateable {
    int calculate(int value);
}
