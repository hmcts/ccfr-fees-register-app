package uk.gov.hmcts.fees2.register.data.service.validator.validators;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.fees2.register.data.exceptions.BadRequestException;
import uk.gov.hmcts.fees2.register.data.model.Fee;

@Component
public class GenericFeeValidator implements IFeeValidator<Fee>{

    private final static String REGEX =  "^[a-zA-Z0-9_-]*$";

    @Override
    public void validateFee(Fee fee) {

        if(fee.getKeyword() != null && !fee.getKeyword().matches(REGEX)) {
            throw new BadRequestException("Keyword must be a combination of only numbers, letters and hyphens");
        }

    }

}
