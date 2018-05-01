package uk.gov.hmcts.fees2.register.data.model.amount;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.data.model.AbstractEntity;

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
@EqualsAndHashCode(callSuper = false)
public abstract class Amount extends AbstractEntity{

    @Column(name = "creation_time", nullable = false)
    @JsonIgnore
    private Date creationTime;

    @Column(name = "last_updated", nullable = false)
    @JsonIgnore
    private Date lastUpdated;

    public abstract BigDecimal calculateFee(BigDecimal amountOrVolume);

    public abstract boolean acceptsUnspecifiedFees();

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

    public abstract void setAmountValue(BigDecimal amount);
}
