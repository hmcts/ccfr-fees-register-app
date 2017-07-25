package uk.gov.hmcts.fees.register.api.controllers;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.fees.register.model.Fee;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargedFeeWrapperDto<F extends Fee> {
    @JsonUnwrapped
    private final F fee;

    private final int chargedFee;

    @JsonCreator
    @Builder(builderMethodName = "calculatedFeeDtoWith")
    public ChargedFeeWrapperDto(@JsonProperty("fee") F fee,
                                @JsonProperty("chargedFee") int chargedFee) {
        this.fee = fee;
        this.chargedFee = chargedFee;
    }
}
