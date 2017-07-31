package uk.gov.hmcts.fees.register.api.componenttests;

import java.util.Arrays;
import org.junit.Test;
import uk.gov.hmcts.fees.register.api.contract.RangeGroupDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.hmcts.fees.register.api.contract.FixedFeeDto.fixedFeeDtoWith;
import static uk.gov.hmcts.fees.register.api.contract.RangeDto.rangeDtoWith;
import static uk.gov.hmcts.fees.register.api.contract.RangeGroupDto.rangeGroupDtoWith;

public class RangeGroupsCrudComponentTest extends ComponentTestBase {

    private static final RangeGroupDto RANGE_GROUP_PROBATE_COPIES = rangeGroupDtoWith()
        .id(4)
        .description("Probate - Copies")
        .ranges(Arrays.asList(
            rangeDtoWith()
                .from(0)
                .to(1)
                .fee(fixedFeeDtoWith()
                    .id(20)
                    .code("X0251-1")
                    .description("First copy (consisting of grant including a copy of the Will, if applicable) ordered after the Grant of Representation has been issued.")
                    .amount(1000)
                    .build())
                .build(),

            rangeDtoWith()
                .from(2)
                .fee(fixedFeeDtoWith()
                    .id(21)
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
    public void retrieveById() throws Exception {
        restActions
            .get("/range-groups/4")
            .andExpect(status().isOk())
            .andExpect(body().as(RangeGroupDto.class, rangeGroup -> {
                assertThat(rangeGroup).isEqualTo(RANGE_GROUP_PROBATE_COPIES);
            }));
    }

    @Test
    public void retrieveByUnknownId() throws Exception {
        restActions
            .get("/range-groups/-1")
            .andExpect(status().isNotFound());
    }
}
