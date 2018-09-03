package uk.gov.hmcts.fees2.register.data.service.validator.validators;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees2.register.data.exceptions.BadRequestException;
import uk.gov.hmcts.fees2.register.data.model.Fee;

@Component
public class GenericFeeValidator implements IFeeValidator<Fee>{

    @Override
    public void validateFee(Fee fee) {

        if(fee.getKeyword() != null && fee.getKeyword().contains(" ")) {
            throw new BadRequestException("Keyword cannot contain blank spaces");
        }

    }

}
