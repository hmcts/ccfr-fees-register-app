package uk.gov.hmcts.fees2.register.data.model;


import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "banded_fee")
public class BandedFee extends FixedFee {
    @Override
    public String getTypeCode() {
        return "banded";
    }
}
