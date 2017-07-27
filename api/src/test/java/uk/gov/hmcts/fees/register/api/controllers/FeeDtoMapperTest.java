package uk.gov.hmcts.fees.register.api.controllers;

import java.math.BigDecimal;
import org.junit.Test;
import uk.gov.hmcts.fees.register.api.contract.FixedFeeDto;
import uk.gov.hmcts.fees.register.api.contract.PercentageFeeDto;
import uk.gov.hmcts.fees.register.api.model.Fee;
import uk.gov.hmcts.fees.register.api.model.FixedFee;
import uk.gov.hmcts.fees.register.api.model.PercentageFee;

import static org.assertj.core.api.Assertions.assertThat;

public class FeeDtoMapperTest {

    private final FeeDtoMapper mapper = new FeeDtoMapper();

    @Test
    public void convertsFixedFee() {
        assertThat(mapper.toFeeDto(new FixedFee(1, "code", "description", 999)))
            .isEqualTo(new FixedFeeDto(1, "code", "description", 999));
    }

    @Test
    public void convertsPercentageFee() {
        assertThat(mapper.toFeeDto(new PercentageFee(1, "code", "description", BigDecimal.valueOf(4.5))))
            .isEqualTo(new PercentageFeeDto(1, "code", "description", BigDecimal.valueOf(4.5)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsOnUnknownFee() {
        mapper.toFeeDto(new Fee() {
        });
    }
}
