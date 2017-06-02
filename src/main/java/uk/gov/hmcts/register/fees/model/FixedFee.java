package uk.gov.hmcts.register.fees.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(NON_NULL)
public class FixedFee extends Fee implements SpecifiedFee {
    private final int amount;

    @Builder(builderMethodName = "fixedFeeWith")
    public FixedFee(String id, String description, int amount) {
        super(id, description);
        this.amount = amount;
    }

    @Override
    public int calculate(int amount) { // TODO: think about getting rid of parameter
        return this.amount;
    }
}
