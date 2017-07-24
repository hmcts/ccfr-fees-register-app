package uk.gov.hmcts.fees.register.model;

@FunctionalInterface
public interface CalculatedFee {
    int calculate(int value);
}
