package uk.gov.hmcts.fees2.register.data.service.validator.validators;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees2.register.data.exceptions.BadRequestException;
import uk.gov.hmcts.fees2.register.data.model.RangedFee;

@Component
public class RangedFeeValidator implements IFeeValidator<RangedFee> {

    @Override
    public void validateFee(RangedFee fee) {

        if (fee.isUnspecifiedClaimAmount()) {
            throw new BadRequestException("Ranged fees can not have unspecified claim amounts");
        }

        if (fee.getMinRange() != null && fee.getMaxRange() != null && fee.getMinRange().compareTo(fee.getMaxRange()) >= 0) {
            throw new BadRequestException("Ranged fee min range can not be greater or equal than the max range");
        }
    }

}
