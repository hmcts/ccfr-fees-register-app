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
import java.util.stream.Collectors;

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

    @Column(name = "fee_number", unique = true)
    private Integer feeNumber;

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
    @JoinColumn(name = "channel_type")
    private ChannelType channelType;

    @ManyToOne
    @JoinColumn(name = "application_type")
    private ApplicantType applicantType;

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

    @Column(name = "keyword")
    private String keyword;

    /* --- */

    @Transient
    public String getFeeType() {
        return this.getClass().getAnnotation(DiscriminatorValue.class).value();
    }

    public abstract String getTypeCode();

    public abstract List<Class<? extends IFeeValidator>> getValidators();

    public abstract boolean isInRange(BigDecimal amount);

    public boolean isDraft() {
        List<FeeVersion> feeVersions = getFeeVersions()
            .stream()
            .filter(v -> !v.getStatus().equals(FeeVersionStatus.draft))
            .collect(Collectors.toList());

        return feeVersions.isEmpty();
    }

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
            ", jurisdiction1=" + jurisdiction1 +
            ", jurisdiction2=" + jurisdiction2 +
            ", eventType=" + eventType +
            ", service=" + service +
            ", channelType=" + channelType +
            ", applicantType=" + applicantType +
            ", feeVersions=" + feeVersions +
            ", unspecifiedClaimAmount=" + unspecifiedClaimAmount +
            ", creationTime=" + creationTime +
            ", lastUpdated=" + lastUpdated +
            '}';
    }

    @OneToMany
    @Cascade(CascadeType.ALL)
    @JoinColumn(name = "fee_id", referencedColumnName = "id", nullable = false)
    private List<FeeCodeHistory> feeCodeHistories;

}
