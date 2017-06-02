package uk.gov.hmcts.register.fees.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Range {
    private final int startAmount;
    private final int uptoAmount;
    private final Fee fee;

    @JsonCreator
    public Range(@JsonProperty("startAmount") int startAmount,
                 @JsonProperty("uptoAmount") int uptoAmount,
                 @JsonProperty("fee") Fee fee) {
        this.startAmount = startAmount;
        this.uptoAmount = uptoAmount;
        this.fee = fee;
    }
}
