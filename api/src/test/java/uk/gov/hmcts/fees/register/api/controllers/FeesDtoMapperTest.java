package uk.gov.hmcts.fees.register.api.controllers;

import java.math.BigDecimal;
import org.junit.Test;
import uk.gov.hmcts.fees.register.api.contract.FeeDto;
import uk.gov.hmcts.fees.register.api.contract.FixedFeeDto;
import uk.gov.hmcts.fees.register.api.contract.PercentageFeeDto;
import uk.gov.hmcts.fees.register.api.controllers.fees.FeesDtoMapper;
import uk.gov.hmcts.fees.register.api.model.Fee;
import uk.gov.hmcts.fees.register.api.model.FixedFee;
import uk.gov.hmcts.fees.register.api.model.PercentageFee;

import static org.assertj.core.api.Assertions.assertThat;

public class FeesDtoMapperTest {

    private final FeesDtoMapper mapper = new FeesDtoMapper();

    @Test
    public void convertsFixedFee() {
        assertThat(mapper.toFeeDto(new FixedFee(1, "code", "description", 999)))
            .isEqualTo(new FixedFeeDto("code", "description", 999));
    }

    @Test
    public void convertsPercentageFee() {
        assertThat(mapper.toFeeDto(new PercentageFee(1, "code", "description", BigDecimal.valueOf(4.5))))
            .isEqualTo(new PercentageFeeDto("code", "description", BigDecimal.valueOf(4.5)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsOnUnknownFee() {
        mapper.toFeeDto(new Fee() {
        });
    }

    @Test
    public void convertsFixedFeeDto() {
        assertThat(mapper.toFee("code", new FixedFeeDto("otherCode", "description", 999)))
            .isEqualTo(new FixedFee(null, "code", "description", 999));
    }

    @Test
    public void convertsPercentageFeeDto() {
        assertThat(mapper.toFee("code", new PercentageFeeDto("otherCode", "description", BigDecimal.valueOf(4.5))))
            .isEqualTo(new PercentageFee(null, "code", "description", BigDecimal.valueOf(4.5)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failsOnUnknownFeeDto() {
        mapper.toFee("any", new FeeDto("code", "description") {
        });
    }

}
