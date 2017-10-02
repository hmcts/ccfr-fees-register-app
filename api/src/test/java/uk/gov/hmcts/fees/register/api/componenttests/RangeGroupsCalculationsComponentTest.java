package uk.gov.hmcts.fees.register.api.componenttests;

import lombok.ToString;
import org.junit.Test;
import uk.gov.hmcts.fees.register.api.contract.CalculationDto;
import uk.gov.hmcts.fees.register.api.contract.ErrorDto;
import uk.gov.hmcts.fees.register.api.contract.FeeDto;
import uk.gov.hmcts.fees.register.api.contract.PercentageFeeDto;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.gov.hmcts.fees.register.api.contract.PercentageFeeDto.percentageFeeDtoWith;

public class RangeGroupsCalculationsComponentTest extends ComponentTestBase {

    @Test
    public void calculateInclusive() throws Exception {
        restActions
            .get("/range-groups/cmc-online/calculations?value=50000")
            .andExpect(status().isOk())
            .andExpect(body().as(CalculationDto.class, dto -> {
                assertThat(dto.getAmount()).isEqualTo(3500);
                assertThat(dto.getFee().getCode()).isEqualTo("X0025");
            }));

        restActions
            .get("/range-groups/cmc-online/calculations?value=50001")
            .andExpect(status().isOk())
            .andExpect(body().as(CalculationDto.class, dto -> {
                assertThat(dto.getAmount()).isEqualTo(6000);
                assertThat(dto.getFee().getCode()).isEqualTo("X0026");
            }));
    }

    @Test
    public void maxFeesForUnspecifiedClaimAmount() throws Exception {
        restActions
            .get("/range-groups/cmc-paper/calculations")
            .andExpect(status().isOk())
            .andExpect(body().as(CalculationDto.class, dto -> {
                assertThat(dto.getAmount()).isGreaterThan(0);
            }));
    }

    @Test
    public void unknownFeesForIncorrectRangeGroupCode() throws Exception {
        restActions
            .get("/range-groups/cmc-online/calculations")
            .andExpect(status().isNotFound());
    }


    @Test
    public void maxPercentageForUnspecifiedClaimAmount() throws Exception {
        restActions
            .get("/range-groups/cmc-online/calculations")
            .andExpect(status().isOk())
            .andExpect(body().as(CalculationDto.class, dto -> {
                assertThat(dto.getFee()).isEqualTo(
                    percentageFeeDtoWith()
                        .code("X0434")
                        .description("Civil Court fees - Money Claims Online - Claim Amount - 10000.01 upto 15000 GBP. Fees are 4.5% of the claim value")
                        .percentage(BigDecimal.valueOf(4.5))
                        .build()
                );
            }));
    }



    @Test
    public void calculateOutOfRange() throws Exception {
        restActions
            .get("/range-groups/cmc-online/calculations?value=999999999")
            .andExpect(status().isNotFound())
            .andExpect(body().as(ErrorDto.class, dto -> {
                assertThat(dto.getMessage()).isEqualTo("range for value=999999999 was not found");
            }));
    }
}
