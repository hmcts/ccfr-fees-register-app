package uk.gov.hmcts.fees.register.api.componenttests;

import org.junit.Test;
import uk.gov.hmcts.fees.register.api.contract.RangeGroupDto;
import uk.gov.hmcts.fees.register.api.contract.RangeGroupUpdateDto;
import uk.gov.hmcts.fees.register.api.contract.RangeGroupUpdateDto.RangeGroupUpdateDtoBuilder;
import uk.gov.hmcts.fees.register.api.contract.RangeGroupUpdateDto.RangeUpdateDto;

import static java.lang.String.join;
import static java.util.Arrays.asList;
import static java.util.Collections.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.hmcts.fees.register.api.contract.FixedFeeDto.fixedFeeDtoWith;
import static uk.gov.hmcts.fees.register.api.contract.RangeGroupDto.RangeDto.rangeDtoWith;
import static uk.gov.hmcts.fees.register.api.contract.RangeGroupDto.rangeGroupDtoWith;
import static uk.gov.hmcts.fees.register.api.contract.RangeGroupUpdateDto.RangeUpdateDto.rangeUpdateDtoWith;
import static uk.gov.hmcts.fees.register.api.contract.RangeGroupUpdateDto.rangeGroupUpdateDtoWith;

public class RangeGroupsCrudComponentTest extends ComponentTestBase {

    private static final RangeGroupDto RANGE_GROUP_PROBATE_COPIES = rangeGroupDtoWith()
        .code("probate-copies")
        .description("Probate - Copies")
        .ranges(asList(
            rangeDtoWith()
                .from(0)
                .to(1)
                .fee(fixedFeeDtoWith()
                    .code("X0251-1")
                    .description("First copy (consisting of grant including a copy of the Will, if applicable) ordered after the Grant of Representation has been issued.")
                    .amount(1000)
                    .build())
                .build(),

            rangeDtoWith()
                .from(2)
                .fee(fixedFeeDtoWith()
                    .code("X0251-2")
                    .description("Additional copies (consisting of grant including a copy of the Will, if applicable) ordered after the Grant of Representation has been issued.")
                    .amount(50)
                    .build())
                .build()
        ))
        .build();

    @Test
    public void retrieveAll() throws Exception {
        restActions
            .get("/range-groups")
            .andExpect(status().isOk())
            .andExpect(body().asListOf(RangeGroupDto.class, rangeGroups -> {
                assertThat(rangeGroups).contains(RANGE_GROUP_PROBATE_COPIES);
            }));
    }

    @Test
    public void retrieveOne() throws Exception {
        restActions
            .get("/range-groups/probate-copies")
            .andExpect(status().isOk())
            .andExpect(body().as(RangeGroupDto.class, rangeGroup -> {
                assertThat(rangeGroup).isEqualTo(RANGE_GROUP_PROBATE_COPIES);
            }));
    }

    @Test
    public void retrieveNonExisting() throws Exception {
        restActions
            .get("/range-groups/-1")
            .andExpect(status().isNotFound());
    }

    @Test
    public void updateRangeGroup() throws Exception {
        RangeGroupUpdateDtoBuilder proposeRangeGroup = rangeGroupUpdateDtoWith()
            .code("ignored")
            .description("New Description")
            .ranges(asList(
                new RangeUpdateDto(0, 1000, "X0046"),
                new RangeUpdateDto(1001, null, "X0047")
            ));

        restActions
            .put("/range-groups/cmc-online", proposeRangeGroup.build())
            .andExpect(status().isOk())
            .andExpect(body().as(RangeGroupDto.class, rangeGroupDto -> {
                assertThat(rangeGroupDto.getCode()).isEqualTo("cmc-online");
                assertThat(rangeGroupDto.getDescription()).isEqualTo("New Description");
                assertThat(rangeGroupDto.getRanges()).hasSize(2);
                assertThat(rangeGroupDto.getRanges()).contains(rangeDtoWith()
                    .from(1001)
                    .to(null)
                    .fee(fixedFeeDtoWith().code("X0047").amount(54500).description("Civil Court fees - Hearing fees - Fast track claim").build())
                    .build()
                );
            }));
    }

    @Test
    public void validateCode() throws Exception {
        assertValidationMessage("/range-groups/" + join("", nCopies(51, "A")), validRangeGroupDto().build(), "code: length must be between 0 and 50");
    }

    @Test
    public void validateDescription() throws Exception {
        assertValidationMessage("/range-groups/cmc-online", validRangeGroupDto().description(null).build(), "description: may not be empty");
        assertValidationMessage("/range-groups/cmc-online", validRangeGroupDto().description("").build(), "description: may not be empty");
        assertValidationMessage("/range-groups/cmc-online", validRangeGroupDto().description(join("", nCopies(2001, "A"))).build(), "description: length must be between 0 and 2000");
    }

    @Test
    public void validateFeeExists() throws Exception {
        RangeGroupUpdateDto rangeGroupWithNonExistingFee = validRangeGroupDto()
            .ranges(singletonList(rangeUpdateDtoWith().from(100).to(1000).feeCode("non-existing").build()))
            .build();
        assertValidationMessage("/range-groups/cmc-online", rangeGroupWithNonExistingFee, "ranges.feeCode: unknown fee code provided");
    }

    private RangeGroupUpdateDto.RangeGroupUpdateDtoBuilder validRangeGroupDto() {
        return rangeGroupUpdateDtoWith().description("any").ranges(emptyList());
    }

    private void assertValidationMessage(String urlTemplate, RangeGroupUpdateDto dto, String message) throws Exception {
        restActions
            .put(urlTemplate, dto)
            .andExpect(status().isBadRequest())
            .andExpect(body().isErrorWithMessage(message));
    }
}
