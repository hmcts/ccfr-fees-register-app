package uk.gov.hmcts.fees2.register.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees2.register.data.service.validator.validators.GenericFeeValidator;
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
        if(!super.isADuplicateOf(anotherFee) || ! (anotherFee instanceof RangedFee)) {
            return false;
        }

        RangedFee anotherRangedFee = (RangedFee) anotherFee;

        return (anotherRangedFee.minRange != null && minRange != null
            && anotherRangedFee.maxRange == null && maxRange == null)
            ||
            (anotherRangedFee.maxRange != null && maxRange != null
                && anotherRangedFee.minRange == null && minRange == null)
            ||
            (anotherRangedFee.maxRange != null && maxRange != null
                && anotherRangedFee.minRange != null && minRange != null
            &&
                anotherRangedFee.isInRange(minRange) || anotherRangedFee.isInRange(maxRange));
    }

    private final static List<Class<? extends IFeeValidator>> VALIDATORS = Arrays.asList(RangedFeeValidator.class, GenericFeeValidator.class);

    @Override
    public List<Class<? extends IFeeValidator>> getValidators() {
        return VALIDATORS;
    }

}
