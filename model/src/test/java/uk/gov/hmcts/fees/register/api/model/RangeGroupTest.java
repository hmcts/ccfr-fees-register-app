package uk.gov.hmcts.fees.register.api.model;

import org.junit.Test;
import uk.gov.hmcts.fees.register.api.model.exceptions.RangeNotFoundException;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.hmcts.fees.register.api.model.FixedFeeOld.fixedFeeWith;

public class RangeGroupTest {

    public static final FixedFeeOld ANY_FEE = fixedFeeWith().amount(0).code("any").description("any").build();

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

    @Test
    public void findMatchingFee() {
        RangeGroup rangeGroup = validRangeGroup().ranges(asList(
            Range.rangeWith().from(0).to(100).fee(fixedFeeWith().amount(100).code("1").description("any").build()).build(),
            Range.rangeWith().from(101).to(200).fee(fixedFeeWith().amount(200).code("2").description("any").build()).build()
        )).build();

        assertThat(rangeGroup.findFeeForValue(99).getCode()).isEqualTo("1");
        assertThat(rangeGroup.findFeeForValue(150).getCode()).isEqualTo("2");
    }

    @Test(expected = RangeNotFoundException.class)
    public void findThrowsRangeNotFoundException() {
        RangeGroup rangeGroup = validRangeGroup().ranges(asList(
            Range.rangeWith().from(0).to(100).fee(fixedFeeWith().amount(100).code("any").description("any").build()).build(),
            Range.rangeWith().from(101).to(200).fee(fixedFeeWith().amount(200).code("any").description("any").build()).build()
        )).build();

        rangeGroup.findFeeForValue(201);
    }

    private RangeGroup.RangeGroupBuilder validRangeGroup() {
        return RangeGroup.rangeGroupWith()
            .code("any")
            .description("any")
            .ranges(emptyList());
    }
}
