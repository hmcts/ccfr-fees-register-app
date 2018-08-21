package uk.gov.hmcts.fees2.register.data.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "rateable_fee")
public class RateableFee extends FixedFee {
    @Override
    public String getTypeCode() {
        return "rateable";
    }
}
