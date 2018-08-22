package uk.gov.hmcts.fees2.register.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.data.service.validator.validators.IFeeValidator;
import uk.gov.hmcts.fees2.register.data.service.validator.validators.RangedFeeValidator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ranged_fee")
public class RangedFee extends Fee{

    @Column(name = "min_range")
    private BigDecimal minRange;

    @Column(name = "max_range")
    private BigDecimal maxRange;

    @ManyToOne
    @JoinColumn(name = "range_unit")
    private RangeUnit rangeUnit;

    @Override
    public String getTypeCode() {
        return "ranged";
    }

    @Override
    public boolean isInRange(BigDecimal amount) {
        return amount != null &&
            (minRange == null || amount.compareTo(minRange) >= 0)
            && (maxRange == null || amount.compareTo(maxRange) <= 0);
    }

    @Override
    public boolean isADuplicateOf(Fee anotherFee) {

        return false;

//        if(!super.isADuplicateOf(anotherFee)) {
//            return false;
//        }
//
//        RangedFee anotherRangedFee = (RangedFee) anotherFee;
//
//        return anotherRangedFee.minRange != null && maxRange != null && anotherRangedFee.minRange.compareTo(maxRange) >= 0 ||
//            anotherRangedFee.maxRange != null && minRange != null && anotherRangedFee.maxRange.compareTo(minRange) <= 0;
    }

    /* KISS for now */
    private final static List<Class<? extends IFeeValidator>> VALIDATORS = Arrays.asList(RangedFeeValidator.class);

    @Override
    public List<Class<? extends IFeeValidator>> getValidators() {
        return VALIDATORS;
    }

}
