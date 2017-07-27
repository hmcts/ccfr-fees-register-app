package uk.gov.hmcts.fees.register.legacymodel;

@FunctionalInterface
public interface CalculateableFee {
    int calculate(int value);
}
