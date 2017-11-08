package uk.gov.hmcts.fees2.register.data.model.amount;

import lombok.*;
import uk.gov.hmcts.fees2.register.data.model.AbstractEntity;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * An entity class which contains the information of a Amount
 *
 */

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "amount")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "amount_type")
public abstract class Amount extends AbstractEntity{

    public abstract BigDecimal calculateFee(BigDecimal amount);

    @Column(name = "creation_time", nullable = false)
    private Date creationTime;

    @Column(name = "last_updated", nullable = false)
    private Date lastUpdated;

    @PreUpdate
    public void preUpdate() {
        Date now = new Date();
        lastUpdated = now;
    }

    @PrePersist
    public void prePersist() {
        Date now = new Date();
        creationTime = now;
        lastUpdated = now;
    }
}
