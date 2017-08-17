package uk.gov.hmcts.fees.register.api.componenttests;

import java.util.Arrays;
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
            .description("New Description")
            .ranges(asList(
                new RangeUpdateDto(0, 1000, "X0046"),
                new RangeUpdateDto(1001, null, "X0047")
            ));

        restActions
            .withUser("admin")
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
    public void createRangeGroup() throws Exception {
        RangeGroupUpdateDtoBuilder proposeRangeGroup = rangeGroupUpdateDtoWith()
            .description("New Description")
            .ranges(asList(
                new RangeUpdateDto(0, 1000, "X0046"),
                new RangeUpdateDto(1001, null, "X0047")
            ));

        restActions
            .withUser("admin")
            .put("/range-groups/new-group", proposeRangeGroup.build())
            .andExpect(status().isOk())
            .andExpect(body().as(RangeGroupDto.class, rangeGroupDto -> {
                assertThat(rangeGroupDto.getCode()).isEqualTo("new-group");
                assertThat(rangeGroupDto.getDescription()).isEqualTo("New Description");
                assertThat(rangeGroupDto.getRanges()).hasSize(2);
            }));
    }

    @Test
    public void validateCode() throws Exception {
        assertValidationMessage("/range-groups/" + join("", nCopies(51, "A")), validRangeGroupUpdateDto().build(), "code: length must be between 0 and 50");
    }

    @Test
    public void validateDescription() throws Exception {
        assertValidationMessage("/range-groups/cmc-online", validRangeGroupUpdateDto().description(null).build(), "description: may not be empty");
        assertValidationMessage("/range-groups/cmc-online", validRangeGroupUpdateDto().description("").build(), "description: may not be empty");
        assertValidationMessage("/range-groups/cmc-online", validRangeGroupUpdateDto().description(join("", nCopies(2001, "A"))).build(), "description: length must be between 0 and 2000");
    }

    @Test
    public void validateFeeExists() throws Exception {
        RangeGroupUpdateDto rangeGroupWithNonExistingFee = rangeGroupWithRange(rangeUpdateDtoWith().from(100).to(1000).feeCode("non-existing").build());
        assertValidationMessage("/range-groups/cmc-online", rangeGroupWithNonExistingFee, "ranges: one of the ranges contains unknown fee code");
    }

    @Test
    public void validateRangeFrom() throws Exception {
        assertValidationMessage("/range-groups/cmc-online", rangeGroupWithRange(rangeUpdateDtoWith().from(null).to(1000).feeCode("X0047").build()), "ranges[0].from: may not be null");
        assertValidationMessage("/range-groups/cmc-online", rangeGroupWithRange(rangeUpdateDtoWith().from(-1).to(1000).feeCode("X0047").build()), "ranges[0].from: must be greater than or equal to 0");
    }

    @Test
    public void validateEmptyRange() throws Exception {
        assertValidationMessage("/range-groups/cmc-online", rangeGroupWithRange(rangeUpdateDtoWith().from(0).to(0).feeCode("X0047").build()), "ranges: one of the ranges is empty");
    }

    @Test
    public void validateContinuousRanges() throws Exception {
        RangeGroupUpdateDto nonContinuousRangeGroup = validRangeGroupUpdateDto()
            .ranges(Arrays.asList(
                rangeUpdateDtoWith().from(0).to(1000).feeCode("X0047").build(),
                rangeUpdateDtoWith().from(1002).to(null).feeCode("X0048").build()
            ))
            .build();

        assertValidationMessage("/range-groups/cmc-online", nonContinuousRangeGroup, "ranges: provided set of ranges contains gaps or overlaps");
    }

    private RangeGroupUpdateDto rangeGroupWithRange(RangeUpdateDto rangeDto) {
        return validRangeGroupUpdateDto().ranges(singletonList(rangeDto)).build();
    }

    private RangeGroupUpdateDto.RangeGroupUpdateDtoBuilder validRangeGroupUpdateDto() {
        return rangeGroupUpdateDtoWith().description("any").ranges(emptyList());
    }

    private void assertValidationMessage(String urlTemplate, RangeGroupUpdateDto dto, String message) throws Exception {
        restActions
            .withUser("admin")
            .put(urlTemplate, dto)
            .andExpect(status().isBadRequest())
            .andExpect(body().isErrorWithMessage(message));
    }
}
