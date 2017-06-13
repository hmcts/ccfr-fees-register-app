package uk.gov.hmcts.fees.register.model;

@FunctionalInterface
public interface SpecifiedFee {
    int calculate(int amount);
}
