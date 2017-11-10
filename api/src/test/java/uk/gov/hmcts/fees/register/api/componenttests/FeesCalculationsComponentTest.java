package uk.gov.hmcts.fees.register.api.componenttests;

import org.junit.Test;
import uk.gov.hmcts.fees.register.api.contract.CalculationDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FeesCalculationsComponentTest extends ComponentTestBase {

    @Test
    public void calculateFixed() throws Exception {
        restActions
            .get("/fees/X0433/calculations?value=999999999")
            .andExpect(status().isOk())
            .andExpect(body().as(CalculationDto.class, dto -> {
                assertThat(dto.getAmount()).isEqualTo(41000);
                assertThat(dto.getFee().getCode()).isEqualTo("X0433");
            }));
    }

    @Test
    public void calculatePercentage() throws Exception {
        restActions
            .get("/fees/X0434/calculations?value=1000")
            .andExpect(status().isOk())
            .andExpect(body().as(CalculationDto.class, dto -> {
                assertThat(dto.getAmount()).isEqualTo(45);
                assertThat(dto.getFee().getCode()).isEqualTo("X0434");
            }));
    }
}
