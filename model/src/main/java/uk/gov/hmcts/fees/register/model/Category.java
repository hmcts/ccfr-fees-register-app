package uk.gov.hmcts.fees.register.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.Builder;
import lombok.Data;

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
        this.ranges = (null == ranges ? Collections.emptyList() : ranges);
        this.flatFees = (null == flatFees ? Collections.emptyList() : flatFees);
    }

    public Optional<Range> findRange(int amount) {
        return ranges.stream().filter(containsAmount(amount)).findFirst();
    }

    private Predicate<Range> containsAmount(int amount) {
        return range -> (null == range.getUptoAmount()) || (range.getStartAmount() <= amount && amount <= range.getUptoAmount());
    }

    public Optional<Fee> findFlatFee(String feeId) {
        return flatFees.stream().filter(x -> feeId.equals(x.getId())).findFirst();
    }
}
