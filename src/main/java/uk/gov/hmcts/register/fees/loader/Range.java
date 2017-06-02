package uk.gov.hmcts.register.fees.loader;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class Range {
    private BigDecimal startAmount;
    private BigDecimal uptoAmount;
    private Fee fee;
}
