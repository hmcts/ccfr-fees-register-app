package uk.gov.hmcts.fees.register.model;

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
public class FixedFee extends Fee {
    private final int amount;

    @JsonCreator
    @Builder(builderMethodName = "fixedFeeWith")
    public FixedFee(@JsonProperty("id") String id,
                    @JsonProperty("description") String description,
                    @JsonProperty("amount") int amount) {
        super(id, description);
        this.amount = amount;
    }

    @Override
    public int calculate(int value) {
        return this.amount;
    }

    public String  getType() {
        return "fixed";
    }
}
