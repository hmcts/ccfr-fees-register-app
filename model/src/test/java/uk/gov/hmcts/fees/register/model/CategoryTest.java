package uk.gov.hmcts.fees.register.model;

import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryTest {

    private static final Range RANGE_1_30000 = new Range(1, 30000, null);
    private static final Range RANGE_30001_50000 = new Range(30001, 50000, null);
    private static final Range RANGE_50001_100000 = new Range(50001, 100000, null);
    private static final Range RANGE_100001_UNLIMITED = new Range(100001, Integer.MAX_VALUE, null);
    private static final FixedFee FIXEDFEE_X0046 =new FixedFee("X0046","Civil Court fees - Hearing fees - Multi track claim",109000);
    private static final FixedFee FIXEDFEE_X0047 =new FixedFee("X0047","Civil Court fees - Hearing fees - Fast track claim",54500);
    private static final Category CATEGORY = new Category("any", Arrays.asList(
        RANGE_1_30000,
        RANGE_30001_50000,
        RANGE_50001_100000,
        RANGE_100001_UNLIMITED),Arrays.asList(
        FIXEDFEE_X0046,
        FIXEDFEE_X0047
        ));

    @Test
    public void findRangeShouldBeInclusive() {
        assertThat(CATEGORY.findRange(1).get()).isEqualTo(RANGE_1_30000);
        assertThat(CATEGORY.findRange(30000).get()).isEqualTo(RANGE_1_30000);
    }

    @Test
    public void findRangeInRange() {
        assertThat(CATEGORY.findRange(40000).get()).isEqualTo(RANGE_30001_50000);
        assertThat(CATEGORY.findRange(Integer.MAX_VALUE - 1).get()).isEqualTo(RANGE_100001_UNLIMITED);
    }

    @Test
    public void findFlatFeeInACategory() {
        assertThat(CATEGORY.findFlatFee("X0046").get()).isEqualTo(FIXEDFEE_X0046);
    }

    @Test
    public void unmatchedRangeShouldReturnEmpty() {
        assertThat(CATEGORY.findRange(0).isPresent()).isFalse();
    }


    @Test
    public void unmatchedFlatFeeInACategoryShouldReturnEmpty() {
        assertThat(CATEGORY.findFlatFee("X0000").isPresent()).isFalse();
    }

}
