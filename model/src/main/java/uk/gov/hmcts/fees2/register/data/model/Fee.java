package uk.gov.hmcts.fees2.register.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import uk.gov.hmcts.fees2.register.data.service.validator.validators.IFeeValidator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Data
@Entity
@EqualsAndHashCode(callSuper = true)
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

    @Column(name = "statutory_instrument")
    private String statutoryInstrument;

    @Column(name = "si_ref_id")
    private String siRefId;

    @OneToMany(mappedBy = "fee", orphanRemoval = true)
    @Cascade(CascadeType.ALL)
    private List<FeeVersion> feeVersions;

    @Column(name = "unspecified_claim_amount")
    private boolean unspecifiedClaimAmount;

    /* --- */

    @Column(name = "creation_time", nullable = false)
    private Date creationTime;

    @Column(name = "last_updated", nullable = false)
    private Date lastUpdated;

    /* --- */

    public abstract String getTypeCode();

    public abstract List<Class<? extends IFeeValidator>> getValidators();

    public abstract boolean isInRange(BigDecimal amount);

    public FeeVersion getCurrentVersion(boolean isApproved) {
        Optional<FeeVersion> opt = getFeeVersions()
            .stream()
            .filter(v -> (!isApproved || v.getStatus() == FeeVersionStatus.approved) && v.isInRange(new Date()))
            .findFirst();

        return opt.isPresent() ? opt.get() : null;
    }

    /* --- */

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

    /** Added toString method to avoid StackOverFlow error on debugger */
    @Override
    public String toString() {
        return "Fee{" +
            "code='" + code + '\'' +
            ", memoLine='" + memoLine + '\'' +
            ", jurisdiction1=" + jurisdiction1 +
            ", jurisdiction2=" + jurisdiction2 +
            ", eventType=" + eventType +
            ", service=" + service +
            ", directionType=" + directionType +
            ", channelType=" + channelType +
            ", feeOrderName='" + feeOrderName + '\'' +
            ", naturalAccountCode='" + naturalAccountCode + '\'' +
            ", statutoryInstrument='" + statutoryInstrument + '\'' +
            ", siRefId='" + siRefId + '\'' +
            ", feeVersions=" + feeVersions +
            ", unspecifiedClaimAmount=" + unspecifiedClaimAmount +
            ", creationTime=" + creationTime +
            ", lastUpdated=" + lastUpdated +
            '}';
    }
}
