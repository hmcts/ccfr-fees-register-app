package uk.gov.hmcts.fees2.register.data.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "rateable_fee")
public class RateableFee extends FixedFee {
    @Override
    public String getTypeCode() {
        return "rateable";
    }
}
