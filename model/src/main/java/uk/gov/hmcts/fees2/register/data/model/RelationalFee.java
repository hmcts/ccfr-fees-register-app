package uk.gov.hmcts.fees2.register.data.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "relational_fee")
public class RelationalFee extends FixedFee {
    @Override
    public String getTypeCode() {
        return "relational";
    }
}
