package uk.gov.hmcts.fees.register.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Optional;
import lombok.Data;

@Data
public class Category {

    private final String id;
    private final List<Range> ranges;

    @JsonCreator
    public Category(@JsonProperty("id") String id,
                    @JsonProperty("ranges") List<Range> ranges) {
        this.id = id;
        this.ranges = ranges;
    }

    public Optional<Range> findRange(int amount) {
        for (Range range : ranges) {
            if (range.getStartAmount() <= amount && amount <= range.getUptoAmount()) {
                return Optional.of(range);
            }
        }

        return Optional.empty();
    }

}
