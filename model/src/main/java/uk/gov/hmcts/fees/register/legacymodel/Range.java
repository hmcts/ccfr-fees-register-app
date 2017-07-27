package uk.gov.hmcts.fees.register.legacymodel;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Range {
    @JsonProperty("start")
    private final Integer startAmount;
    @JsonProperty("upto")
    private final Integer uptoAmount;
    private final Fee fee;

    @JsonCreator
    @Builder(builderMethodName = "rangeWith")
    public Range(@JsonProperty("start") Integer startAmount,
                 @JsonProperty("upto") Integer uptoAmount,
                 @JsonProperty("fee") Fee fee) {
        this.startAmount = startAmount;
        this.uptoAmount = uptoAmount;
        this.fee = fee;
    }
}
