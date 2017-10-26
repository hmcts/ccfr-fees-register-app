package uk.gov.hmcts.fees2.register.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "feeWith")
@Table(name = "fee")
public class Fee extends AbstractEntity{

    private String code;

    @ManyToOne
    @JoinColumn(name = "jurisdiction1")
    private Jurisdiction1 jurisdiction1;

    @ManyToOne
    private Jurisdiction2 jurisdiction2;

    @ManyToOne
    private EventType event;

    @ManyToOne
    @JoinColumn(name = "fee_type")
    private FeeType feeType;

    @ManyToOne
    @JoinColumn(name = "amount_type")
    private AmountType amountType;

    @ManyToOne
    private ServiceType service;

    @Column(length = 4000)
    private String description;

    @Column(length = 4000)
    private String memo;

    /* --- */

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
