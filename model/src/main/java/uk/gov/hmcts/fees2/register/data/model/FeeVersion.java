package uk.gov.hmcts.fees2.register.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.data.model.amount.Amount;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "feeVersionWith")
@Table(name = "fee_version", uniqueConstraints = {@UniqueConstraint(name = "uk_fee_version", columnNames = {"fee_id", "version"})})
@EqualsAndHashCode(callSuper = false)
public class FeeVersion extends AbstractEntity{

    @ManyToOne
    @JsonIgnore
    private Fee fee;

    @OneToOne(cascade = CascadeType.ALL)
    private Amount amount;

    @Column(name = "description")
    private String description;

    @Column(name = "version")
    private Integer version;

    @Column(name = "status")
    private FeeVersionStatus status;

    @Column(name = "valid_from")
    private Date validFrom;

    @Column(name = "valid_to")
    private Date validTo;

    @Column(name = "author")
    private String author;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "memo_line")
    private String memoLine;

    @Column(name="fee_order_name")
    private String lastAmendingSi;

    @Column(name="consolidated_fee_order_name")
    private String consolidatedFeeOrderName;

    @Column(name = "natural_account_code")
    private String naturalAccountCode;

    @Column(name = "statutory_instrument")
    private String statutoryInstrument;

    @Column(name = "si_ref_id")
    private String siRefId;

    @Column(name = "reason_for_update")
    private String reasonForUpdate;

    @Column(name = "reason_for_reject")
    private String reasonForReject;

    @ManyToOne
    @JoinColumn(name = "direction_type")
    private DirectionType directionType;

    @Column(name = "approved_date")
    private Date approvedDate;

    public boolean isInRange(Date date) {
        return (validFrom == null || date.compareTo(validFrom) >= 0)
            && (validTo == null || date.compareTo(validTo) < 0);
    }

    public BigDecimal calculateFee(BigDecimal amount) {
        return this.amount.calculateFee(amount);
    }

    @Override
    public String toString() {
        return "FeeVersion{" +
            "fee=" + fee +
            ", amount=" + amount +
            ", description='" + description + '\'' +
            ", version=" + version +
            ", status=" + status +
            ", validFrom=" + validFrom +
            ", validTo=" + validTo +
            ", author='" + author + '\'' +
            ", approvedBy='" + approvedBy + '\'' +
            ", memoLine='" + memoLine + '\'' +
            ", lastAmendingSi='" + lastAmendingSi + '\'' +
            ", consolidateFeeOrderName='" + consolidatedFeeOrderName + '\'' +
            ", naturalAccountCode='" + naturalAccountCode + '\'' +
            ", statutoryInstrument='" + statutoryInstrument + '\'' +
            ", siRefId='" + siRefId + '\'' +
            ", reasonForUpdate='" + reasonForUpdate + '\'' +
            ", reasonForReject='" + reasonForReject + '\'' +
            ", approvedDate='" + approvedDate + '\'' +
            '}';
    }
}
