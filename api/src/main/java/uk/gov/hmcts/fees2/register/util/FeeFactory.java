package uk.gov.hmcts.fees2.register.util;

import uk.gov.hmcts.fees2.register.api.contract.loader.request.LoaderFixedFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.loader.request.LoaderRelationalFeeDto;
import uk.gov.hmcts.fees2.register.api.contract.request.*;
import uk.gov.hmcts.fees2.register.data.model.*;

public class FeeFactory {

    public static Fee getFee(FeeDto request){
        Fee fee = null;
        if (request instanceof FixedFeeDto || request instanceof LoaderFixedFeeDto)
            fee = new FixedFee();
        if (request instanceof BandedFeeDto)
            fee = new BandedFee();
        if (request instanceof RateableFeeDto)
            fee = new RateableFee();
        if (request instanceof RelationalFeeDto)
            fee = new RelationalFee();
        if (request instanceof RelationalFeeDto || request instanceof LoaderRelationalFeeDto)
            fee = new RelationalFee();
        return fee;

    }
}
