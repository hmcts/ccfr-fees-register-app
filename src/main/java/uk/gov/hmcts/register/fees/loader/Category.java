package uk.gov.hmcts.register.fees.loader;

import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class Category {

    private String claimCategoryId;

    private List<Range> ranges;

    public Range getClaimRange(BigDecimal claimAmount) {
        Range requiredRange = null;

        for (Range range : ranges) {
            if ((claimAmount.compareTo(range.getUptoAmount()) == -1) || (claimAmount.compareTo(range.getUptoAmount()) == 0)) {
                requiredRange = range;
                break;
            }
        }

        return requiredRange;
    }

}
