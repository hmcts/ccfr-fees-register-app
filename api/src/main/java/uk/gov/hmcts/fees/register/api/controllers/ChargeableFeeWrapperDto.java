package uk.gov.hmcts.fees.register.api.controllers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.fees.register.legacymodel.Fee;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargeableFeeWrapperDto<F extends Fee> {
    @JsonUnwrapped
    private F fee;

    private int chargeableFee;

}
