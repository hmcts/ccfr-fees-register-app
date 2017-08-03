package uk.gov.hmcts.fees.register.api.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RangeDto {
    @NotNull
    private Integer from;
    private Integer to;
    @NotNull
    private FeeDto fee;

    @JsonCreator
    @Builder(builderMethodName = "rangeDtoWith")
    public RangeDto(@JsonProperty("from") Integer from,
                    @JsonProperty("to") Integer to,
                    @JsonProperty("fee") FeeDto fee) {
        this.from = from;
        this.to = to;
        this.fee = fee;
    }
}
