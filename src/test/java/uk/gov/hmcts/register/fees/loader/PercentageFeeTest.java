package uk.gov.hmcts.register.fees.loader;

import java.math.BigDecimal;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PercentageFeeTest {

    @Test
    public void simpleCalculation() {
        int amount = new PercentageFee(new BigDecimal("4.5")).calculate(10000);
        assertThat(amount).isEqualTo(450);
    }

    @Test
    public void roundDownCalculation() {
        int amount = new PercentageFee(new BigDecimal("0.99")).calculate(101);
        assertThat(amount).isEqualTo(0);
    }

    @Test
    public void roundDownCalculation2() {
        int amount = new PercentageFee(new BigDecimal("0.99")).calculate(102);
        assertThat(amount).isEqualTo(1);
    }

}