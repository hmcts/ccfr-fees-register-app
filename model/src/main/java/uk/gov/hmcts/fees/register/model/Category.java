package uk.gov.hmcts.fees.register.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Category {

    private final String id;
    private final List<Range> ranges;
    private final List<Fee> flatFees;

    @JsonCreator
    @Builder(builderMethodName = "categoryWith")
    public Category(@JsonProperty("id") String id,
                    @JsonProperty("ranges") List<Range> ranges,
                    @JsonProperty("flatFees") List<Fee> flatFees) {
        this.id = id;
        this.ranges = ranges;
        this.flatFees = flatFees;
    }

    public Optional<Range> findRange(int amount) {
        if(null != ranges) {
            for (Range range : ranges) {
                if ((null == range.getUptoAmount()) || (range.getStartAmount() <= amount && amount <= range.getUptoAmount())) {
                    return Optional.of(range);
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Fee> findFlatFee(String feeId) {

        if (null != flatFees)
            return flatFees.stream().filter(x -> feeId.equals(x.getId())).findFirst();

        return Optional.empty();

    }


}
