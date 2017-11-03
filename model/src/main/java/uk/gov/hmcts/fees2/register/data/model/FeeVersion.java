package uk.gov.hmcts.fees2.register.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.data.model.amount.Amount;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "feeVersionWith")
@Table(name = "fee_version", uniqueConstraints = {@UniqueConstraint(name = "uk_fee_version", columnNames = {"fee_id", "version"})})
public class FeeVersion extends AbstractEntity{

    @ManyToOne
    @JsonIgnore
    private Fee fee;

    @OneToOne(cascade = CascadeType.ALL)
    private Amount amount;

    @Column(name = "description")
    private String description;

    @Column(name = "version")
    private int version;

    @Column(name = "status")
    private FeeVersionStatus status;

    @Column(name = "valid_from")
    private Date validFrom;

    @Column(name = "valid_to")
    private Date validTo;

}
