package uk.gov.hmcts.fees.register.api.model;

import org.junit.Test;

public class RangeTest {

    @Test(expected = RangeEmptyException.class)
    public void fromIsEqualTo() {
        Range.rangeWith().from(0).to(0).build();
    }

    @Test(expected = RangeEmptyException.class)
    public void fromIsLowerThanTo() {
        Range.rangeWith().from(1).to(0).build();
    }

}
