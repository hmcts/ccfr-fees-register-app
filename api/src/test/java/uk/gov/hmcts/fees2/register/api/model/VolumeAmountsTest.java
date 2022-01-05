package uk.gov.hmcts.fees2.register.api.model;

import org.junit.Test;
import uk.gov.hmcts.fees2.register.data.model.amount.Amount;
import uk.gov.hmcts.fees2.register.data.model.amount.VolumeAmount;

import java.math.BigDecimal;

import static org.junit.Assert.assertTrue;

public class VolumeAmountsTest {

    @Test
    public void simple() {
        Amount amount = new VolumeAmount(new BigDecimal("10"));
        assertTrue(amount.calculateFee(BigDecimal.TEN).compareTo(new BigDecimal(100)) == 0);
    }

    @Test
    public void zero() {
        Amount amount = new VolumeAmount(new BigDecimal("10"));
        assertTrue(amount.calculateFee(BigDecimal.ZERO).compareTo(BigDecimal.ZERO) == 0);
    }

    @Test
    public void typical() {
        Amount amount = new VolumeAmount(new BigDecimal("5"));
        assertTrue(amount.calculateFee(new BigDecimal("65")).compareTo(new BigDecimal("325")) == 0);
    }

}
