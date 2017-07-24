package uk.gov.hmcts.fees.register.model;

@FunctionalInterface
public interface CalculateableFee {
    int calculate(int value);
}
