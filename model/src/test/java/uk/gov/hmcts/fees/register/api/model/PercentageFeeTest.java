package uk.gov.hmcts.fees.register.api.model;

import java.math.BigDecimal;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PercentageFeeTest {

    @Test
    public void simpleCalculation() {
        int amount = percentageFee("4.5").calculate(10000);
        assertThat(amount).isEqualTo(450);
    }

    @Test
    public void roundDownCalculation() {
        int amount = percentageFee("0.99").calculate(100);
        assertThat(amount).isEqualTo(0);
    }

    @Test
    public void roundDownCalculation2() {
        int amount = percentageFee("0.99").calculate(102);
        assertThat(amount).isEqualTo(1);
    }

    private PercentageFee percentageFee(String percentage) {
        return PercentageFee.percentageFeeWith().code("any").description("any").percentage(new BigDecimal(percentage)).build();
    }

}
