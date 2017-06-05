package uk.gov.hmcts.register.fees.service;

import org.springframework.stereotype.Component;
import uk.gov.hmcts.register.fees.model.Fee;
import uk.gov.hmcts.register.fees.model.FixedFee;
import uk.gov.hmcts.register.fees.model.PercentageFee;

/**
 * Created by sacs on 05/06/2017.
 */
@Component
public class FeesDtoFactory {


    public FeesDto toFeeDto(Fee fee, int claimAmount) {

        FeesDto feeDto =null;

        if (fee instanceof PercentageFee) {
            PercentageFee percentageFee = (PercentageFee) fee;

            feeDto = FeesDto.feeDtoWith()
                    .id(percentageFee.getId().toString())
                    .amount(claimAmount)
                    .description(percentageFee.getDescription())
                    .feeAmount(percentageFee.calculate(claimAmount))
                    .percentage(percentageFee.getPercentage())
                    .build();
        }


        if (fee instanceof FixedFee) {

            FixedFee fixedFee = (FixedFee) fee;

            feeDto = FeesDto.feeDtoWith()
                    .id(fixedFee.getId().toString())
                    .amount(claimAmount)
                    .description(fixedFee.getDescription())
                    .feeAmount(fixedFee.getAmount())
                    .build();


        }

        return feeDto;
    }
}