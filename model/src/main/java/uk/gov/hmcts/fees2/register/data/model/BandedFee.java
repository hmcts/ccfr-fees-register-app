package uk.gov.hmcts.fees2.register.data.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "banded_fee")
public class BandedFee extends FixedFee {
    @Override
    public String getTypeCode() {
        return "banded";
    }
}
