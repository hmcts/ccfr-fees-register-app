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

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PercentageFee extends Fee implements SpecifiedFee {
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
    public int calculate(int amount) {
        return new BigDecimal(amount).multiply(percentage).divide(BigDecimal.valueOf(100)).intValue();
    }
}
