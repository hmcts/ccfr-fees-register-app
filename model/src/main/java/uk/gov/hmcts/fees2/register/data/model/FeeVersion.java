package uk.gov.hmcts.fees2.register.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by tarun on 30/10/2017.
 */

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "feeVersionWith")
@Table(name = "fee_version")
public class FeeVersion extends AbstractEntity{

    @ManyToOne
    @JoinColumn(name = "fee_id")
    @JsonIgnore
    private Fee2 fee;

    @Column(name = "description")
    private String description;

    @Column(name = "version")
    private int version;

    @Column(name = "status")
    private String status;

    @Column(name = "valid_from")
    private Date validFrom;

    @Column(name = "valid_to")
    private Date validTo;

    @Column(name = "min_range")
    private Long minRange;

    @Column(name = "max_range")
    private Long maxRange;

    @Column(name = "fee_amount")
    private Long feeAmount;

    @Column(name = "min_fee_amount")
    private Long minFeeAmount;

    @Column(name = "max_fee_amount")
    private Long maxFeeAmount;

    @Column(name = "percentage")
    private BigDecimal percentage;

}
