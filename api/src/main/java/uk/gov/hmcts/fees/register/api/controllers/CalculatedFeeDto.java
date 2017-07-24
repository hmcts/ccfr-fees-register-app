package uk.gov.hmcts.fees.register.api.controllers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.fees.register.model.Fee;

@Data
public class CalculatedFeeDto<F extends Fee> {
    @JsonUnwrapped
    private final F fee;

    private final int calculatedAmount;

    @JsonCreator
    @Builder(builderMethodName = "calculatedFeeDtoWith")
    public CalculatedFeeDto(@JsonProperty("fee") F fee,
                            @JsonProperty("calculatedAmount") int calculatedAmount) {
        this.fee = fee;
        this.calculatedAmount = calculatedAmount;
    }
}
