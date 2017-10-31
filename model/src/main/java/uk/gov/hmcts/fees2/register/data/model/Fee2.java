package uk.gov.hmcts.fees2.register.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fee")
public class Fee2 extends AbstractEntity{

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "memo_line")
    private String memoLine;

    @ManyToOne
    @JoinColumn(name = "jurisdiction1_id")
    private Jurisdiction1 jurisdiction1;

    @ManyToOne
    @JoinColumn(name = "jurisdiction2_id")
    private Jurisdiction2 jurisdiction2;

    @ManyToOne
    @JoinColumn(name = "event_type_id")
    private EventType eventType;

    @ManyToOne
    @JoinColumn(name = "fee_type_id")
    private FeeType feeType;

    @ManyToOne
    @JoinColumn(name = "amount_type_id")
    private AmountType amountType;

    @ManyToOne
    @JoinColumn(name = "service_type_id")
    private ServiceType service;

    @ManyToOne
    @JoinColumn(name = "direction_type_id")
    private DirectionType directionType;

    @ManyToOne
    @JoinColumn(name = "channel_type_id")
    private ChannelType channelType;

    @OneToMany(mappedBy = "fee", orphanRemoval = true)
    @Cascade(CascadeType.ALL)
    private List<FeeVersion> feeVersions;

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
