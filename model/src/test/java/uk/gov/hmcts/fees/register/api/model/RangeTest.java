package uk.gov.hmcts.fees.register.api.model;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RangeTest {

    @Test(expected = RangeEmptyException.class)
    public void fromIsEqualTo() {
        Range.rangeWith().from(0).to(0).build();
    }

    @Test(expected = RangeEmptyException.class)
    public void fromIsLowerThanTo() {
        Range.rangeWith().from(1).to(0).build();
    }

    @Test
    public void containsValueIsInclusive() {
        Range range = Range.rangeWith().from(0).to(1000).build();
        assertThat(range.containsValue(0)).isTrue();
        assertThat(range.containsValue(1000)).isTrue();
    }

    @Test
    public void containsValue() {
        Range range = Range.rangeWith().from(0).to(1000).build();
        assertThat(range.containsValue(-1)).isFalse();
        assertThat(range.containsValue(1001)).isFalse();
        assertThat(range.containsValue(500)).isTrue();
    }
}
