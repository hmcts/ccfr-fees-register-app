package uk.gov.hmcts.fees2.register.data.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "feeWith")
@Table(name = "fee")
public class Fee extends AbstractEntity{

    @Column(name = "code")
    private String code;

    @Column(name = "memo_line")
    private String memoLine;

    @OneToOne
    @JoinColumn(name = "jurisdiction1_id")
    private Jurisdiction1 jurisdiction1;

    @OneToOne
    @JoinColumn(name = "jurisdiction2_id")
    private Jurisdiction2 jurisdiction2;

    @OneToOne
    @JoinColumn(name = "event_type_id")
    private EventType eventType;

    @OneToOne
    @JoinColumn(name = "fee_type_id")
    private FeeType feeType;

    @OneToOne
    @JoinColumn(name = "amount_type_id")
    private AmountType amountType;

    @OneToOne
    @JoinColumn(name = "service_type_id")
    private ServiceType service;

    @OneToOne
    @JoinColumn(name = "direction_type_id")
    private DirectionType directionType;

    @OneToOne
    @JoinColumn(name = "channel_type_id")
    private ChannelType channelType;

    /* --- */

    @Column(name = "creation_time", nullable = false)
    private Date creationTime;

    @Column(name = "last_updated", nullable = false)
    private Date lastUpdated;

    @OneToMany(mappedBy = "fee", orphanRemoval = true)
    @Cascade(CascadeType.ALL)
    @JsonManagedReference
    private List<FeeVersion> feeVersions;

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
