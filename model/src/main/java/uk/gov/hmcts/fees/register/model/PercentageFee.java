package uk.gov.hmcts.fees.register.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PercentageFee extends Fee {
    @NonNull
    private final BigDecimal percentage;

    @JsonCreator
    @Builder(builderMethodName = "percentageFeeWith")
    public PercentageFee(@JsonProperty("id") String id,
                         @JsonProperty("description") String description,
                         @JsonProperty("percentage") BigDecimal percentage) {
        super(id, description);
        this.percentage = percentage;
    }

    @Override
    public int calculate(int value) {
        return new BigDecimal(value).multiply(percentage).divide(BigDecimal.valueOf(100)).intValue();
    }

    public String  getType() {
        return "percentage";
    }
}
