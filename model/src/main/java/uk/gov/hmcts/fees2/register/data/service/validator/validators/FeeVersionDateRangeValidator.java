package uk.gov.hmcts.fees2.register.data.service.validator.validators;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees2.register.data.exceptions.BadRequestException;
import uk.gov.hmcts.fees2.register.data.model.Fee;
import uk.gov.hmcts.fees2.register.data.model.FeeVersion;

@Component
public class FeeVersionDateRangeValidator implements IFeeVersionValidator{

    @Override
    public void onCreate(Fee fee, FeeVersion v) {

        if (fee.isUnspecifiedClaimAmount() && !v.getAmount().acceptsUnspecifiedFees()) {
            throw new BadRequestException(
                "Amount type " + v.getAmount().getClass().getSimpleName()
                    + " is not allowed with unspecified amount fees");
        }

        if (v.getValidFrom() != null && v.getValidTo() != null && v.getValidFrom().compareTo(v.getValidTo()) >= 0) {
            throw new BadRequestException("Fee version valid from must be lower than valid to");
        }

    }

}
