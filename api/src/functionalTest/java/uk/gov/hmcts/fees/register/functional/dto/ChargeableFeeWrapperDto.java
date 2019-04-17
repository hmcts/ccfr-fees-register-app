package uk.gov.hmcts.fees.register.functional.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Builder;
import lombok.Data;
import uk.gov.hmcts.fees.register.api.contract.FeeDto;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChargeableFeeWrapperDto<F extends FeeDto> {
    @JsonUnwrapped
    private final F fee;

    private final int chargeableFee;

    @JsonCreator
    @Builder(builderMethodName = "chargeableFeeDtoWith")
    public ChargeableFeeWrapperDto(@JsonProperty("fee") F fee,
                                   @JsonProperty("chargeableFee") int chargeableFee) {
        this.fee = fee;
        this.chargeableFee = chargeableFee;
    }
}
