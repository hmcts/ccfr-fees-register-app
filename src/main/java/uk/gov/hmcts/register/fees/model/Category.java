package uk.gov.hmcts.register.fees.model;

import java.util.List;
import java.util.Optional;
import lombok.Data;

@Data
public class Category {

    private final String id;
    private final List<Range> ranges;

    public Optional<Range> findRange(int amount) {
        for (Range range : ranges) {
            if (range.getStartAmount() <= amount && amount <= range.getUptoAmount()) {
                return Optional.of(range);
            }
        }

        return Optional.empty();
    }

}
