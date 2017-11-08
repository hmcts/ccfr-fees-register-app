package uk.gov.hmcts.fees2.register.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "fee_type")
@Table(name = "fee")
public abstract class Fee extends AbstractEntity{

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "memo_line")
    private String memoLine;

    @ManyToOne
    @JoinColumn(name = "jurisdiction1")
    private Jurisdiction1 jurisdiction1;

    @ManyToOne
    @JoinColumn(name = "jurisdiction2")
    private Jurisdiction2 jurisdiction2;

    @ManyToOne
    @JoinColumn(name = "event_type")
    private EventType eventType;

    @ManyToOne
    @JoinColumn(name = "service")
    private ServiceType service;

    @ManyToOne
    @JoinColumn(name = "direction_type")
    private DirectionType directionType;

    @ManyToOne
    @JoinColumn(name = "channel_type")
    private ChannelType channelType;

    @Column(name="fee_order_name")
    private String feeOrderName;

    @Column(name = "natural_account_code")
    private String naturalAccountCode;

    @OneToMany(mappedBy = "fee", orphanRemoval = true)
    @Cascade(CascadeType.ALL)
    private List<FeeVersion> feeVersions;

    /* --- */

    public abstract boolean isInRange(BigDecimal amount);

    private transient FeeVersion currentVersion = null;

    public FeeVersion getCurrentVersion() {

        if(currentVersion != null) {
            return currentVersion;
        }

        final Date date = new Date();

        return getFeeVersions()
            .stream()
            .filter(v -> v.getStatus() == FeeVersionStatus.approved && v.isInRange(date))
            .findFirst().get();
    }

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
