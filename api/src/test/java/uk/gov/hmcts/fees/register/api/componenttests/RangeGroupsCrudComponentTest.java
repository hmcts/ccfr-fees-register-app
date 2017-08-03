package uk.gov.hmcts.fees.register.api.componenttests;

import java.util.Arrays;
import java.util.Collections;
import org.junit.Test;
import uk.gov.hmcts.fees.register.api.contract.RangeGroupDto;

import static java.lang.String.join;
import static java.util.Collections.nCopies;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.hmcts.fees.register.api.contract.FixedFeeDto.fixedFeeDtoWith;
import static uk.gov.hmcts.fees.register.api.contract.RangeDto.rangeDtoWith;
import static uk.gov.hmcts.fees.register.api.contract.RangeGroupDto.rangeGroupDtoWith;

public class RangeGroupsCrudComponentTest extends ComponentTestBase {

    private static final RangeGroupDto RANGE_GROUP_PROBATE_COPIES = rangeGroupDtoWith()
        .code("probate-copies")
        .description("Probate - Copies")
        .ranges(Arrays.asList(
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
    public void updatePercentageFee() throws Exception {
        RangeGroupDto.RangeGroupDtoBuilder proposeRangeGroup = rangeGroupDtoWith()
            .code("ignored")
            .description("New Description")
            .ranges(Collections.emptyList());

        restActions
            .put("/range-groups/cmc-online", proposeRangeGroup.build())
            .andExpect(status().isOk())
            .andExpect(body().as(RangeGroupDto.class, rangeGroupDto -> {
                assertThat(rangeGroupDto.getCode()).isEqualTo("cmc-online");
                assertThat(rangeGroupDto.getDescription()).isEqualTo("New Description");
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

    private RangeGroupDto.RangeGroupDtoBuilder validRangeGroupDto() {
        return rangeGroupDtoWith().description("any").ranges(Collections.emptyList());
    }


    private void assertValidationMessage(String urlTemplate, RangeGroupDto rangeGroupDto, String message) throws Exception {
        restActions
            .put(urlTemplate, rangeGroupDto)
            .andExpect(status().isBadRequest())
            .andExpect(body().isErrorWithMessage(message));
    }
}
