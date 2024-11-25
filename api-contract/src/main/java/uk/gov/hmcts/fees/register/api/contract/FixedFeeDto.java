package uk.gov.hmcts.fees.register.api.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FixedFeeDto extends FeeDto {
    @NotNull
    @Min(0)
    private final Integer amount;

    @JsonCreator
    @Builder(builderMethodName = "fixedFeeDtoWith")
    public FixedFeeDto(@JsonProperty("code") String code,
                       @JsonProperty("description") String description,
                       @JsonProperty("amount") Integer amount) {
        super(code, description);
        this.amount = amount;
    }

    public String getType() {
        return "fixed";
    }
}
