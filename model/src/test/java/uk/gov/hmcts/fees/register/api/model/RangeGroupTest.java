package uk.gov.hmcts.fees.register.api.model;

import org.junit.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class RangeGroupTest {

    public static final FixedFee ANY_FEE = FixedFee.fixedFeeWith().amount(0).code("any").description("any").build();

    @Test(expected = RangeGroupNotContinuousException.class)
    public void overlappingRangesNotAllowed() {
        validRangeGroup().ranges(asList(
            Range.rangeWith().from(0).to(100).fee(ANY_FEE).build(),
            Range.rangeWith().from(100).to(101).fee(ANY_FEE).build()
        )).build();
    }

    @Test(expected = RangeGroupNotContinuousException.class)
    public void gapInRangesNotAllowed() {
        validRangeGroup().ranges(asList(
            Range.rangeWith().from(0).to(100).fee(ANY_FEE).build(),
            Range.rangeWith().from(102).to(110).fee(ANY_FEE).build()
        )).build();
    }

    @Test(expected = RangeGroupNotContinuousException.class)
    public void twoNonEndingRangesNotAllowed() {
        validRangeGroup().ranges(asList(
            Range.rangeWith().from(0).to(null).fee(ANY_FEE).build(),
            Range.rangeWith().from(1).to(null).fee(ANY_FEE).build()
        )).build();
    }

    @Test
    public void rangeOrderingShouldBeIgnored() {
        validRangeGroup().ranges(asList(
            Range.rangeWith().from(101).to(110).fee(ANY_FEE).build(),
            Range.rangeWith().from(111).to(120).fee(ANY_FEE).build(),
            Range.rangeWith().from(0).to(100).fee(ANY_FEE).build()
        )).build();
    }

    private RangeGroup.RangeGroupBuilder validRangeGroup() {
        return RangeGroup.rangeGroupWith()
            .code("any")
            .description("any")
            .ranges(emptyList());
    }
}
