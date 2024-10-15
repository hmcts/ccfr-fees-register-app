package uk.gov.hmcts.fees.register.api.contract;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PercentageFeeDto extends FeeDto {
    @NotNull(message = "must not be null")
    @DecimalMin(value = "0.01", message = "must be greater than or equal to 0.01")
    @DecimalMax(value = "100.00", message = "must be less than or equal to 100.00")
    private final BigDecimal percentage;

    @JsonCreator
    @Builder(builderMethodName = "percentageFeeDtoWith")
    public PercentageFeeDto(@JsonProperty("code") final String code,
                            @JsonProperty("description") final String description,
                            @JsonProperty("percentage") final BigDecimal percentage) {
        super(code, description);
        this.percentage = percentage;
    }

    public String getType() {
        return "percentage";
    }
}
