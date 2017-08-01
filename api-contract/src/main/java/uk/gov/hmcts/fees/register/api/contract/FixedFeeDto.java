package uk.gov.hmcts.fees.register.api.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FixedFeeDto extends FeeDto {
    private final int amount;

    @JsonCreator
    @Builder(builderMethodName = "fixedFeeDtoWith")
    public FixedFeeDto(@JsonProperty("code") String code,
                       @JsonProperty("description") String description,
                       @JsonProperty("amount") int amount) {
        super(code, description);
        this.amount = amount;
    }

    public String getType() {
        return "fixed";
    }
}
