package uk.gov.hmcts.register.fees.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class Range {
    private final int startAmount;
    private final int uptoAmount;
    private final Fee fee;
}
